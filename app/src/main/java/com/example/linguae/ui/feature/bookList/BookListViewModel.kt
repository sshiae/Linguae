package com.example.linguae.ui.feature.bookList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.linguae.base.BaseViewModel
import com.example.linguae.domain.interactor.Interactor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [BookListScreen]
 */
@HiltViewModel
class BookListViewModel @Inject constructor(
    private val interactor: Interactor
) : BaseViewModel() {

    var state by mutableStateOf(
        BookListContract.State(
            items = listOf()
        )
    )
        private set

    init {
        updateState()
    }

    /**
     * Refreshes the book list from the data source and updates the UI state.
     * Automatically handles loading states and error notifications.
     *
     * Triggers:
     * 1. Initial load during ViewModel initialization
     * 2. Manual refresh requests
     *
     * @throws Exception Propagates errors from data layer with messages:
     *         - Network/database errors from [interactor.getBooks]
     *         - Data parsing/validation failures
     */
    fun updateState() {
        viewModelScope.launch {
            try {
                showLoading()
                interactor.getBooks().collect { bookList ->
                    state = state.copy(items = bookList)
                    hideLoading()
                }
            } catch (e: Exception) {
                showErrorMessage(e.message!!)
            }
        }
    }

    /**
     * Deletes a book by its unique identifier. Handles:
     * - Loading state management during operation
     * - Success/error feedback
     * - Automatic state updates through [Flow] observation
     *
     * @param id UUID of the book to remove
     * @throws Exception Propagates deletion errors with messages:
     *         - "Book not found" if invalid ID
     *         - Permission/IO errors from [interactor.deleteBook]
     */
    fun deleteBook(id: String) {
        viewModelScope.launch {
            try {
                showLoading()
                interactor.deleteBook(id)
            } catch (e: Exception) {
                showErrorMessage(e.message!!)
            } finally {
                hideLoading()
            }
        }
    }
}