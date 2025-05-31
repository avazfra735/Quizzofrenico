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
import androidx.compose.ui.window.Dialog
import es.iessaladillo.adrian.quizzofrenico.data.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizzScreen(
    questions: List<Question>,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    currentQuestionIndex: Int,
    isLastQuestion: Boolean,
    navigateToResult: (String, String, Map<String, Boolean>) -> Unit,
    isLoading: Boolean,
    selectedAnswer: String,
    optColors: Map<String, Color>,
    topic: String,
    difficulty: String,
    answers: Map<String, Boolean>,
    quizTimer: String,
    isTimeUp: Boolean,
    explanation: String,
    showExplanation: Boolean,
    onExplanationDismiss: () -> Unit
) {
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6a1b9a))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Cargando preguntas...",
                        color = Color(0xFF6a1b9a),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    } else {
        Scaffold(
            topBar = { QuizzTopAppBar(currentQuestionIndex, questions.size, quizTimer) }
        ) { innerPadding ->
            // Comprobar si el tiempo se ha agotado
            if (isTimeUp) {
                Dialog(onDismissRequest = {}) {
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "¡Tiempo agotado!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                navigateToResult(topic, difficulty, answers)
                            }) {
                                Text("Ver resultados")
                            }
                        }
                    }
                }
            }
            // Comprobar si se debe mostrar la explicación
            if (showExplanation) {
                Dialog(onDismissRequest = onExplanationDismiss) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Explicación",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(text = explanation, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = onExplanationDismiss) {
                                Text("Cerrar")
                            }
                        }
                    }
                }
            }
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
                            navigateToResult(topic, difficulty, answers)
                        } else {
                            onNextQuestion()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedAnswer.isNotEmpty()

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizzTopAppBar(currentQuestionIndex: Int, questions: Int, quizTimer: String) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Quiz - Pregunta ${currentQuestionIndex + 1}/${questions}") },
        actions = {
            Text(
                text = quizTimer,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White

            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF6a1b9a),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
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
        navigateToResult = { _, _, _ -> },
        isLoading = false,
        selectedAnswer = "",
        optColors = emptyMap(),
        topic = "Geografía",
        difficulty = "Fácil",
        answers = emptyMap(),
        quizTimer = "00:00",
        isTimeUp = false,
        explanation = "Has fallado esta pregunta porque la capital de Francia es París.",
        showExplanation = true,
        onExplanationDismiss = {}
    )
}