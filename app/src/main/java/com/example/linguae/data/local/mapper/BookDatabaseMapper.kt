package com.example.linguae.data.local.mapper

import com.example.linguae.data.local.entity.BookDatabase
import com.example.linguae.data.local.entity.BookWordDatabase
import com.example.linguae.domain.model.Book
import com.example.linguae.domain.model.BookWord

fun List<BookDatabase>.toBooks(): List<Book> {
    return map { it.toBook() }
}

fun BookDatabase.toBook(): Book {
    return Book(
        id = id,
        title = title,
        filePath = filePath,
        previewPath = previewPath
    )
}

fun Book.toBookDatabase(): BookDatabase {
    return BookDatabase(
        id = id,
        title = title,
        filePath = filePath,
        previewPath = previewPath
    )
}

fun List<BookWordDatabase>.toBookWords(): List<BookWord> {
    return map { it.toBookWord() }
}

fun BookWordDatabase.toBookWord(): BookWord {
    return BookWord(
        id = id,
        bookId = bookId,
        original = original,
        translation = translation,
        timestamp = timestamp
    )
}

fun BookWord.toBookWordDatabase(): BookWordDatabase {
    return BookWordDatabase(
        id = id,
        bookId = bookId,
        original = original,
        translation = translation,
        timestamp = timestamp
    )
}