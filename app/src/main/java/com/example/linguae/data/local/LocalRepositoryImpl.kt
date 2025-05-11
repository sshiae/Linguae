package com.example.linguae.data.local

import com.example.linguae.data.local.dao.Dao
import com.example.linguae.data.local.mapper.toBook
import com.example.linguae.data.local.mapper.toBookDatabase
import com.example.linguae.data.local.mapper.toBookWordDatabase
import com.example.linguae.data.local.mapper.toBookWords
import com.example.linguae.data.local.mapper.toBooks
import com.example.linguae.domain.model.Book
import com.example.linguae.domain.model.BookWord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [LocalRepository]
 */
class LocalRepositoryImpl @Inject constructor(
    private val dao: Dao
) : LocalRepository {
    override fun getBooks(): Flow<List<Book>> {
        return dao.getBooks().map { it.toBooks() }
    }

    override suspend fun getBook(id: String): Book {
        return dao.getBook(id).toBook()
    }

    override suspend fun saveBook(book: Book) {
        dao.saveBook(book.toBookDatabase())
    }

    override suspend fun deleteBook(id: String) {
        dao.deleteBook(id)
    }

    override fun getWordsForBook(bookId: String): Flow<List<BookWord>> {
        return dao.getWordsForBook(bookId).map { it.toBookWords() }
    }

    override suspend fun insert(word: BookWord) {
        dao.insert(word.toBookWordDatabase())
    }

    override suspend fun deleteBookWord(id: String) {
        dao.deleteBookWord(id)
    }
}