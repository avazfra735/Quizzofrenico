package es.iessaladillo.adrian.quizzofrenico.ui.scores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.iessaladillo.adrian.quizzofrenico.data.Repository
import es.iessaladillo.adrian.quizzofrenico.data.Score
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoresViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _scores: MutableStateFlow<List<Score>> = MutableStateFlow(emptyList())
    val scores: StateFlow<List<Score>>
        get() = _scores.asStateFlow()

    init {
        viewModelScope.launch {
            _scores.value = repository.getQuizz()
        }
    }
}