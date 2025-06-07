package es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import es.iessaladillo.adrian.quizzofrenico.ui.theme.QuizzofrenicoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePlayModeScreen(
    navigateToQuizz: (String, String, Int) -> Unit,
    difficultSelected: String,
    onDifficultSelected: (String) -> Unit,
    inputValue: String,
    onChangeInput: (String) -> Unit,
    navigateToScores: () -> Unit,
    showSettings: Boolean,
    onShowSettings: () -> Unit,
    timeSelected: Int,
    onTimeSelected: (Int) -> Unit
) {
    Scaffold(
        topBar = { ChoosePlayModeTopBar(onShowSettings) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showSettings) {
                Dialog(onDismissRequest = { onShowSettings() }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Ajustes",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Button(
                                onClick = {
                                    onShowSettings()
                                    navigateToScores()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Ver resultados anteriores")
                            }
                            Text(
                                text = "Selecciona la dificultad",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            DifficultyButton(
                                text = "Fácil",
                                isSelected = difficultSelected == "Fácil",
                                onClick = { onDifficultSelected("Fácil") }
                            )
                            DifficultyButton(
                                text = "Intermedio",
                                isSelected = difficultSelected == "Intermedio",
                                onClick = { onDifficultSelected("Intermedio") }
                            )
                            DifficultyButton(
                                text = "Difícil",
                                isSelected = difficultSelected == "Difícil",
                                onClick = { onDifficultSelected("Difícil") }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Selecciona el tiempo",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TimeButton(
                                text = "1 min",
                                isSelected = timeSelected == 1,
                                onClick = { onTimeSelected(1) }
                            )
                            TimeButton(
                                text = "5 min",
                                isSelected = timeSelected == 5,
                                onClick = { onTimeSelected(5) }
                            )
                            TimeButton(
                                text = "10 min",
                                isSelected = timeSelected == 10,
                                onClick = { onTimeSelected(10) }
                            )
                        }
                    }
                }
            }
            Text(
                text = "Introduce el tema del quiz",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
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
            Button(
                onClick = { navigateToQuizz(inputValue, difficultSelected, timeSelected) },
                enabled = difficultSelected.isNotEmpty() && inputValue.isNotEmpty() && timeSelected > 0,
            ) {
                Text(text = "Generar quiz")
            }
        }
    }
}

@Composable
private fun TimeButton(
    text: String, isSelected: Boolean, onClick: () -> Unit
) {
    val colors = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary.copy(alpha = .8f)
    }

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = colors)
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
private fun DifficultyButton(
    text: String, isSelected: Boolean, onClick: () -> Unit
) {
    val colors = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary.copy(alpha = .8f)
    }

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = colors)
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePlayModeTopBar(onShowSettings: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Elegir dificultad",
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            IconButton(onClick = onShowSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ChoosePlayModePreview() {
    QuizzofrenicoTheme {
        ChoosePlayModeScreen(
            navigateToQuizz = { _, _, _ -> },
            difficultSelected = "Fácil",
            onDifficultSelected = {},
            inputValue = "",
            onChangeInput = {},
            navigateToScores = {},
            showSettings = true,
            onShowSettings = {},
            timeSelected = 1,
            onTimeSelected = {}
        )
    }
}
