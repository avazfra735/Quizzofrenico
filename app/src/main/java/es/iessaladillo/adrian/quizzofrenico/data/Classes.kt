package es.iessaladillo.adrian.quizzofrenico.data

import kotlinx.serialization.Serializable

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

data class Quizz(val questions: List<Question>)

@Serializable
data class QuizzSettings( val topic: String, val difficulty: String)
