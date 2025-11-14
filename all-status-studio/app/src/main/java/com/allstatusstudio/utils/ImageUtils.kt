package com.allstatusstudio.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {

    fun exportImage(context: Context, uri: Uri, quality: Int): String? {
        return try {
            val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
            val outputDir = File(context.getExternalFilesDir(null), "edited")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }

            val outputFile = File(outputDir, "IMG_${System.currentTimeMillis()}.jpg")
            FileOutputStream(outputFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            }

            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun scaleBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    fun cropBitmap(bitmap: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(bitmap, x, y, width, height)
    }

    fun saveBitmap(bitmap: Bitmap, outputPath: String, quality: Int = 90): Boolean {
        return try {
            FileOutputStream(outputPath).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun applyFilter(bitmap: Bitmap, filterType: String): Bitmap {
        // Apply OpenCV filters here
        // For now, return the original bitmap
        return bitmap
    }

    fun addTextToBitmap(bitmap: Bitmap, text: String, x: Float, y: Float): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = android.graphics.Canvas(mutableBitmap)
        val paint = android.graphics.Paint()
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 48f
        paint.setShadowLayer(5f, 2f, 2f, android.graphics.Color.BLACK)

        canvas.drawText(text, x, y, paint)
        return mutableBitmap
    }
}
