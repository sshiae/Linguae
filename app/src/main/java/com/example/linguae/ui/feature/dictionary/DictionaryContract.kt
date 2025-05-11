package com.example.linguae.ui.feature.dictionary

import com.example.linguae.domain.model.BookWord

/**
 * Contract for dictionary
 */
class DictionaryContract {

    /**
     * State
     */
    data class State(
        val words: List<BookWord>
    )
}