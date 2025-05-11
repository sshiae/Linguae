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
 * View model for [DictionaryScreen]
 */
@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val interactor: Interactor,
    private val stateHandle: SavedStateHandle
) : BaseViewModel() {

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
     * Updates state
     */
    fun updateState() {
        viewModelScope.launch {
            try {
                showLoading()
                val itemId = stateHandle.get<String>(NavigationKeys.Arg.BOOK_ID)
                    ?: throw IllegalArgumentException("No book id was passed to destination.")
                interactor.getWordsForBook(itemId).collect { wordList ->
                    state = state.copy(words = wordList)
                    hideLoading()
                }
            } catch (e: Exception) {
                showErrorMessage(e.message!!)
            }
        }
    }

    /**
     * Delete a book word
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