package com.example.linguae.ui.feature.dictionary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.linguae.domain.model.BookWord
import com.example.linguae.ui.feature.ErrorAlertDialog
import com.example.linguae.ui.feature.LoadingBar
import com.example.linguae.ui.feature.NoDataPlug
import com.example.linguae.ui.feature.SwipeToDeleteContainer
import java.util.concurrent.TimeUnit

@Composable
fun DictionaryScreen(
    viewModel: DictionaryViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.updateState()
    }

    DictionaryScreen(
        state = viewModel.state,
        onDelete = { id ->
            viewModel.deleteBookWord(id)
        },
        errorMessageState = viewModel.errorMessageState,
        onClearError = {
            viewModel.clearError()
        },
        loadingState = viewModel.loadingState
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DictionaryScreen(
    state: DictionaryContract.State,
    onDelete: (String) -> Unit,
    errorMessageState: String?,
    onClearError: () -> Unit,
    loadingState: Boolean
) {
    val words: List<BookWord> = state.words

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (words.isEmpty()) {
            NoDataPlug(
                modifier = Modifier.fillMaxSize(),
                title = "Ваш словарь пуст",
                subtitle = "Добавляйте слова при чтении"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(words) { index, word ->
                    SwipeToDeleteContainer(
                        onDelete = { onDelete(word.id) },
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        WordItem(word = word)
                    }
                    if (index < words.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }

        if (loadingState) {
            LoadingBar()
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
private fun WordItem(word: BookWord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Translate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = word.original,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = word.translation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "Добавлено ${getRelativeTimeSpan(word.timestamp)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Icon(
            imageVector = Icons.Default.Book,
            contentDescription = "Из книги",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun getRelativeTimeSpan(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val duration = now - timestamp
    return when {
        duration < 60_000 -> "только что"
        duration < 3_600_000 -> "${TimeUnit.MILLISECONDS.toMinutes(duration)} мин. назад"
        duration < 86_400_000 -> "${TimeUnit.MILLISECONDS.toHours(duration)} ч. назад"
        else -> "${TimeUnit.MILLISECONDS.toDays(duration)} дн. назад"
    }
}