package com.example.linguae.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entity for book
 */
@Entity(tableName = "books")
data class BookDatabase(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "file_path")
    val filePath: String,
    @ColumnInfo(name = "preview_path")
    val previewPath: String
)