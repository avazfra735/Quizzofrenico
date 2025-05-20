package es.iessaladillo.adrian.quizzofrenico.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
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
            val onRegister: (String, String) -> Unit = { email, password ->
                registerViewModel.register(email, password)
            }
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
            val navigateToQuizz: (String, String) -> Unit = { topic, difficulty ->
                navController.navigate(Quizz(topic, difficulty))
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
            ChoosePlayModeScreen(
                navigateToQuizz,
                difficultSelected.value,
                onDifficultSelected,
                inputValue.value,
                onChangeInput,
                navigateToScores
            )
        }

        composable<Scores> { entry ->
            val scoresViewModel: ScoresViewModel = hiltViewModel(entry)
            val scores = scoresViewModel.scores.collectAsStateWithLifecycle()
            val navigateBack: () -> Unit = { navController.popBackStack() }
            //val onDeleteScore: (String) -> Unit = { scoresViewModel.deleteScore(it) }
            ScoresScreen(scores.value, navigateBack)

        }

        composable<Quizz> { entry ->
            val quizzViewModel: QuizzViewModel = hiltViewModel(entry)
            val topic = quizzViewModel.settings.topic
            val difficulty = quizzViewModel.settings.difficulty
            val answers = quizzViewModel.answers.collectAsStateWithLifecycle()
            //Tenemos que pasar el mapa a String porque ha dia de hoy solo toma valores simples
            val navigateToResult: (String, String, Map<String, Boolean>) -> Unit =
                { topic, difficulty, answers ->
                    val jsonAnswers = Json.encodeToString(answers)
                    navController.navigate(
                        Result(
                            topic,
                            difficulty,
                            jsonAnswers
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
                timer.value
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

