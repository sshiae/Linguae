package com.example.linguae.data.remote

/**
 * Abstraction for remote data operations, typically handling API communications
 * and network-dependent functionality. Operates with suspending functions for
 * asynchronous execution.
 */
interface RemoteRepository {

    /**
     * Translates text to the Russian language using a remote service.
     * This is a suspend function and must be called from a coroutine context.
     *
     * @param text The input text to be translated. Must be non-empty and in a supported language.
     * @return The Russian translation as a [String].
     * @throws IllegalArgumentException If input text is empty or contains unsupported characters.
     */
    suspend fun translate(text: String): String
}