package com.example.linguae.ui.feature.bookReader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TextSnippet
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.TextSnippet
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.linguae.domain.model.Book
import com.example.linguae.ui.feature.ErrorAlertDialog
import com.example.linguae.ui.feature.LoadingBar
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

@Composable
fun BookReaderScreen(
    viewModel: BookReaderViewModel = hiltViewModel(),
    onClickShowDictionary: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.updateState()
    }

    BookReaderScreen(
        mainState = viewModel.mainState,
        translationState = viewModel.translationState,
        errorMessageState = viewModel.errorMessageState,
        onClearError = { viewModel.clearError() },
        loadingState = viewModel.loadingState,
        handleTranslation = { text ->
            viewModel.handleTextSelection(text)
        },
        clearTranslation = {
            viewModel.clearTranslation()
        },
        onClickShowDictionary = onClickShowDictionary,
        onAddToDictionary = {
            viewModel.addCurrentTranslationToDictionary()
        }
    )
}

@Composable
fun BookReaderScreen(
    mainState: BookReaderContract.MainState,
    translationState: BookReaderContract.TranslationState,
    errorMessageState: String?,
    onClearError: () -> Unit,
    loadingState: Boolean,
    handleTranslation: (String) -> Unit,
    clearTranslation: () -> Unit,
    onClickShowDictionary: (String) -> Unit,
    onAddToDictionary: () -> Unit
) {
    val book: Book? = mainState.book
    val bookPath: String = book?.filePath ?: ""

    if (bookPath.isEmpty()) {
        LoadingBar()
        return
    }

    val pdfFile = remember { File(bookPath) }
    var pdfView by remember { mutableStateOf<PDFView?>(null) }
    var showTranslationInput by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PDFView(ctx, null).apply {
                    fromFile(pdfFile)
                        .enableDoubletap(true)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .load()
                    pdfView = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )

        FloatingActionButton(
            onClick = { showTranslationInput = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Translate,
                contentDescription = "Перевести текст"
            )
        }

        FloatingActionButton(
            onClick = { onClickShowDictionary(book?.id!!) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Icon(Icons.Default.Book, "Словарь")
        }

        if (showTranslationInput) {
            TranslationInputDialog(
                initialText = selectedText,
                onDismiss = { showTranslationInput = false },
                onConfirm = { text ->
                    handleTranslation(text)
                    showTranslationInput = false
                }
            )
        }

        if (loadingState) {
            LoadingBar()
        }

        if (errorMessageState != null) {
            ErrorAlertDialog(
                text = errorMessageState,
                onDismiss = onClearError
            )
        }

        if (translationState.translation.isNotEmpty()) {
            TranslationResultDialog(
                originalText = translationState.text,
                translation = translationState.translation,
                onDismiss = {
                    clearTranslation()
                },
                onRetry = { showTranslationInput = true },
                onAddToDictionary = onAddToDictionary
            )
        }
    }
}

@Composable
private fun TranslationInputDialog(
    initialText: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var inputText by remember { mutableStateOf(initialText) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .widthIn(max = 480.dp)
                .heightIn(max = 640.dp),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Введите текст для перевода",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Текст для перевода") },
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(inputText) },
                        enabled = inputText.isNotBlank()
                    ) {
                        Text("Перевести")
                    }
                }
            }
        }
    }
}

@Composable
fun TranslationResultDialog(
    originalText: String,
    translation: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    onAddToDictionary: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .widthIn(max = 480.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Результат перевода",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TranslationBlock(
                        title = "Оригинал",
                        text = originalText,
                        icon = Icons.Outlined.TextSnippet,
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )

                    TranslationBlock(
                        title = "Перевод",
                        text = translation,
                        icon = Icons.Outlined.Translate,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FilledTonalButton(
                        onClick = {
                            onAddToDictionary()
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Icon(Icons.Outlined.BookmarkAdd, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Сохранить в словарь")
                    }

                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier.fillMaxWidth(),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.dp
                        )
                    ) {
                        Icon(Icons.Outlined.Edit, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Редактировать текст")
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Закрыть")
                    }
                }
            }
        }
    }
}

@Composable
private fun TranslationBlock(
    title: String,
    text: String,
    icon: ImageVector,
    containerColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(containerColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}