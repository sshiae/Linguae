package com.example.linguae.common

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.linguae.common.Database.Companion.DATABASE_VERSION
import com.example.linguae.data.local.dao.Dao
import com.example.linguae.data.local.entity.BookDatabase
import com.example.linguae.data.local.entity.BookWordDatabase

/**
 * Database for the application
 */
@TypeConverters(DateConverter::class)
@Database(
    version = DATABASE_VERSION,
    entities = [
        BookDatabase::class,
        BookWordDatabase::class
    ]
)
abstract class Database : RoomDatabase() {
    abstract fun dao(): Dao

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "LinguaeDatabase"
    }
}