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
     * Using for set selected pdf uri
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
     * Save a book
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