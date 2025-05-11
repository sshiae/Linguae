package com.example.linguae.ui.feature.bookList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.linguae.domain.model.Book
import com.example.linguae.ui.feature.ErrorAlertDialog
import com.example.linguae.ui.feature.LoadingBar
import com.example.linguae.ui.feature.NoDataPlug
import com.example.linguae.ui.feature.SwipeToDeleteContainer
import java.io.File

@Composable
fun BookListScreen(
    viewModel: BookListViewModel = hiltViewModel(),
    onClickedAddBook: () -> Unit,
    onClicked: (id: String) -> Unit
) {
    BookList(
        state = viewModel.state,
        onClicked = onClicked,
        onDelete = { id -> viewModel.deleteBook(id) },
        errorMessageState = viewModel.errorMessageState,
        onClearError = { viewModel.clearError() },
        loadingState = viewModel.loadingState,
        onClickedAddBook = onClickedAddBook
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookList(
    modifier: Modifier = Modifier,
    state: BookListContract.State,
    onClicked: (id: String) -> Unit,
    onDelete: (id: String) -> Unit,
    errorMessageState: String?,
    onClearError: () -> Unit,
    loadingState: Boolean,
    onClickedAddBook: () -> Unit
) {
    val bookList: List<Book> = state.items
    val scrollState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        if (bookList.isEmpty()) {
            NoDataPlug(
                modifier = Modifier.align(Alignment.Center),
                title = "Библиотека пуста",
                subtitle = "Добавьте первую книгу для чтения"
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(bookList) { index, book ->
                    SwipeToDeleteContainer(
                        onDelete = { onDelete(book.id) },
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        BookCard(
                            book = book,
                            onClick = { onClicked(book.id) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.BottomEnd),
            onClick = onClickedAddBook,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Добавить книгу"
            )
        }

        if (loadingState) {
            LoadingBar(modifier = Modifier.align(Alignment.Center))
        }

        errorMessageState?.let { errorMessage ->
            ErrorAlertDialog(
                text = errorMessage,
                onDismiss = onClearError
            )
        }
    }
}

@Composable
private fun BookCard(
    book: Book,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BookCover(
                previewPath = book.previewPath
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BookCover(
    previewPath: String
) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = File(previewPath),
            contentDescription = "Обложка книги",
            contentScale = ContentScale.Crop
        )
    }
}