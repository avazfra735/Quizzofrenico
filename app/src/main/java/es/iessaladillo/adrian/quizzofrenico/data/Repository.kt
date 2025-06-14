package es.iessaladillo.adrian.quizzofrenico.data

interface Repository {
    suspend fun generateQuizz(topic: String, difficulty: String): List<Question>
    suspend fun authenticate(email: String, password: String): AuthResult
    suspend fun register(email: String, password: String): AuthResult
    suspend fun saveQuizz(topic: String, difficulty: String, result:Int,total: Int,timer:String)
    suspend fun getQuizz(): List<Score>
    fun isUserLoggedIn(): AuthState
    fun signOut()
    fun getUserName(): String
}