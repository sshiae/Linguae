package com.example.linguae.di

import com.example.linguae.data.local.LocalRepository
import com.example.linguae.data.local.LocalRepositoryImpl
import com.example.linguae.data.remote.RemoteRepository
import com.example.linguae.data.remote.RemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module for binds repositories
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @[Binds Singleton]
    abstract fun bindLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository

    @[Binds Singleton]
    abstract fun bindRemoteRepository(
        remoteRepositoryImpl: RemoteRepositoryImpl
    ): RemoteRepository
}