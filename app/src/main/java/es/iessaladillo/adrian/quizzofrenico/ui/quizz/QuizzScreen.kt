package es.iessaladillo.adrian.quizzofrenico.ui.quizz


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import es.iessaladillo.adrian.quizzofrenico.R
import es.iessaladillo.adrian.quizzofrenico.data.Question
import es.iessaladillo.adrian.quizzofrenico.ui.theme.QuizzofrenicoTheme
import kotlinx.coroutines.delay

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
    onExplanationShown: () -> Unit,
    onExplanationDismiss: () -> Unit,
    explanationPending: Boolean,
    onExplanationPending: () -> Unit,
    error: Boolean,
    onErrorDialogDismiss: () -> Unit
) {
    if (error) {
        Dialog(onDismissRequest = onErrorDialogDismiss) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Ha ocurrido un error al generar las preguntas. Por favor, inténtalo de nuevo y compruebe su conexion.",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onErrorDialogDismiss) {
                        Text("Volver")
                    }
                }
            }
        }
        return
    }


    Scaffold(
        topBar = { QuizzTopBar(currentQuestionIndex, questions.size, quizTimer) }
    ) { innerPadding ->
        QuizzBackground()
        if (isLoading) {
            Dialog(onDismissRequest = {}) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cargando preguntas...",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        } else {
            if (isTimeUp) {
                Dialog(onDismissRequest = {}) {
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "¡Tiempo agotado!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
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

            LaunchedEffect(selectedAnswer) {
                if (selectedAnswer.isNotEmpty() && explanation.isNotBlank()) {
                    onExplanationPending()
                    delay(600) // Esperas a que termine animación de colores (ajusta el tiempo según sea necesario)
                    onExplanationShown()
                    onExplanationPending()
                }
            }

            if (showExplanation) {
                Dialog(onDismissRequest = { }) {
                    AnimatedVisibility(
                        visible = showExplanation,
                        enter = fadeIn(animationSpec = tween(durationMillis = 600)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 600))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Explicación",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = explanation,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = onExplanationDismiss) {
                                    Text("Cerrar")
                                }
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

                AnimatedQuestion(
                    currentQuestionIndex = currentQuestionIndex,
                    selectedAnswer = selectedAnswer,
                    onAnswerSelected = onAnswerSelected,
                    optColors = optColors,
                    questions = questions,
                    isLastQuestion = isLastQuestion,
                    navigateToResult = navigateToResult,
                    topic = topic,
                    difficulty = difficulty,
                    answers = answers,
                    onNextQuestion = onNextQuestion,
                    showExplanation = showExplanation,
                    explanationPending = explanationPending
                )


            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizzTopBar(currentQuestionIndex: Int, questions: Int, quizTimer: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Quiz - Pregunta ${currentQuestionIndex + 1}/${if (questions == 0) "?" else questions}",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            Text(
                text = quizTimer,
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun AnimatedQuestion(
    currentQuestionIndex: Int,
    selectedAnswer: String,
    onAnswerSelected: (String) -> Unit,
    optColors: Map<String, Color>,
    questions: List<Question>,
    isLastQuestion: Boolean,
    navigateToResult: (String, String, Map<String, Boolean>) -> Unit,
    topic: String,
    difficulty: String,
    answers: Map<String, Boolean>,
    onNextQuestion: () -> Unit,
    showExplanation: Boolean,
    explanationPending: Boolean
) {
    @OptIn(ExperimentalAnimationApi::class)
    AnimatedContent(
        targetState = currentQuestionIndex,
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // entra desde la derecha
                animationSpec = tween(500)
            ).togetherWith(
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }, // sale hacia la izquierda
                    animationSpec = tween(500)
                )
            )
        },
        label = "QuestionTransition"
    ) { index ->
        val currentQuestion = questions[index]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    shape = MaterialTheme.shapes.medium
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentQuestion.question,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )

                currentQuestion.options.forEach { option ->
                    val targetColor = optColors[option] ?: MaterialTheme.colorScheme.surface
                    val animatedColor by animateColorAsState(targetColor, tween(700))
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(animatedColor)
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = selectedAnswer.isEmpty()) {
                                    onAnswerSelected(option)
                                }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = option,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isLastQuestion) {
                            navigateToResult(topic, difficulty, answers)
                        } else {
                            onNextQuestion()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedAnswer.isNotEmpty() && !showExplanation && !explanationPending
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

@Composable
fun QuizzBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = if (!isSystemInDarkTheme()) painterResource(id = R.drawable.main_image) else painterResource(
                id = R.drawable.main_image_dark
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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

    QuizzofrenicoTheme {
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
            showExplanation = false,
            onExplanationDismiss = {},
            error = false,
            onErrorDialogDismiss = {},
            onExplanationShown = { },
            explanationPending = false,
            onExplanationPending = { }
        )
    }
}

