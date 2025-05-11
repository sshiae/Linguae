package com.example.linguae.ui.feature.addBook

import android.net.Uri
import coil3.Bitmap

/**
 * Contract for add book
 */
class AddBookContract {

    /**
     * State for add book
     */
    data class State(
        val selectedPdfUri: Uri?,
        val previewBitmap: Bitmap?
    )
}