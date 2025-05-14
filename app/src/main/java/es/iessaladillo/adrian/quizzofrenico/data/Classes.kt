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
    val date: String
)

//Is necesary to use serializable to convert the data class to a json string
@Serializable
data class QuizzSettings(val topic: String, val difficulty: String)

@Serializable
data class QuizzResult(
    val topic: String,
    val difficulty: String,
    val answers: String
)