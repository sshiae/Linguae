package com.example.linguae.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity for book word
 */
@Entity(tableName = "book_words")
data class BookWordDatabase(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "book_id")
    val bookId: String,
    @ColumnInfo(name = "original")
    val original: String,
    @ColumnInfo(name = "translation")
    val translation: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
)