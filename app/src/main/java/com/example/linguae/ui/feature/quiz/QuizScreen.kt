package com.example.linguae.ui.feature.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.linguae.ui.feature.Button

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    QuizScreen(
        state = viewModel.state,
        onFinish = onFinish,
        onAnswerChanged = viewModel::onAnswerChanged,
        nextWord = viewModel::nextWord,
        checkAnswer = viewModel::checkAnswer
    )
}

@Composable
fun QuizScreen(
    state: QuizContract.State,
    onFinish: () -> Unit,
    onAnswerChanged: (String) -> Unit,
    nextWord: () -> Unit,
    checkAnswer: () -> Unit
) {
    if (state.isFinished) {
        LaunchedEffect(Unit) {
            onFinish()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Слово ${state.currentIndex + 1} из ${state.totalWords}",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = state.currentWord?.original ?: "",
                    style = MaterialTheme.typography.displayMedium
                )

                OutlinedTextField(
                    value = state.userAnswer,
                    onValueChange = onAnswerChanged,
                    label = { Text("Перевод") },
                    enabled = !state.isAnswerChecked,
                    isError = state.isAnswerWrong,
                    colors = TextFieldDefaults.colors(
                        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                        errorTextColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )

                if (state.isAnswerChecked) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (state.isAnswerWrong) {
                            Text(
                                text = "Правильный ответ: ${state.currentWord?.translation}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Button(
                            text = "Следующее слово",
                            onClick = nextWord
                        )
                    }
                } else {
                    Button(
                        text = "Проверить",
                        onClick = checkAnswer,
                        enabled = state.userAnswer.isNotBlank()
                    )
                }
            }
        }
    }
}