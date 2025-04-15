package es.iessaladillo.adrian.quizzofrenico.data

import es.iessaladillo.adrian.quizzofrenico.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.generationConfig
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DefaultRepository @Inject constructor() : Repository {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    private val model = GenerativeModel(
        "gemini-2.0-flash",
        BuildConfig.geminiApiKey,
        generationConfig = generationConfig {
            temperature = 0.1f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 8192
            responseMimeType = "text/plain"
        },
    )

    override suspend fun generateQuizz(topic: String, difficulty: String): List<Question> {
        return try {
            // 1. Crear prompt estructurado
            val prompt = """
                Genera 5 preguntas de opción múltiple sobre $topic en español con un nivel $difficulty.
                Formato para cada pregunta:
                1. [Pregunta]
                Opciones: a) ..., b) ..., c) ..., d) ...
                Respuesta correcta: [opción]
                Explicación: [texto]
                ---
            """.trimIndent()

            // 2. Llamar al modelo de IA
            val response: GenerateContentResponse = model.generateContent(prompt)
            // 3. Parsear respuesta
            parseQuestions(response.text!!)

        } catch (e: Exception) {
            println("Error during quiz generation: ${e.message}")
            emptyList() // Devolver lista vacía en caso de error
        }
    }

    override suspend fun authenticate(email: String, password: String): String? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            println("Error during authentication: ${e.message}")
            null
        }
    }

    override suspend fun register(email: String, password: String): String? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid
        } catch (e: Exception) {
            println("Error during registration: ${e.message}")
            null
        }
    }

    override suspend fun saveQuizzSettings(settings: QuizzSettings): String {
        val uid = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val docRef = firestore.collection("users").document(uid).collection("quizzes").document()
        val quizData = mapOf(
            "quizId" to docRef.id,
            "topic" to settings.topic,
            "difficulty" to settings.difficulty,
            "timestamp" to FieldValue.serverTimestamp(),
        )
        docRef.set(quizData).await()
        return docRef.id
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
                    val sanitizedBlock = questionBlock.replace("""\*\*(.*?)\*\*""".toRegex(), "$1")//El $1 hace referencia al contenido

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
                    null // Ignorar bloques mal formateados
                }
            }
    }
}