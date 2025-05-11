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
 * PDF handler
 */
class PdfHandler @Inject constructor(
    @ApplicationContext val context: Context
) {

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
        } catch (e: Exception) {
            null
        }
    }

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