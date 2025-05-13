package com.example.linguae.ui.feature.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.linguae.base.BaseViewModel
import com.example.linguae.domain.interactor.Interactor
import com.example.linguae.ui.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val interactor: Interactor,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val bookId: String = checkNotNull(savedStateHandle[NavigationKeys.Arg.BOOK_ID])

    var state by mutableStateOf(
        QuizContract.State()
    )
        private set

    init {
        loadWords()
    }

    private fun loadWords() {
        viewModelScope.launch {
            interactor.getWordsForBook(bookId).collect { words ->
                val shuffledWords = words.shuffled()
                state = state.copy(
                    words = shuffledWords,
                    totalWords = words.size,
                    currentWord = shuffledWords.first(),
                    currentIndex = 0
                )
            }
        }
    }

    /**
     * Updates the user's answer input in the current quiz state.
     * Automatically trims whitespace from the input value.
     *
     * @param newAnswer The raw text input from the user for the current word translation
     */
    fun onAnswerChanged(newAnswer: String) {
        state = state.copy(userAnswer = newAnswer.trim())
    }

    /**
     * Validates the user's answer against the correct translation.
     * Updates the state to:
     * - Mark the answer as checked
     * - Flag incorrect answers
     * - Increment the correct answers counter if validation passes
     *
     * Uses case-insensitive comparison for translation validation.
     */
    fun checkAnswer() {
        val currentWord = state.currentWord ?: return
        val isCorrect = currentWord.translation.equals(state.userAnswer, true)
        state = state.let {
            it.copy(
                isAnswerChecked = true,
                isAnswerWrong = !isCorrect,
                correctAnswers = if (isCorrect) it.correctAnswers + 1 else it.correctAnswers
            )
        }
    }

    /**
     * Progresses to the next word in the quiz sequence.
     * Handles:
     * - Resetting answer-related state fields
     * - Updating the current word reference
     * - Marking quiz completion when reaching the last word
     *
     * If no more words remain, sets the [QuizContract.State.isFinished] flag to true.
     */
    fun nextWord() {
        state = state.let {
            if (it.currentIndex >= it.words.lastIndex) {
                it.copy(isFinished = true)
            } else {
                it.copy(
                    currentIndex = it.currentIndex + 1,
                    currentWord = it.words.getOrNull(it.currentIndex + 1),
                    userAnswer = "",
                    isAnswerChecked = false,
                    isAnswerWrong = false
                )
            }
        }
    }
}