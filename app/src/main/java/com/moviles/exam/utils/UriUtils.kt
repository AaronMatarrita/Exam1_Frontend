package com.moviles.exam.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun Uri.toFile(context: Context): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this) ?: return null
        val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
        FileOutputStream(file).use { output ->
            inputStream.copyTo(output)
        }
        file
    } catch (e: Exception) {
        null
    }
}

fun String?.toUri(): Uri? {
    return this?.let { Uri.parse(it) }
}