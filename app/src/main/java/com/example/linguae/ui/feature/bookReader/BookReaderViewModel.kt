package com.example.linguae.ui.feature.bookReader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.linguae.base.BaseViewModel
import com.example.linguae.domain.interactor.Interactor
import com.example.linguae.domain.model.Book
import com.example.linguae.domain.model.BookWord
import com.example.linguae.generateUUID
import com.example.linguae.ui.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [BookReaderScreen]
 */
@HiltViewModel
class BookReaderViewModel @Inject constructor(
    private val interactor: Interactor,
    private val stateHandle: SavedStateHandle
) : BaseViewModel() {

    var mainState by mutableStateOf(
        BookReaderContract.MainState(
            book = null
        )
    )
        private set

    var translationState by mutableStateOf(
        BookReaderContract.TranslationState(
            text = "",
            translation = ""
        )
    )
        private set

    /**
     * Updates state
     */
    fun updateState() {
        viewModelScope.launch {
            try {
                showLoading()
                val itemId = stateHandle.get<String>(NavigationKeys.Arg.BOOK_ID)
                    ?: throw IllegalArgumentException("No book id was passed to destination.")
                val book: Book = interactor.getBook(itemId)
                mainState = mainState.copy(book = book)
            } catch (e: Exception) {
                showErrorMessage(e.message)
            } finally {
                hideLoading()
            }
        }
    }

    /**
     * Handle text selection
     */
    fun handleTextSelection(
        text: String
    ) {
        viewModelScope.launch {
            try {
                showLoading()
                val response = interactor.translate(text)
                translationState = translationState.copy(
                    translation = response,
                    text = text
                )
            } catch (e: Exception) {
                showErrorMessage(e.message)
            } finally {
                hideLoading()
            }
        }
    }

    /**
     * Clear translation
     */
    fun clearTranslation() {
        translationState = translationState.copy(
            text = "",
            translation = ""
        )
    }

    /**
     * Used to add current translation to dictionary
     */
    fun addCurrentTranslationToDictionary() {
        viewModelScope.launch {
            try {
                showLoading()
                val currentBook: Book = mainState.book!!
                val bookWord = BookWord(
                    id = generateUUID(),
                    bookId = currentBook.id,
                    original = translationState.text,
                    translation = translationState.translation,
                    timestamp = System.currentTimeMillis()
                )
                interactor.insert(bookWord)
            } catch (e: Exception) {
                showErrorMessage(e.message!!)
            } finally {
                hideLoading()
            }
        }
    }
}