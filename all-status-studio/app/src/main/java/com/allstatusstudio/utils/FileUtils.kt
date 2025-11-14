package com.allstatusstudio.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileUtils {

    fun saveStatusToGallery(context: Context, sourcePath: String): Boolean {
        return try {
            val sourceFile = File(sourcePath)
            val savedDir = File(context.getExternalFilesDir(null), "saved")
            if (!savedDir.exists()) {
                savedDir.mkdirs()
            }

            val destFile = File(savedDir, "STATUS_${System.currentTimeMillis()}.${sourceFile.extension}")
            sourceFile.copyTo(destFile, overwrite = true)

            // Add to media scanner
            scanMedia(context, destFile.absolutePath)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun shareFile(context: Context, filePath: String) {
        try {
            val file = File(filePath)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = when (file.extension.lowercase()) {
                "mp4" -> "video/*"
                "jpg", "jpeg", "png", "gif" -> "image/*"
                else -> "*/*"
            }
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            context.startActivity(Intent.createChooser(intent, "Share via"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }

    fun getFileSize(filePath: String): Long {
        return File(filePath).length()
    }

    fun copyFile(source: File, dest: File) {
        FileInputStream(source).use { input ->
            FileOutputStream(dest).use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun scanMedia(context: Context, filePath: String) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(File(filePath))
        context.sendBroadcast(intent)
    }

    fun createAppDirectories(context: Context) {
        val dirs = listOf("saved", "edited", "vault", "templates", "logs")
        dirs.forEach { dir ->
            val folder = File(context.getExternalFilesDir(null), dir)
            if (!folder.exists()) {
                folder.mkdirs()
            }
        }
    }
}
