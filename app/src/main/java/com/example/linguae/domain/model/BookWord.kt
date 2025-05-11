package com.example.linguae.domain.model

/**
 * Model of book word
 */
data class BookWord(
    val id: String,
    val bookId: String,
    val original: String,
    val translation: String,
    val timestamp: Long
)