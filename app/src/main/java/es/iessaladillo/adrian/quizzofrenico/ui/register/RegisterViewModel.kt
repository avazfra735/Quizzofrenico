package es.iessaladillo.adrian.quizzofrenico.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iessaladillo.adrian.quizzofrenico.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String>
        get() = _email.asStateFlow()
    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password: StateFlow<String>
        get() = _password.asStateFlow()
    private val _confPassword: MutableStateFlow<String> = MutableStateFlow("")
    val confPassword: StateFlow<String>
        get() = _confPassword.asStateFlow()
    private val _showPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean>
        get() = _showPassword.asStateFlow()
    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage: StateFlow<String>
        get() = _errorMessage.asStateFlow()


    fun register(email: String, password: String) {
        viewModelScope.launch {
            repository.register(email, password)
        }
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfPasswordChange(newConfPassword: String) {
        _confPassword.value = newConfPassword
    }

    fun onPasswordVisible() {
        _showPassword.value = !_showPassword.value
    }

    fun onErrorMessageChange(errorMessage: String) {
        _errorMessage.value = errorMessage
    }
}