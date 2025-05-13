package com.example.linguae.ui.feature.dictionary.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.linguae.domain.model.BookWord
import com.example.linguae.generateUUID
import com.example.linguae.ui.feature.dictionary.DictionaryContract
import com.example.linguae.ui.feature.dictionary.DictionaryScreen

@Preview(showBackground = true)
@Composable
fun DictionaryScreenPreview() {
    MaterialTheme {
        DictionaryScreen(
            state = DictionaryContract.State(
                words = listOf(
                    BookWord(
                        id = generateUUID(),
                        bookId = generateUUID(),
                        original = "Butterfly",
                        translation = "Бабочка",
                        timestamp = System.currentTimeMillis() - 120_000
                    ),
                    BookWord(
                        id = generateUUID(),
                        bookId = generateUUID(),
                        original = "Serendipity",
                        translation = "Удачное стечение обстоятельств",
                        timestamp = System.currentTimeMillis() - 3_600_000
                    )
                )
            ),
            onDelete = {},
            errorMessageState = null,
            onClearError = {},
            loadingState = false,
            onStartLearning = { _ -> },
            bookId = generateUUID()
        )
    }
}