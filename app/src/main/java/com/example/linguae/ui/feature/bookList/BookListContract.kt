package com.example.linguae.ui.feature.bookList

import com.example.linguae.domain.model.Book

/**
 * Contract for a list of books
 */
class BookListContract {

    /**
     * State for the list of books
     */
    data class State(
        val items: List<Book>
    )
}