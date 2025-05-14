package es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePlayModeScreen(
    navigateToQuizz: (String, String) -> Unit,
    difficultSelected: String,
    onDifficultSelected: (String) -> Unit,
    inputValue: String,
    onChangeInput: (String) -> Unit,
    navigateToScores: () -> Unit
) {
    Scaffold(
        topBar = { ChoosePlayModeTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = navigateToScores,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE), // Color de fondo
                    contentColor = Color.White // Color del texto
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Ver resultados anteriores")
            }
            DifficultyButton(
                text = "Fácil",
                isSelected = difficultSelected == "Fácil",
                onClick = { onDifficultSelected("Fácil") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DifficultyButton(
                text = "Intermedio",
                isSelected = difficultSelected == "Intermedio",
                onClick = { onDifficultSelected("Intermedio") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DifficultyButton(
                text = "Difícil",
                isSelected = difficultSelected == "Difícil",
                onClick = { onDifficultSelected("Difícil") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Introduce el tema del quiz",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = inputValue,
                onValueChange = onChangeInput,
                label = { Text("Tema") },
                placeholder = { Text("Ej: Historia") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navigateToQuizz(inputValue, difficultSelected) }) {
                Text(text = "Generar quiz")
            }
        }

    }
}

@Composable
private fun DifficultyButton(
    text: String, isSelected: Boolean, onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color.Blue else Color.Gray
        ),
    ) {
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePlayModeTopBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = "Elegir dificultad") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF6a1b9a),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ChoosePlayModePreview() {
    ChoosePlayModeScreen(
        navigateToQuizz = { _, _ -> },
        "",
        onDifficultSelected = {},
        "",
        onChangeInput = {},
        navigateToScores = { },
    )
}