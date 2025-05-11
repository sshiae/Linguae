package com.example.linguae.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * The base class for the ViewModel implementation
 */
open class BaseViewModel : ViewModel() {

    /**
     * Status of error messages
     */
    var errorMessageState by mutableStateOf<String?>(null)
        private set

    /**
     * Status of loading
     */
    var loadingState by mutableStateOf(false)
        private set

    /**
     * Used to display an error message
     */
    fun showErrorMessage(errorMessage: String?) {
        errorMessageState = errorMessage
    }

    /**
     * Used to clear an error message
     */
    fun clearError() {
        errorMessageState = null
    }

    /**
     * Used to hide the loading
     */
    fun hideLoading() {
        loadingState = false
    }

    /**
     * Used to show the loading
     */
    fun showLoading() {
        loadingState = true
    }
}