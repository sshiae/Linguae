package com.example.linguae.domain.interactor

import com.example.linguae.data.local.LocalRepository
import com.example.linguae.data.remote.RemoteRepository
import com.example.linguae.domain.model.Book
import com.example.linguae.domain.model.BookWord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor for interacting with various repositories
 */
class Interactor @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) {
    fun getBooks(): Flow<List<Book>> {
        return localRepository.getBooks()
    }

    suspend fun getBook(id: String): Book {
        return localRepository.getBook(id)
    }

    suspend fun deleteBook(id: String) {
        localRepository.deleteBook(id)
    }

    suspend fun saveBook(book: Book) {
        localRepository.saveBook(book)
    }

    suspend fun translate(text: String): String {
        return remoteRepository.translate(text)
    }

    fun getWordsForBook(bookId: String): Flow<List<BookWord>> {
        return localRepository.getWordsForBook(bookId)
    }

    suspend fun insert(word: BookWord) {
        localRepository.insert(word)
    }

    suspend fun deleteBookWord(id: String) {
        localRepository.deleteBookWord(id)
    }
}