package com.example.finanzas.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException
import java.util.UUID

fun saveImageToInternalStorage(context: Context, uri: Uri): Result<Uri> {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: return Result.failure(IOException("Could not open input stream for the selected URI."))

        val fileName = "purchase_${UUID.randomUUID()}.jpg"
        val file = File(context.filesDir, fileName)

        val outputStream = file.outputStream()
        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        Result.success(Uri.fromFile(file))
    } catch (e: Exception) {
        e.printStackTrace()
        Result.failure(e)
    }
}
