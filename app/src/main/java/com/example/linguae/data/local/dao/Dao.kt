package com.example.linguae.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.linguae.data.local.entity.BookDatabase
import com.example.linguae.data.local.entity.BookWordDatabase
import kotlinx.coroutines.flow.Flow

/**
 * DAO to work with the database
 */
@Dao
interface Dao {
    @Query("SELECT * FROM books")
    fun getBooks(): Flow<List<BookDatabase>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBook(id: String): BookDatabase

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(book: BookDatabase)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBook(id: String)

    @Query("SELECT * FROM book_words WHERE book_id = :bookId")
    fun getWordsForBook(bookId: String): Flow<List<BookWordDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: BookWordDatabase)

    @Query("DELETE FROM book_words WHERE id = :id")
    suspend fun deleteBookWord(id: String)
}