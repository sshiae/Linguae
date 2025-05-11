package com.example.linguae.ui.feature.addBook

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.linguae.base.BaseViewModel
import com.example.linguae.domain.interactor.Interactor
import com.example.linguae.domain.model.Book
import com.example.linguae.generateUUID
import com.example.linguae.utils.PdfHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [AddBookScreen]
 */
@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val interactor: Interactor,
    private val pdfHandler: PdfHandler
) : BaseViewModel() {

    var state by mutableStateOf(
        AddBookContract.State(
            selectedPdfUri = null,
            previewBitmap = null
        )
    )
        private set

    /**
     * Updates the selected PDF URI and generates a thumbnail preview.
     * Triggers asynchronous preview generation in the IO dispatcher context.
     *
     * @param uri Content URI of the selected PDF file.
     * @throws SecurityException If URI access permission isn't granted (implementation-dependent).
     */
    fun setSelectedPdfUri(uri: Uri) {
        state = state.copy(
            selectedPdfUri = uri
        )
        generatePreview(uri)
    }

    private fun generatePreview(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(
                previewBitmap = pdfHandler.generatePdfPreview(uri)
            )
        }
    }

    /**
     * Saves a new book with PDF processing and validation. Handles:
     * - Input validation (title + PDF selection)
     * - PDF and preview persistence
     * - Book metadata storage
     * - Loading state management
     *
     * @param title Non-nullable book title (user input)
     * @throws Exception Propagates errors with user-friendly messages:
     *         - "Введите название" if title is null/empty
     *         - "Выберите файл" if no PDF selected
     *         - File system errors from [pdfHandler]
     *         - Network/database errors from [interactor]
     */
    fun saveBook(title: String?) {
        viewModelScope.launch {
            try {
                showLoading()
                checkBeforeSaveBook(title)
                state.selectedPdfUri?.let { uri ->
                    val (pdfPath, previewPath) = pdfHandler.savePdfWithPreview(uri)
                    val book = Book(
                        id = generateUUID(),
                        title = title!!,
                        filePath = pdfPath,
                        previewPath = previewPath
                    )
                    interactor.saveBook(book)
                }
            } catch (e: Exception) {
                showErrorMessage(e.message!!)
            } finally {
                hideLoading()
            }
        }
    }

    private fun checkBeforeSaveBook(title: String?) {
        if (title == null) {
            throw Exception("Введите название")
        }

        if (state.selectedPdfUri == null) {
            throw Exception("Выберите файл")
        }
    }
}