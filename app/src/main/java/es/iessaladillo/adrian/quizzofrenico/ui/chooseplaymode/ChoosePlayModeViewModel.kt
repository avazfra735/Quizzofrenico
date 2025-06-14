package es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode

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
class ChoosePlayModeViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val _inputValue: MutableStateFlow<String> = MutableStateFlow("")
    val inputValue: StateFlow<String> get() = _inputValue.asStateFlow()

    private val _onDifficultSelected: MutableStateFlow<String> = MutableStateFlow("")
    val onDifficultSelected: StateFlow<String> get() = _onDifficultSelected.asStateFlow()

    private val _showSettings: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val showSettings: StateFlow<Boolean> get() = _showSettings.asStateFlow()

    private val _time: MutableStateFlow<Int> = MutableStateFlow(0)
    val time: StateFlow<Int> get() = _time.asStateFlow()

    private val _userName: MutableStateFlow<String> = MutableStateFlow("")
    val userName: StateFlow<String> get() = _userName.asStateFlow()

    init {
        _userName.value = repository.getUserName()
    }

    fun onChangeInput(newInputValue: String) {
        _inputValue.value = newInputValue
    }

    fun onDifficultSelected(difficult: String) {
        _onDifficultSelected.value = difficult
    }

    fun onShowSettings() {
        _showSettings.value = !_showSettings.value
    }

    fun onTimeSelected(time: Int) {
        _time.value = time
    }

    fun onLogOut(navigateToHome: () -> Unit) {
        viewModelScope.launch {
            repository.signOut()
            navigateToHome()
        }
    }

}