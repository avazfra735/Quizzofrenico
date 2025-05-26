package es.iessaladillo.adrian.quizzofrenico.ui.scores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.iessaladillo.adrian.quizzofrenico.data.Score

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresScreen(
    scores: List<Score>, // Lista de resultados recuperados de Firestore
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = { ScoresTopBar(onBackClick)   }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(scores.size) { testResult ->
                ScoreItem(scores[testResult]) // Muestra cada resultado
            }
        }
    }
}

@Composable
private fun ScoreItem(testResult: Score) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Tema: ${testResult.topic}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dificultad: ${testResult.difficulty}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Fecha: ${testResult.date}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Resultado: ${testResult.score}/${testResult.total}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = testResult.timer,
                fontSize = 24.sp, // Tamaño grande
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6a1b9a) // Color destacado
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresTopBar(
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Resultados") },
        navigationIcon = {
            IconButton(onClick =  onBackClick ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
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
fun ScoresScreenPreview() {
    ScoresScreen(
        scores = listOf(
            Score("Tema 1", "Fácil", 5, 5,"2023-10-01", "00:30"),
            Score("Tema 2", "Intermedio", 3, 5,"2023-10-02","00:45"),
            Score("Tema 3", "Difícil", 4,5, "2023-10-03", "00:50")
        ),
        onBackClick = {}
    )
}