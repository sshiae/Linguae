package com.example.linguae.ui.feature.addBook

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.linguae.ui.feature.Button
import com.example.linguae.ui.feature.LoadingBar

@Composable
fun AddBookScreen(
    viewModel: AddBookViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {
    AddBookScreen(
        state = viewModel.state,
        errorMessage = viewModel.errorMessageState,
        setSelectedPdfUri = { uri -> viewModel.setSelectedPdfUri(uri) },
        saveBook = { title -> viewModel.saveBook(title) },
        onClearError = { viewModel.clearError() },
        loadingState = viewModel.loadingState,
        onBackClicked = onBackClicked
    )
}

@Composable
fun AddBookScreen(
    state: AddBookContract.State,
    errorMessage: String?,
    setSelectedPdfUri: (Uri) -> Unit,
    saveBook: (String?) -> Unit,
    onClearError: () -> Unit,
    loadingState: Boolean,
    onBackClicked: () -> Unit
) {
    var title by remember { mutableStateOf("") }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                setSelectedPdfUri(it)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = {
                    Text("Название книги")
                },
                isError = errorMessage != null,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                text = "Выбрать PDF файл",
                onClick = {
                    filePicker.launch(arrayOf("application/pdf"))
                },
                modifier = Modifier.fillMaxWidth()
            )

            state.previewBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Превью",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Button(
            text = "Сохранить книгу",
            onClick = {
                onClearError()
                saveBook(title)
                onBackClicked()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (loadingState) {
        LoadingBar()
    }

    errorMessage?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}