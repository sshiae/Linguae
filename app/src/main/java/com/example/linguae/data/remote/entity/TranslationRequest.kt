package com.example.linguae.data.remote.entity

/**
 * Model for request translation
 */
data class TranslationRequest(
    val model: String = "GigaChat-Pro",
    val messages: List<Message>
) {
    data class Message(
        val role: String,
        val content: String
    )
}