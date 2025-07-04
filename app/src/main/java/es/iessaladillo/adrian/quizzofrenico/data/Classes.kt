package es.iessaladillo.adrian.quizzofrenico.data

import kotlinx.serialization.Serializable

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Score(
    val topic: String,
    val difficulty: String,
    val score: Int,
    val total: Int,
    val date: String,
    val timer: String
)

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
}

//Is necesary to use serializable to convert the data class to a json string
@Serializable
data class QuizzSettings(val topic: String, val difficulty: String, val time: Int)

@Serializable
data class QuizzResult(
    val topic: String,
    val difficulty: String,
    val answers: String,
    val timer: String
)