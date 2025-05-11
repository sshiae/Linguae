package com.example.linguae.di

import android.content.Context
import androidx.room.Room
import com.example.linguae.common.Database
import com.example.linguae.data.local.dao.Dao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for the database
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @[Provides Singleton]
    fun provideRoomDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder<Database>(
            context,
            Database::class.java,
            Database.DATABASE_NAME
        )
            .build()
    }

    @[Provides Singleton]
    fun provideAccutaneDao(database: Database): Dao {
        return database.dao()
    }
}