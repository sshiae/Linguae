package com.example.linguae.ui.feature.quiz

import com.example.linguae.domain.model.BookWord

/**
 * Contract for quiz screen
 */
class QuizContract {

    /**
     * State
     */
    data class State(
        val words: List<BookWord> = emptyList(),
        val currentIndex: Int = 0,
        val currentWord: BookWord? = null,
        val userAnswer: String = "",
        val isAnswerChecked: Boolean = false,
        val isAnswerWrong: Boolean = false,
        val correctAnswers: Int = 0,
        val totalWords: Int = 0,
        val isFinished: Boolean = false
    )
}