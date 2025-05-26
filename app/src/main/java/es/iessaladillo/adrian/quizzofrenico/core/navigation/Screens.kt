package es.iessaladillo.adrian.quizzofrenico.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Login

@Serializable
object Register

@Serializable
object ChoosePlayMode

@Serializable
object Scores

@Serializable
data class Quizz(val topic: String, val difficulty: String)

@Serializable
data class Result(val topic: String, val difficulty: String, val answers: String,val timer : String)

