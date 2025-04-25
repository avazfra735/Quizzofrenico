package es.iessaladillo.adrian.quizzofrenico.ui.quizz

import androidx.compose.ui.graphics.Color
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

    private val _optionColors: MutableStateFlow<Map<String, Color>> = MutableStateFlow(emptyMap())
    val optionColors: StateFlow<Map<String, Color>>
        get() = _optionColors.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _selectedAnswer: MutableStateFlow<String> = MutableStateFlow("")
    val selectedAnswer: StateFlow<String>
        get() = _selectedAnswer.asStateFlow()

    private val _answers: MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(emptyMap())
    val answers: StateFlow<Map<String, Boolean>>
        get() = _answers.asStateFlow()


    // Retrieve the settings from the saved state handle
    val settings = savedStateHandle.toRoute<QuizzSettings>()


    init {
        viewModelScope.launch {
            _isLoading.value = true
            _questions.value = repository.generateQuizz(settings.topic, settings.difficulty)
            _isLoading.value = false
            println(_questions.value)
        }
    }

    fun onAnswerSelected(option: String) {
        // Actualizar el estado de la respuesta seleccionada
        _selectedAnswer.value = option.substringBefore(")")
        backgroundColors(option)
        scoreUp(option)
        println(_answers.value)
    }

    fun onNextQuestion() {
        _selectedAnswer.value = ""
        if (_currentQuestion.value < _questions.value.size - 1) {
            _currentQuestion.value++
        }
    }

    fun isLastQuestion(): Boolean {
        return _currentQuestion.value == _questions.value.size - 1
    }

    private fun backgroundColors(option: String) {
        val currentQ = _questions.value[_currentQuestion.value]
        val correctAnswer = currentQ.correctAnswer

        val selectedLetter = option.substringBefore(")")

        // Calcular los colores para cada opción
        val newColors = currentQ.options.associateWith { opt ->
            val letter = opt.substringBefore(")")
            when {
                letter == correctAnswer -> Color.Green
                letter == selectedLetter && letter != correctAnswer -> Color.Red
                else -> Color.Transparent
            }
        }

        // Actualizar el estado de los colores
        _optionColors.value = newColors
    }

    private fun scoreUp(option: String) {
        // Actualizar el estado de las respuestas correctas
        val currentQ = _questions.value[_currentQuestion.value]
        val correctAnswer = currentQ.correctAnswer
        val selectedLetter = option.substringBefore(")")
        val isCorrect = selectedLetter == correctAnswer
        _answers.value =
            answers.value + ("Pregunta " + (_currentQuestion.value + 1) to isCorrect)

    }
}