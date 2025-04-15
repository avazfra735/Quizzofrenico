package es.iessaladillo.adrian.quizzofrenico.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iessaladillo.adrian.quizzofrenico.data.Repository
import es.iessaladillo.adrian.quizzofrenico.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> get() = _email.asStateFlow()

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String> get() = _password.asStateFlow()

    private val _showPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> get() = _showPassword.asStateFlow()

    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun togglePasswordVisibility() {
        _showPassword.value = !_showPassword.value
    }

    fun errorMessage(message: String) {
        _errorMessage.value = message
    }


    fun login(onLoginSucces: () -> Unit) {
        viewModelScope.launch {
            sessionManager.userId = repository.authenticate(_email.value, _password.value)
            if (sessionManager.userId != null) {
                onLoginSucces()
            } else{
                _errorMessage.value = "Usuario o contraseña incorrectos"
            }
        }
    }
}