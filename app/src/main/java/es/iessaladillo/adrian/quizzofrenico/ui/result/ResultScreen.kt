package es.iessaladillo.adrian.quizzofrenico.ui.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    topic: String,
    difficulty: String,
    answers: Map<String, Boolean>, // Clave: Título de la pregunta, Valor: Respuesta correcta (true/false)
    navigateToChoosePlayMode: () -> Unit
) {

    // Interceptamos el boton atras del sistema
    BackHandler {
        navigateToChoosePlayMode()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Resultados del Quiz") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Título con el tema y dificultad
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tema: $topic",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Dificultad: $difficulty",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 2. Lista de preguntas con resultados
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                answers.forEach { (question, isCorrect) ->
                    item {
                        ResultItem(
                            question = question,
                            isCorrect = isCorrect
                        )
                    }
                }
            }

            // 3. Botón para volver a ChoosePlayMode
            Button(
                onClick = navigateToChoosePlayMode,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Volver a Elegir Modo de Juego",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ResultItem(
    question: String,
    isCorrect: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Título de la pregunta
        Text(
            text = question,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Ícono de respuesta (✓ o X)
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (isCorrect) Color.Green else Color.Red,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                contentDescription = if (isCorrect) "Correcto" else "Incorrecto",
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
fun ResultScreenPreview() {
    ResultScreen(
        topic = "Matemáticas",
        difficulty = "Fácil",
        answers = mapOf(
            "¿Cuánto es 2 + 2?" to true,
            "¿Cuánto es 3 + 5?" to false,
            "¿Cuánto es 10 - 4?" to true
        ),
        navigateToChoosePlayMode = {}
    )
}