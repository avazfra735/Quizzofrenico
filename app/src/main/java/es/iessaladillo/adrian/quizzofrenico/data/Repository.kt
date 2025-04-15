package es.iessaladillo.adrian.quizzofrenico.data

interface Repository {

    suspend fun generateQuizz(topic: String, difficulty: String): List<Question>
    suspend fun authenticate(email: String, password: String): String?
    suspend fun register(email: String, password: String): String?
    suspend fun saveQuizzSettings(settings: QuizzSettings): String
}