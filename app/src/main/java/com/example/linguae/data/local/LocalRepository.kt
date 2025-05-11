package com.example.linguae.data.local

import com.example.linguae.domain.model.Book
import com.example.linguae.domain.model.BookWord
import kotlinx.coroutines.flow.Flow

/**
 * Local repository abstraction for database operations, providing access to book and vocabulary data.
 * Handles CRUD operations and exposes reactive [Flow] streams for data observation.
 */
interface LocalRepository {

    /**
     * Retrieves a reactive stream of all books in the repository.
     * The [Flow] emits updates automatically when the underlying data changes.
     *
     * @return A [Flow] emitting a list of [Book]. The list may be empty if no books exist.
     */
    fun getBooks(): Flow<List<Book>>

    /**
     * Fetches a specific book by its unique identifier.
     * This is a suspend function and must be called from a coroutine context.
     *
     * @param id The unique identifier of the book to retrieve.
     * @return [Book] object if found.
     * @throws NoSuchElementException If no book with the given ID exists (implementation-dependent).
     */
    suspend fun getBook(id: String): Book

    /**
     * Persists a book entity in the repository. Handles both insert and update operations.
     * This is a suspend function and must be called from a coroutine context.
     *
     * @param book The [Book] entity to save.
     * @throws IllegalArgumentException If the book contains invalid data
     *         (e.g., empty required fields or invalid relationships).
     */
    suspend fun saveBook(book: Book)

    /**
     * Removes a book from the repository by its unique identifier.
     * This is a suspend function and must be called from a coroutine context.
     *
     * @param id The unique identifier of the book to delete.
     * @throws NoSuchElementException If no book with the given ID exists (implementation-dependent).
     */
    suspend fun deleteBook(id: String)

    /**
     * Retrieves a reactive stream of vocabulary words associated with a specific book.
     * The [Flow] emits updates automatically when the underlying data changes.
     *
     * @param bookId The unique identifier of the target book.
     * @return A [Flow] emitting a list of [BookWord]. The list may be empty if no words exist.
     */
    fun getWordsForBook(bookId: String): Flow<List<BookWord>>

    /**
     * Stores a new vocabulary word in the repository.
     * This is a suspend function and must be called from a coroutine context.
     *
     * @param word The [BookWord] entity to persist.
     * @throws IllegalArgumentException If the word contains invalid data
     *         (e.g., empty lemma, missing book association, or invalid metadata).
     */
    suspend fun insert(word: BookWord)

    /**
     * Removes a vocabulary word from the repository by its unique identifier.
     * This is a suspend function and must be called from a coroutine context.
     *
     * @param id The unique identifier of the word to remove.
     * @throws NoSuchElementException If no word with the given ID exists (implementation-dependent).
     */
    suspend fun deleteBookWord(id: String)
}