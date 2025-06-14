package es.iessaladillo.adrian.quizzofrenico.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.iessaladillo.adrian.quizzofrenico.R
import es.iessaladillo.adrian.quizzofrenico.data.AuthState
import es.iessaladillo.adrian.quizzofrenico.ui.theme.QuizzofrenicoTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navigateToHome: () -> Unit,
    navigateToChoosePlayMode: () -> Unit,
    uiState: AuthState
) {

    Scaffold { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Transparent,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
                .clickable {
                    when (uiState) {
                        is AuthState.Authenticated -> navigateToChoosePlayMode()
                        is AuthState.Unauthenticated -> navigateToHome()
                        is AuthState.Loading -> {}
                    }
                }
        )
        SplashBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clip(MaterialTheme.shapes.medium) // Recorta las esquinas
                    .background(MaterialTheme.colorScheme.primary) // Aplica el fondo después de clip
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                JumpingLettersText()
            }

        }

    }

}

@Composable
fun JumpingLettersText() {
    val text = "Toca la pantalla para comenzar"
    val animatables = remember { text.map { Animatable(0f) } }

    LaunchedEffect(Unit) {
        while (true) {
            text.indices.forEach { index ->
                launch {
                    animatables[index].animateTo(
                        targetValue = -20f,
                        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                    )
                    animatables[index].animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 200, easing = LinearEasing)
                    )
                }
                delay(50) // Delay para el efecto de ola
            }
            delay(500) // Pausa entre ciclos de ola
        }
    }

    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        text.forEachIndexed { index, char ->
            Text(
                text = char.toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.offset(y = animatables[index].value.dp)
            )
        }
    }
}



@Composable
fun SplashBackground() {
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

@Preview
@Composable
fun SplashScreenPreview() {
    QuizzofrenicoTheme {
        SplashScreen(
            navigateToHome = {},
            navigateToChoosePlayMode = {},
            uiState = AuthState.Loading
        )
    }
}