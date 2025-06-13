package es.iessaladillo.adrian.quizzofrenico.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.iessaladillo.adrian.quizzofrenico.data.AuthState
import es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode.ChoosePlayModeScreen
import es.iessaladillo.adrian.quizzofrenico.ui.home.HomeScreen
import es.iessaladillo.adrian.quizzofrenico.ui.login.LoginScreen
import es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode.ChoosePlayModeViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.login.LoginViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.quizz.QuizzScreen
import es.iessaladillo.adrian.quizzofrenico.ui.quizz.QuizzViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.register.RegisterScreen
import es.iessaladillo.adrian.quizzofrenico.ui.register.RegisterViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.result.ResultScreen
import es.iessaladillo.adrian.quizzofrenico.ui.result.ResultViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.scores.ScoresScreen
import es.iessaladillo.adrian.quizzofrenico.ui.scores.ScoresViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.splash.SplashViewModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val timeDuration = 600 // Duración de la animación en milisegundos
    NavHost(
        navController = navController,
        startDestination = Splash,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(timeDuration)
            ) + fadeIn(animationSpec = tween(timeDuration))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(timeDuration)
            ) + fadeOut(animationSpec = tween(timeDuration))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(timeDuration)
            ) + fadeIn(animationSpec = tween(timeDuration))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(timeDuration)
            ) + fadeOut(animationSpec = tween(timeDuration))
        }) {

        composable<Splash> { entry ->
            val splashViewModel: SplashViewModel = hiltViewModel(entry)
            val uiState = splashViewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(uiState.value) {// Observa el estado de autenticación
                when (uiState.value) {
                    is AuthState.Authenticated -> navController.navigate(ChoosePlayMode) {
                        popUpTo(Splash) { inclusive = true }
                    }

                    is AuthState.Unauthenticated -> navController.navigate(Home) {
                        popUpTo(Splash) { inclusive = true }
                    }

                    is AuthState.Loading -> {}
                }
            }

        }
        composable<Home> { entry ->
            val navigateToLogin = { navController.navigate(Login) }
            val navigateToRegister = { navController.navigate(Register) }
            HomeScreen(navigateToLogin, navigateToRegister)
        }

        composable<Register> { entry ->
            val registerViewModel: RegisterViewModel = hiltViewModel(entry)
            val navigateToLogin = {
                navController.popBackStack()
                navController.navigate(Login)
            }
            val email = registerViewModel.email.collectAsStateWithLifecycle()
            val password = registerViewModel.password.collectAsStateWithLifecycle()
            val confPassword = registerViewModel.confPassword.collectAsStateWithLifecycle()
            val showPassword = registerViewModel.showPassword.collectAsStateWithLifecycle()
            val errorMessage = registerViewModel.errorMessage.collectAsStateWithLifecycle()
            val isLoading = registerViewModel.isLoading.collectAsStateWithLifecycle()
            val onChangeEmail: (String) -> Unit = { registerViewModel.onEmailChange(it) }
            val onChangePassword: (String) -> Unit = { registerViewModel.onPasswordChange(it) }
            val onChangeConfPassword: (String) -> Unit =
                { registerViewModel.onConfPasswordChange(it) }
            val onShowPassword = { registerViewModel.onPasswordVisible() }
            val onErrorMessageChange: (String) -> Unit =
                { registerViewModel.onErrorMessageChange(it) }
            val onRegister =
                { registerViewModel.register { navController.navigate(ChoosePlayMode) } }

            RegisterScreen(
                email.value, onChangeEmail,
                password.value, onChangePassword,
                confPassword.value, onChangeConfPassword,
                showPassword.value, onShowPassword,
                errorMessage.value, onErrorMessageChange,
                onRegister, navigateToLogin, isLoading.value
            )
        }

        composable<Login> { entry ->
            val loginViewModel: LoginViewModel = hiltViewModel(entry)
            val navigateToRegister = {
                navController.popBackStack()
                navController.navigate(Register)
            }
            val email = loginViewModel.email.collectAsStateWithLifecycle()
            val password = loginViewModel.password.collectAsStateWithLifecycle()
            val authenticate =
                { loginViewModel.login { navController.navigate(ChoosePlayMode) } }
            val onChangeEmail: (String) -> Unit = { loginViewModel.onEmailChange(it) }
            val onChangePassword: (String) -> Unit = { loginViewModel.onPasswordChange(it) }
            val showPassword = loginViewModel.showPassword.collectAsStateWithLifecycle()
            val errorMessage = loginViewModel.errorMessage.collectAsStateWithLifecycle()
            val onShowPassword = { loginViewModel.togglePasswordVisibility() }
            val onErrorMessageChange: (String) -> Unit = { loginViewModel.errorMessage(it) }
            val isLoading = loginViewModel.isLoading.collectAsStateWithLifecycle()
            LoginScreen(
                email.value,
                password.value,
                authenticate,
                navigateToRegister,
                onChangeEmail,
                onChangePassword,
                showPassword.value,
                errorMessage.value,
                onShowPassword,
                onErrorMessageChange,
                isLoading.value
            )
        }

        composable<ChoosePlayMode> { entry ->
            val choosePlayModeViewModel: ChoosePlayModeViewModel = hiltViewModel(entry)
            val navigateToQuizz: (String, String, Int) -> Unit = { topic, difficulty, time ->
                navController.navigate(Quizz(topic, difficulty, time))
            }
            val navigateToScores: () -> Unit = {
                navController.navigate(Scores)
            }
            val inputValue = choosePlayModeViewModel.inputValue.collectAsStateWithLifecycle()
            val onChangeInput: (String) -> Unit = { choosePlayModeViewModel.onChangeInput(it) }
            val onDifficultSelected: (String) -> Unit =
                { choosePlayModeViewModel.onDifficultSelected(it) }
            val difficultSelected =
                choosePlayModeViewModel.onDifficultSelected.collectAsStateWithLifecycle()
            val showSettings = choosePlayModeViewModel.showSettings.collectAsStateWithLifecycle()
            val onShowSettings = { choosePlayModeViewModel.onShowSettings() }
            val time = choosePlayModeViewModel.time.collectAsStateWithLifecycle()
            val onTimeSelected: (Int) -> Unit =
                { choosePlayModeViewModel.onTimeSelected(it) }
            val onLogOut: () -> Unit = {
                choosePlayModeViewModel.onLogOut { navController.navigate(Home) }
            }
            ChoosePlayModeScreen(
                navigateToQuizz,
                difficultSelected.value,
                onDifficultSelected,
                inputValue.value,
                onChangeInput,
                navigateToScores,
                showSettings.value,
                onShowSettings,
                time.value,
                onTimeSelected,
                onLogOut
            )
        }

        composable<Scores> { entry ->
            val scoresViewModel: ScoresViewModel = hiltViewModel(entry)
            val scores = scoresViewModel.scores.collectAsStateWithLifecycle()
            val navigateBack: () -> Unit = { navController.popBackStack() }
            ScoresScreen(scores.value, navigateBack)

        }

        composable<Quizz> { entry ->
            val quizzViewModel: QuizzViewModel = hiltViewModel(entry)
            val topic = quizzViewModel.settings.topic
            val difficulty = quizzViewModel.settings.difficulty
            val answers = quizzViewModel.answers.collectAsStateWithLifecycle()
            val timerResult = quizzViewModel.timer.collectAsStateWithLifecycle()
            //Tenemos que pasar el mapa a String porque ha dia de hoy solo toma valores simples
            val navigateToResult: (String, String, Map<String, Boolean>) -> Unit =
                { topic, difficulty, answers ->
                    val jsonAnswers = Json.encodeToString(answers)
                    quizzViewModel.stopTimer() // Detener el temporizador antes de navegar
                    navController.navigate(
                        Result(
                            topic,
                            difficulty,
                            jsonAnswers,
                            timerResult.value
                        )
                    )
                }
            val questions = quizzViewModel.questions.collectAsStateWithLifecycle()
            val currentQuestionIndex = quizzViewModel.currentQuestion.collectAsStateWithLifecycle()
            val onAnswerSelected: (String) -> Unit = { answer ->
                quizzViewModel.onAnswerSelected(answer)
            }
            val onNextQuestion = { quizzViewModel.onNextQuestion() }
            val isLastQuestion = quizzViewModel.isLastQuestion()
            val isLoading = quizzViewModel.isLoading.collectAsStateWithLifecycle()
            val selectedAnswer = quizzViewModel.selectedAnswer.collectAsStateWithLifecycle()
            val optColors = quizzViewModel.optionColors.collectAsStateWithLifecycle()
            val timer = quizzViewModel.timer.collectAsStateWithLifecycle()
            val isTimeUp = quizzViewModel.isTimeUp.collectAsStateWithLifecycle()
            val explanation = quizzViewModel.explanation.collectAsStateWithLifecycle()
            val showExplanation = quizzViewModel.showExplanation.collectAsStateWithLifecycle()
            val onExplanationShown : () -> Unit = { quizzViewModel.onExplanationShown() }
            val onExplanationDismiss = { quizzViewModel.onExplanationDismiss() }
            val error = quizzViewModel.error.collectAsStateWithLifecycle()
            val onErrorDialogDissmiss: () -> Unit =
                { navController.popBackStack(ChoosePlayMode, false) }
            QuizzScreen(
                questions.value,
                onAnswerSelected,
                onNextQuestion,
                currentQuestionIndex.value,
                isLastQuestion,
                navigateToResult,
                isLoading.value,
                selectedAnswer.value,
                optColors.value,
                topic,
                difficulty,
                answers.value,
                timer.value,
                isTimeUp.value,
                explanation.value,
                showExplanation.value,
                onExplanationShown,
                onExplanationDismiss,
                error.value,
                onErrorDialogDissmiss
            )
        }

        composable<Result> { entry ->
            val resultViewModel: ResultViewModel = hiltViewModel(entry)
            val navigateToChoosePlayMode: () -> Unit =
                { navController.popBackStack(ChoosePlayMode, false) }
            val topic = resultViewModel.results.topic
            val difficulty = resultViewModel.results.difficulty
            val answers = resultViewModel.answers
            ResultScreen(
                topic,
                difficulty,
                answers,
                navigateToChoosePlayMode
            )
        }

    }

}

