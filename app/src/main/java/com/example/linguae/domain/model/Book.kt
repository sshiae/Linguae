package com.example.linguae.domain.model

/**
 * Model of book
 */
data class Book(
    val id: String,
    val title: String,
    val filePath: String,
    val previewPath: String
)