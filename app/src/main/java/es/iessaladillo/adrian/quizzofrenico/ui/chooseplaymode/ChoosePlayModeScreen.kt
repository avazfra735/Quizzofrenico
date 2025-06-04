package es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoosePlayModeScreen(
    navigateToQuizz: (String, String) -> Unit,
    difficultSelected: String,
    onDifficultSelected: (String) -> Unit,
    inputValue: String,
    onChangeInput: (String) -> Unit,
    navigateToScores: () -> Unit,
    showSettings: Boolean,
    onShowSettings: () -> Unit
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
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
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
                                color = Color.Black
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
                        }
                    }
                }
            }
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
fun ChoosePlayModeTopBar(onShowSettings: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Elegir dificultad") },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF6a1b9a),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        actions = {
            IconButton(onClick = onShowSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        }
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
        showSettings = true,
        onShowSettings = { }
    )
}