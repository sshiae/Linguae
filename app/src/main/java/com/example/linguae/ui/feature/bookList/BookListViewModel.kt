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
     * Updates state
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
     * Delete a book
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