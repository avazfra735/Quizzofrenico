package es.iessaladillo.adrian.quizzofrenico

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import es.iessaladillo.adrian.quizzofrenico.core.navigation.NavigationWrapper
import es.iessaladillo.adrian.quizzofrenico.ui.theme.QuizzofrenicoTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizzofrenicoTheme {
                NavigationWrapper()
            }
        }
    }
}



