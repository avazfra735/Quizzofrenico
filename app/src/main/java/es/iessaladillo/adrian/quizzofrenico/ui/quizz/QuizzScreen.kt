package es.iessaladillo.adrian.quizzofrenico.ui.quizz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.iessaladillo.adrian.quizzofrenico.data.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizzScreen(
    questions: List<Question>,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    currentQuestionIndex: Int,
    isLastQuestion: Boolean,
    navigateToResult: () -> Unit,
    isLoading: Boolean,
    selectedAnswer: String,
    optColors: Map<String, Color>
) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Text("Generando preguntas...")
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Quiz - Pregunta ${currentQuestionIndex + 1}/${questions.size}") }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Mostrar la pregunta actual
                val currentQuestion = questions[currentQuestionIndex]
                Text(
                    text = currentQuestion.question,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Mostrar las opciones de respuesta
                currentQuestion.options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedAnswer == option,
                                onClick = { onAnswerSelected(option) },
                                enabled = selectedAnswer.isEmpty()
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        println(optColors)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)) // Bordes redondeados
                                .background(
                                    optColors[option] ?: Color.Transparent
                                ) // Color de fondo según la respuesta
                                .border(
                                    2.dp,
                                    Color.Gray,
                                    RoundedCornerShape(8.dp)
                                ) // Borde alrededor del bloque
                                .clickable(enabled = selectedAnswer.isEmpty()) {
                                    onAnswerSelected(
                                        option
                                    )
                                }
                                .padding(16.dp) // Espaciado interno
                        ) {
                            Text(
                                text = option,
                                fontSize = 16.sp,
                                modifier = Modifier.align(Alignment.CenterStart)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre opciones
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para avanzar o finalizar
                Button(
                    onClick = {
                        if (isLastQuestion) {
                            navigateToResult()
                        } else {
                            onNextQuestion()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isLastQuestion) "Finalizar Quiz" else "Siguiente Pregunta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun QuizzScreenPreview() {
    val questions = listOf(
        Question(
            question = "¿Cuál es la capital de Francia?",
            options = listOf("París", "Londres", "Berlín", "Madrid"),
            correctAnswer = "París",
            explanation = "La capital de Francia es París."
        ),
        Question(
            question = "¿Cuál es la capital de España?",
            options = listOf("Madrid", "Barcelona", "Valencia", "Sevilla"),
            correctAnswer = "Madrid",
            explanation = "La capital de España es Madrid."
        )
    )

    QuizzScreen(
        questions = questions,
        onAnswerSelected = { _ -> },
        onNextQuestion = {},
        currentQuestionIndex = 0,
        isLastQuestion = false,
        navigateToResult = {},
        isLoading = false,
        selectedAnswer = "",
        optColors = emptyMap()
    )
}