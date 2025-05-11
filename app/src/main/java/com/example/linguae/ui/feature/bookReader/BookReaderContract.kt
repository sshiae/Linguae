package com.example.linguae.ui.feature.bookReader

import com.example.linguae.domain.model.Book

/**
 * Book reader contract
 */
class BookReaderContract {

    /**
     * Main state
     */
    data class MainState(
        val book: Book?
    )

    /**
     * Translation state
     */
    data class TranslationState(
        val text: String,
        val translation: String
    )
}