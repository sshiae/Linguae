package com.example.linguae.ui.feature.dictionary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.linguae.base.BaseViewModel
import com.example.linguae.domain.interactor.Interactor
import com.example.linguae.ui.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for [DictionaryScreen]
 */
@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val interactor: Interactor,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val bookId: String = checkNotNull(savedStateHandle[NavigationKeys.Arg.BOOK_ID])

    var state by mutableStateOf(
        DictionaryContract.State(
            words = listOf()
        )
    )
        private set

    init {
        updateState()
    }

    /**
     * Loads and observes vocabulary words for the current book. Automatically:
     * - Triggers on ViewModel initialization
     * - Updates state reactively when data changes
     * - Manages loading states and error handling
     *
     * @throws IllegalArgumentException If no book ID exists in navigation arguments
     * @throws NoSuchElementException If book isn't found (implementation-dependent)
     */
    fun updateState() {
        viewModelScope.launch {
            try {
                showLoading()
                interactor.getWordsForBook(bookId).collect { wordList ->
                    state = state.copy(words = wordList)
                    hideLoading()
                }
            } catch (e: Exception) {
                showErrorMessage(e.message!!)
            }
        }
    }

    /**
     * Removes a vocabulary word from persistent storage. Handles:
     * - Loading state during deletion
     * - Success/error feedback
     * - Automatic list updates via [Flow] observation
     *
     * @param id UUID of the word to delete
     * @throws NoSuchElementException If word doesn't exist (implementation-dependent)
     */
    fun deleteBookWord(id: String) {
        viewModelScope.launch {
            try {
                showLoading()
                interactor.deleteBookWord(id)
            } catch (e: Exception) {
                showErrorMessage(e.message!!)
            } finally {
                hideLoading()
            }
        }
    }
}