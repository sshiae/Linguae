package com.example.linguae.ui.feature.quiz.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.linguae.domain.model.BookWord
import com.example.linguae.generateUUID
import com.example.linguae.ui.feature.quiz.QuizContract
import com.example.linguae.ui.feature.quiz.QuizScreen

private val mockWords = listOf(
    BookWord(id = "1", original = "Hello", translation = "Привет", timestamp = 0, bookId = generateUUID()),
    BookWord(id = "2", original = "World", translation = "Мир", timestamp = 0, bookId = generateUUID()),
    BookWord(id = "3", original = "Compose", translation = "Компоновать", timestamp = 0, bookId = generateUUID())
)

@Preview(showBackground = true, name = "Default State")
@Composable
fun QuizScreenPreview_Default() {
    MaterialTheme {
        QuizScreen(
            state = QuizContract.State(
                words = mockWords,
                currentIndex = 0,
                currentWord = mockWords[0],
                userAnswer = "",
                isAnswerChecked = false,
                isAnswerWrong = false,
                correctAnswers = 0,
                totalWords = mockWords.size,
                isFinished = false
            ),
            onFinish = {},
            onAnswerChanged = {},
            nextWord = {},
            checkAnswer = {}
        )
    }
}

@Preview(showBackground = true, name = "Checked Correct Answer")
@Composable
fun QuizScreenPreview_CorrectAnswer() {
    MaterialTheme {
        QuizScreen(
            state = QuizContract.State(
                words = mockWords,
                currentIndex = 0,
                currentWord = mockWords[0],
                userAnswer = "Привет",
                isAnswerChecked = true,
                isAnswerWrong = false,
                correctAnswers = 1,
                totalWords = mockWords.size,
                isFinished = false
            ),
            onFinish = {},
            onAnswerChanged = {},
            nextWord = {},
            checkAnswer = {}
        )
    }
}

@Preview(showBackground = true, name = "Checked Wrong Answer")
@Composable
fun QuizScreenPreview_WrongAnswer() {
    MaterialTheme {
        QuizScreen(
            state = QuizContract.State(
                words = mockWords,
                currentIndex = 0,
                currentWord = mockWords[0],
                userAnswer = "Пока",
                isAnswerChecked = true,
                isAnswerWrong = true,
                correctAnswers = 0,
                totalWords = mockWords.size,
                isFinished = false
            ),
            onFinish = {},
            onAnswerChanged = {},
            nextWord = {},
            checkAnswer = {}
        )
    }
}

@Preview(showBackground = true, name = "Finished Quiz")
@Composable
fun QuizScreenPreview_Finished() {
    MaterialTheme {
        QuizScreen(
            state = QuizContract.State(
                words = mockWords,
                currentIndex = mockWords.lastIndex,
                currentWord = null,
                userAnswer = "",
                isAnswerChecked = false,
                isAnswerWrong = false,
                correctAnswers = 3,
                totalWords = mockWords.size,
                isFinished = true
            ),
            onFinish = {},
            onAnswerChanged = {},
            nextWord = {},
            checkAnswer = {}
        )
    }
}