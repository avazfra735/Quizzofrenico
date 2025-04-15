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

    fun onChangeInput(newInputValue: String) {
        _inputValue.value = newInputValue
    }

    fun onDifficultSelected(difficult: String) {
        _onDifficultSelected.value = difficult
    }

}