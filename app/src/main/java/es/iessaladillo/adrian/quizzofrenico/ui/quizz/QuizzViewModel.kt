package es.iessaladillo.adrian.quizzofrenico.ui.quizz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iessaladillo.adrian.quizzofrenico.data.Question
import es.iessaladillo.adrian.quizzofrenico.data.QuizzSettings
import es.iessaladillo.adrian.quizzofrenico.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizzViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    private val _questions: MutableStateFlow<List<Question>> = MutableStateFlow(emptyList())
    val questions: StateFlow<List<Question>>
        get() = _questions.asStateFlow()

    private val _currentQuestion: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentQuestion: StateFlow<Int>
        get() = _currentQuestion.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _selectedAnswer: MutableStateFlow<String> = MutableStateFlow("")
    val selectedAnswer: StateFlow<String>
        get() = _selectedAnswer.asStateFlow()

    private val _isAnswerCorrect: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAnswerCorrect: StateFlow<Boolean>
        get() = _isAnswerCorrect.asStateFlow()
    


    // Retrieve the settings from the saved state handle
    private val settings = savedStateHandle.toRoute<QuizzSettings>()


    init {
        viewModelScope.launch {
            _isLoading.value = true
            _questions.value = repository.generateQuizz(settings.topic, settings.difficulty)
            _isLoading.value = false
            println(_questions.value)
        }
    }

    fun onAnswerSelected(option:String)  {
        _selectedAnswer.value = option
    }

    fun onNextQuestion() {
        _isAnswerCorrect.value = _selectedAnswer.value == questions.value[currentQuestion.value].correctAnswer

        
        if (_currentQuestion.value < _questions.value.size - 1) {
            _currentQuestion.value++
        }
    }

    fun isLastQuestion(): Boolean {
        return _currentQuestion.value == _questions.value.size - 1
    }

    fun onBackPressed() {
        // Handle back press logic here
    }

}