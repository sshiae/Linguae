package com.example.linguae.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.core.graphics.createBitmap
import com.example.linguae.generateUUID
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * Handles PDF file operations including preview generation and storage management.
 * Manages internal file storage structure for books and previews.
 */
class PdfHandler @Inject constructor(
    @ApplicationContext val context: Context
) {
    /**
     * Generates a bitmap preview of the first page of a PDF document.
     * Scales the page to double its original dimensions for better visibility.
     *
     * @param uri Content URI of the PDF file to preview
     * @return [Bitmap] of the first page, or `null` if:
     *         - File cannot be opened
     *         - PDF rendering fails
     *         - Insufficient memory
     * @throws SecurityException If URI access permission isn't granted
     */
    fun generatePdfPreview(uri: Uri): Bitmap? {
        return try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
            val pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
            val page = pdfRenderer.openPage(0)
            val bitmap = createBitmap(page.width * 2, page.height * 2)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()
            pdfRenderer.close()
            bitmap
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Saves PDF file to internal storage and generates its preview image.
     * Creates following directory structure:
     * - `filesDir/books/` for PDFs
     * - `filesDir/previews/` for preview images
     *
     * @param uri Content URI of source PDF file
     * @return Pair containing:
     *         - First: Absolute path to saved PDF file
     *         - Second: Absolute path to generated preview image
     * @throws SecurityException If URI access permission isn't granted
     * @throws IllegalArgumentException For invalid URI
     */
    fun savePdfWithPreview(uri: Uri): Pair<String, String> {
        val pdfPath = saveToInternalStorage(uri)
        val previewPath = saveBitmapToFile(generatePdfPreview(uri)!!)
        return pdfPath to previewPath
    }

    private fun saveToInternalStorage(
        uri: Uri
    ): String {
        val file = File(context.filesDir, "books/${generateUUID()}.pdf")
        file.parentFile?.mkdirs()

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    private fun saveBitmapToFile(bitmap: Bitmap): String {
        val file = File(context.filesDir, "previews/${generateUUID()}.png")
        file.parentFile?.mkdirs()

        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)
        }
        return file.absolutePath
    }
}