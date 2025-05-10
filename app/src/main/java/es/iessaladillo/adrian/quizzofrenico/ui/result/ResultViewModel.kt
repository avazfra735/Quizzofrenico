package es.iessaladillo.adrian.quizzofrenico.ui.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iessaladillo.adrian.quizzofrenico.data.QuizzResult
import es.iessaladillo.adrian.quizzofrenico.data.Repository
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject


@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    val results = savedStateHandle.toRoute<QuizzResult>()
    val answers: Map<String, Boolean> = Json.decodeFromString(results.answers)

    init {
        viewModelScope.launch {
            repository.saveQuizz(results.topic, results.difficulty,0)
        }
    }
}