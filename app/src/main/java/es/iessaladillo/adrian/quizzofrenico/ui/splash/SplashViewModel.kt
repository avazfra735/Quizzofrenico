package es.iessaladillo.adrian.quizzofrenico.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iessaladillo.adrian.quizzofrenico.data.AuthState
import es.iessaladillo.adrian.quizzofrenico.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _uiState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val uiState: StateFlow<AuthState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch{
            _uiState.value = repository.isUserLoggedIn()
        }
    }

}