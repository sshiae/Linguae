package com.example.linguae.ui.feature.bookList.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.linguae.domain.model.Book
import com.example.linguae.ui.feature.bookList.BookList
import com.example.linguae.ui.feature.bookList.BookListContract

private fun createPreviewState(
    bookList: List<Book>
) = BookListContract.State(
    items = bookList
)

@Preview(showBackground = true)
@Composable
fun BookListPreview() {
    MaterialTheme {
        BookList(
            state = createPreviewState(listOf(
                Book(
                    id = "1",
                    title = "Война и мир",
                    previewPath = "path/to/preview1.jpg",
                    filePath = "path/to/preview1.jpg"
                ),
                Book(
                    id = "2",
                    title = "Преступление и наказание",
                    previewPath = "path/to/preview2.jpg",
                    filePath = "path/to/preview1.jpg"
                )
            )),
            onClicked = {},
            onDelete = {},
            errorMessageState = null,
            onClearError = {},
            loadingState = false,
            onClickedAddBook = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyBookListPreview() {
    MaterialTheme {
        BookList(
            state = createPreviewState(emptyList()),
            onClicked = {},
            onDelete = {},
            errorMessageState = null,
            onClearError = {},
            loadingState = false,
            onClickedAddBook = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingBookListPreview() {
    MaterialTheme {
        BookList(
            state = createPreviewState(listOf(Book("3", "Загрузка...", "", ""))),
            onClicked = {},
            onDelete = {},
            errorMessageState = null,
            onClearError = {},
            loadingState = true,
            onClickedAddBook = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorBookListPreview() {
    MaterialTheme {
        BookList(
            state = createPreviewState(listOf(Book("4", "Ошибка", "", ""))),
            onClicked = {},
            onDelete = {},
            errorMessageState = "Не удалось загрузить данные",
            onClearError = {},
            loadingState = false,
            onClickedAddBook = {}
        )
    }
}