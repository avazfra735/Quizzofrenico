package es.iessaladillo.adrian.quizzofrenico.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val auth: FirebaseAuth, private val firestore: FirebaseFirestore,
    private val model: GenerativeModel
) : Repository {

    override suspend fun generateQuizz(topic: String, difficulty: String): List<Question> {

        return try {
            // 1. Crear prompt estructurado
            val prompt = """
                Genera 5 preguntas de opción múltiple sobre el tema "$topic" en español con un nivel de dificultad $difficulty. 
                Cada pregunta debe tener 4 opciones (a, b, c, d) y una explicación clara de la respuesta correcta.
                No incluyas información que no esté directamente relacionada con el tema "$topic".
                El formato de cada pregunta debe ser EXACTAMENTE como el siguiente ejemplo:

                1. ¿Cuál es la capital de España?
                Opciones: a) Barcelona, b) Madrid, c) Sevilla, d) Valencia
                Respuesta correcta: b) Madrid
                Explicación: Madrid es la capital de España desde 1561.

                ---

                Por favor, sigue este formato para las 5 preguntas:
                1. [Pregunta]
                Opciones: a) ..., b) ..., c) ..., d) ...
                Respuesta correcta: [opción]
                Explicación: [texto]
                ---
            """.trimIndent()
            println(prompt)
            // 2. Llamar al modelo de IA
            val response: GenerateContentResponse = model.generateContent(prompt)
            // 3. Parsear respuesta
            parseQuestions(response.text!!)

        } catch (e: Exception) {
            println("Error during quiz generation: ${e.message}")
            emptyList() // Devolver lista vacía en caso de error
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun authenticate(email: String, password: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            println(auth.currentUser)
            AuthResult.Success
        } catch (e: Exception) {
            auth.signOut() // Cerrar sesión si hay error
            println("Error during authentication: ${e.message}")
            AuthResult.Error(mapFirebaseAuthError(e))
        }
    }

    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            println(auth.currentUser)
            AuthResult.Success
        } catch (e: Exception) {
            auth.signOut() // Cerrar sesión si hay error
            println("Error during registration: ${e.message}")
            AuthResult.Error(mapFirebaseAuthError(e))
        }
    }

    fun mapFirebaseAuthError(e: Exception): String {
        return when (e) {
            is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
            is FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
            is FirebaseAuthInvalidUserException -> "El usuario no existe o fue deshabilitado."
            is FirebaseAuthEmailException -> "El formato del correo electrónico no es válido."
            is FirebaseAuthInvalidCredentialsException -> "Las credenciales son inválidas."
            else -> e.message ?: "Ocurrió un error desconocido."
        }
    }

    override suspend fun saveQuizz(
        topic: String,
        difficulty: String,
        result: Int,
        total: Int,
        timer: String
    ) {
        try {
            val email =
                auth.currentUser?.email ?: throw IllegalStateException("User not authenticated")
            val docRef =
                firestore.collection("users").document(email).collection("quizzes").document()
            val quizData = mapOf(
                "quizId" to docRef.id,
                "topic" to topic,
                "difficulty" to difficulty,
                "result" to result,
                "total" to total,
                "timer" to timer,
                "timestamp" to FieldValue.serverTimestamp(),
            )
            docRef.set(quizData).await()
        } catch (e: Exception) {
            println("Error saving quiz: ${e.message}")
        }
    }

    override suspend fun getQuizz(): List<Score> {
        return try {
            val email =
                auth.currentUser?.email ?: throw IllegalStateException("User not authenticated")
            val quizzes = firestore.collection("users").document(email).collection("quizzes")
                .orderBy("timestamp").get().await()
            quizzes.documents.map { doc ->
                val rawDate = doc.getDate("timestamp")
                val formattedDate = rawDate?.let {
                    SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault()).format(it)
                } ?: "" // Formatear la fecha
                Score(
                    topic = doc.getString("topic") ?: "",
                    difficulty = doc.getString("difficulty") ?: "",
                    score = doc.getLong("result")?.toInt() ?: 0,
                    total = doc.getLong("total")?.toInt() ?: 0,
                    date = formattedDate,
                    timer = doc.getString("timer") ?: ""
                )
            }
        } catch (e: Exception) {
            println("Error fetching quizzes: ${e.message}")
            emptyList()
        }
    }

    private fun parseQuestions(rawText: String): List<Question> {
        return rawText.split("---") // Separar preguntas individuales
            .filter { it.isNotBlank() }
            .mapNotNull { questionBlock ->
                // Regex para extraer componentes
                val questionRegex = """(\d+\.\s)(.*?)\n""".toRegex()
                val optionsRegex = """Opciones:\s*(.*)""".toRegex()
                val answerRegex = """Respuesta correcta:\s*(\w+)""".toRegex()
                val explanationRegex = """Explicación:\s*(.*)""".toRegex()

                try {
                    // Eliminar palabras resaltadas con asteriscos
                    val sanitizedBlock = questionBlock.replace(
                        """\*\*(.*?)\*\*""".toRegex(),
                        "$1"
                    )//El $1 hace referencia al contenido

                    val questionText = questionRegex.find(sanitizedBlock)?.groupValues?.get(2)
                        ?: return@mapNotNull null
                    val options = optionsRegex.find(sanitizedBlock)?.groupValues?.get(1)
                        ?.split(Regex("""\s*,\s*(?=[abcd]\))""")) // Dividir por "a)", "b)", etc.
                        ?: return@mapNotNull null
                    val correctAnswer = answerRegex.find(sanitizedBlock)?.groupValues?.get(1)
                        ?: return@mapNotNull null
                    val explanation =
                        explanationRegex.find(sanitizedBlock)?.groupValues?.get(1) ?: ""

                    Question(
                        question = questionText.trim(),
                        options = options.map { it.trim() },
                        correctAnswer = correctAnswer.trim(),
                        explanation = explanation.trim()
                    )
                } catch (e: Exception) {
                    println("Error parsing question block: ${e.message}")
                    null // Ignorar bloques mal formateados
                }
            }
    }
}