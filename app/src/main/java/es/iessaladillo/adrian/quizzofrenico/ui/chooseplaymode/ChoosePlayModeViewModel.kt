package es.iessaladillo.adrian.quizzofrenico.ui.chooseplaymode

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChoosePlayModeViewModel @Inject constructor() :
    ViewModel() {

    private val _inputValue: MutableStateFlow<String> = MutableStateFlow("")
    val inputValue: StateFlow<String> get() = _inputValue.asStateFlow()

    private val _onDifficultSelected: MutableStateFlow<String> = MutableStateFlow("")
    val onDifficultSelected: StateFlow<String> get() = _onDifficultSelected.asStateFlow()

    private val _showSettings: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSettings: StateFlow<Boolean> get() = _showSettings.asStateFlow()

    fun onChangeInput(newInputValue: String) {
        _inputValue.value = newInputValue
    }

    fun onDifficultSelected(difficult: String) {
        _onDifficultSelected.value = difficult
    }

    fun onShowSettings() {
        _showSettings.value = !_showSettings.value
    }

}