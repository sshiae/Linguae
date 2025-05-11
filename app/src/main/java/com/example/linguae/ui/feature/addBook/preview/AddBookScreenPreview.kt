package com.example.linguae.ui.feature.addBook.preview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.createBitmap
import com.example.linguae.ui.feature.addBook.AddBookContract
import com.example.linguae.ui.feature.addBook.AddBookScreen

private fun createPreviewState(
    previewBitmap: Bitmap? = null,
    selectedPdfUri: Uri? = null
) = AddBookContract.State(
    previewBitmap = previewBitmap,
    selectedPdfUri = selectedPdfUri
)

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview_Empty() {
    MaterialTheme {
        AddBookScreen(
            state = createPreviewState(),
            errorMessage = null,
            setSelectedPdfUri = {},
            saveBook = {},
            onClearError = {},
            loadingState = false,
            onBackClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview_WithPreview() {
    MaterialTheme {
        AddBookScreen(
            state = createPreviewState(
                previewBitmap = createTestBitmap()
            ),
            errorMessage = null,
            setSelectedPdfUri = {},
            saveBook = {},
            onClearError = {},
            loadingState = false,
            onBackClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview_ErrorState() {
    MaterialTheme {
        AddBookScreen(
            state = createPreviewState(),
            errorMessage = "Необходимо выбрать файл",
            setSelectedPdfUri = {},
            saveBook = {},
            onClearError = {},
            loadingState = false,
            onBackClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview_Loading() {
    MaterialTheme {
        AddBookScreen(
            state = createPreviewState(),
            errorMessage = null,
            setSelectedPdfUri = {},
            saveBook = {},
            onClearError = {},
            loadingState = true,
            onBackClicked = {}
        )
    }
}

private fun createTestBitmap(): Bitmap {
    val width = 200
    val height = 300
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    return bitmap
}