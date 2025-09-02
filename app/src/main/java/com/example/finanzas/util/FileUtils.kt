package com.example.finanzas.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "purchase_${UUID.randomUUID()}.jpg"
        val file = File(context.filesDir, fileName)
        val outputStream = file.outputStream()
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        Uri.fromFile(file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
