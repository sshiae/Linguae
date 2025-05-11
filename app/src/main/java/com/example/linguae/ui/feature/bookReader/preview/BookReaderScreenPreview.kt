package com.example.linguae.ui.feature.bookReader.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.linguae.domain.model.Book
import com.example.linguae.generateUUID
import com.example.linguae.ui.feature.bookReader.BookReaderContract
import com.example.linguae.ui.feature.bookReader.BookReaderScreen
import com.example.linguae.ui.feature.bookReader.TranslationResultDialog

private val fakeMainState = BookReaderContract.MainState(
    book = Book(
        id = generateUUID(),
        title = "test",
        filePath = "/data/user/0/com.example.app/files/books/sample.pdf",
        previewPath = "/data/user/0/com.example.app/files/books/sample.pdf"
    )
)

private val fakeBookState = BookReaderContract.TranslationState(
    text = "Hello World",
    translation = "Привет Мир"
)

@Preview(showBackground = true)
@Composable
fun BookReaderScreenPreview_WithTranslation() {
    MaterialTheme {
        BookReaderScreen(
            mainState = fakeMainState,
            translationState = fakeBookState,
            errorMessageState = null,
            onClearError = {},
            loadingState = false,
            handleTranslation = { h -> },
            clearTranslation = {},
            onAddToDictionary = {},
            onClickShowDictionary = {i -> }
        )
    }
}

@Preview
@Composable
fun BookReaderScreenPreview_Error() {
    MaterialTheme {
        BookReaderScreen(
            translationState = fakeBookState,
            errorMessageState = "Ошибка загрузки перевода",
            onClearError = {},
            loadingState = false,
            mainState = fakeMainState,
            handleTranslation = { h -> },
            clearTranslation = {},
            onAddToDictionary = {},
            onClickShowDictionary = {i -> }
        )
    }
}

@Preview
@Composable
fun BookReaderScreenPreview_Loading() {
    MaterialTheme {
        BookReaderScreen(
            translationState = fakeBookState,
            errorMessageState = null,
            onClearError = {},
            loadingState = true,
            mainState = fakeMainState,
            handleTranslation = { h -> },
            clearTranslation = {},
            onAddToDictionary = {},
            onClickShowDictionary = {i -> }
        )
    }
}

@Preview
@Composable
fun TranslationDialogPreview() {
    MaterialTheme {
        TranslationResultDialog(
            originalText = "Machine Learning",
            translation = "Машинное обучение",
            onDismiss = {},
            onRetry = {},
            onAddToDictionary = {}
        )
    }
}