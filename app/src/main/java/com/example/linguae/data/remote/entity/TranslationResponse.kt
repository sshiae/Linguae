package com.example.linguae.data.remote.entity

/**
 * Model for getting translation response
 */
data class TranslationResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val message: Message
    ) {
        data class Message(
            val content: String
        )
    }
}