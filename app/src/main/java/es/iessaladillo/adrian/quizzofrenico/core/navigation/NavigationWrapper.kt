package es.iessaladillo.adrian.quizzofrenico.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode.ChoosePlayMode
import es.iessaladillo.adrian.quizzofrenico.ui.home.HomeScreen
import es.iessaladillo.adrian.quizzofrenico.ui.login.LoginScreen
import es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode.ChoosePlayModeViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.login.LoginViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.quizz.QuizzScreen
import es.iessaladillo.adrian.quizzofrenico.ui.quizz.QuizzViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.register.RegisterScreen
import es.iessaladillo.adrian.quizzofrenico.ui.register.RegisterViewModel
import es.iessaladillo.adrian.quizzofrenico.ui.result.ResultScreen

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
            val navigateToLogin = { navController.navigate(Login) }
            val email = registerViewModel.email.collectAsStateWithLifecycle()
            val password = registerViewModel.password.collectAsStateWithLifecycle()
            val confPassword = registerViewModel.confPassword.collectAsStateWithLifecycle()
            val showPassword = registerViewModel.showPassword.collectAsStateWithLifecycle()
            val errorMessage = registerViewModel.errorMessage.collectAsStateWithLifecycle()
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
                onRegister, navigateToLogin
            )
        }

        composable<Login> { entry ->
            val loginViewModel: LoginViewModel = hiltViewModel(entry)
            val navigateToRegister = { navController.navigate(Register) }
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
                onErrorMessageChange
            )
        }

        composable<ChoosePlayMode> { entry ->
            val choosePlayModeViewModel: ChoosePlayModeViewModel = hiltViewModel(entry)
            val navigateToQuizz: (String, String) -> Unit = { topic, difficulty ->
                navController.navigate(Quizz(topic, difficulty))
            }
            val inputValue = choosePlayModeViewModel.inputValue.collectAsStateWithLifecycle()
            val onChangeInput: (String) -> Unit = { choosePlayModeViewModel.onChangeInput(it) }
            val onDifficultSelected: (String) -> Unit =
                { choosePlayModeViewModel.onDifficultSelected(it) }
            val difficultSelected =
                choosePlayModeViewModel.onDifficultSelected.collectAsStateWithLifecycle()
            ChoosePlayMode(
                navigateToQuizz,
                difficultSelected.value,
                onDifficultSelected,
                inputValue.value,
                onChangeInput,
            )
        }

        composable<Quizz> { entry ->
            val quizzViewModel: QuizzViewModel = hiltViewModel(entry)
            val navigateToResult = { navController.navigate(Result) }
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
            QuizzScreen(
                questions.value,
                onAnswerSelected,
                onNextQuestion,
                currentQuestionIndex.value,
                isLastQuestion,
                navigateToResult,
                isLoading.value,
                selectedAnswer.value,
                optColors.value
            )
        }

        composable<Result> { entry ->

            ResultScreen()
        }

    }

}

