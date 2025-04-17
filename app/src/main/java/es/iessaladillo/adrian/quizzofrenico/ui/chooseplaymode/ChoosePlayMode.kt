package es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.iessaladillo.adrian.quizzofrenico.data.QuizzSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePlayMode(
    navigateToQuizz: (String, String) -> Unit,
    difficultSelected: String,
    onDifficultSelected: (String) -> Unit,
    inputValue: String,
    onChangeInput: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Elegir dificultad") }
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
            OutlinedTextField(value = inputValue, onValueChange = onChangeInput)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navigateToQuizz(inputValue, difficultSelected) }) {
                Text(text = "Generar")
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

@Preview(showBackground = true)
@Composable
fun ChoosePlayModePreview() {
    ChoosePlayMode(
        navigateToQuizz = { _, _ -> },
        "",
        onDifficultSelected = {},
        "",
        onChangeInput = {},
    )
}