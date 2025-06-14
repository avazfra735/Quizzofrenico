package es.iessaladillo.adrian.quizzofrenico.ui.quizz

import android.annotation.SuppressLint
import android.os.CountDownTimer
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

    private val _showExplanation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showExplanation: StateFlow<Boolean>
        get() = _showExplanation.asStateFlow()

    private val _explanation: MutableStateFlow<String> = MutableStateFlow("")
    val explanation: StateFlow<String>
        get() = _explanation.asStateFlow()

    private val _explanationPending: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val explanationPending: StateFlow<Boolean>
        get() = _explanationPending.asStateFlow()

    private val _timer: MutableStateFlow<String> = MutableStateFlow("")
    val timer: StateFlow<String>
        get() = _timer.asStateFlow()

    private val _isTimeUp: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTimeUp: StateFlow<Boolean>
        get() = _isTimeUp.asStateFlow()

    private val _error: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val error: StateFlow<Boolean>
        get() = _error.asStateFlow()

    private var countDownTimer: CountDownTimer? = null

    // Retrieve the settings from the saved state handle
    val settings = savedStateHandle.toRoute<QuizzSettings>()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            val generatedQuestions = repository.generateQuizz(settings.topic, settings.difficulty)
            val validQuestions = generatedQuestions.filter { it.options.size >= 2 }
            if (validQuestions.isEmpty()) {
                _error.value = true
            } else {
                _questions.value = validQuestions
            }
            _isLoading.value = false
            if (!_error.value) {
                startTimer(mapTimeToMillis(settings.time))
            }
            println(_questions.value)
        }
    }

    @SuppressLint("DefaultLocale")
    fun startTimer(durationInMillis: Long = 60000) {
        countDownTimer = object : CountDownTimer(durationInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                _timer.value = String.format("%02d:%02d", seconds / 60, seconds % 60)
                println("Tiempo restante: ${_timer.value}")
            }

            // Aquí puedes manejar el evento de tiempo agotado
            override fun onFinish() {
                stopTimer()
                println("Tiempo agotado")
                _isTimeUp.value = true
                markUnasweredQuestions()
            }
        }.start()
    }

    fun stopTimer() {
        countDownTimer?.cancel()
    }

    private fun mapTimeToMillis(time: Int): Long {
        return when (time) {
            1 -> 60000L
            5 -> 300000L
            10 -> 600000L
            else -> 60000L // Valor por defecto si no coincide con ninguna opción
        }
    }

    private fun markUnasweredQuestions() {
        // Marcar las preguntas no respondidas como incorrectas
        val unanswered = _questions.value.drop(_currentQuestion.value)
        val updatedAnswers =
            unanswered.associate { "Pregunta ${_questions.value.indexOf(it) + 1}" to false }
        _answers.value = _answers.value + updatedAnswers
    }

    fun onAnswerSelected(option: String) {
        // Actualizar el estado de la respuesta seleccionada
        val currentQ = _questions.value[_currentQuestion.value]
        val correctAnswer = currentQ.correctAnswer
        val isCorrect = option.substringBefore(")") == correctAnswer

        if (!isCorrect) {
            _explanation.value = currentQ.explanation
        }

        _selectedAnswer.value = option.substringBefore(")")
        backgroundColors(option)
        scoreUp(option)
        println(_answers.value)
    }

    fun onExplanationShown() {
        _showExplanation.value = true
    }

    // Cierra la explicación y resetea el estado
    fun onExplanationDismiss() {
        _showExplanation.value = false
        _explanation.value = ""
    }

    fun onExplanationPending() {
        _explanationPending.value = !_explanationPending.value
    }


    fun onNextQuestion() {
        if (_selectedAnswer.value.isNotEmpty()) { // Verifica si la pregunta ha sido respondida
            if (_currentQuestion.value < _questions.value.size - 1) {
                _selectedAnswer.value = ""
                _currentQuestion.value++
            } else {
                stopTimer() // Detener el temporizador si es la última pregunta
            }
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