package com.allstatusstudio.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import java.io.File

object VideoUtils {

    fun getPathFromUri(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(MediaStore.Video.Media.DATA)
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return uri.path
    }

    fun trimVideo(inputPath: String, outputPath: String, startTime: Int, endTime: Int): Boolean {
        val command = "-i $inputPath -ss $startTime -to $endTime -c copy $outputPath"
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }

    fun mergeVideos(inputPaths: List<String>, outputPath: String): Boolean {
        val fileListPath = "${outputPath}_list.txt"
        val fileList = File(fileListPath)
        fileList.writeText(inputPaths.joinToString("\n") { "file '$it'" })

        val command = "-f concat -safe 0 -i $fileListPath -c copy $outputPath"
        val session = FFmpegKit.execute(command)

        fileList.delete()
        return ReturnCode.isSuccess(session.returnCode)
    }

    fun addAudioToVideo(videoPath: String, audioPath: String, outputPath: String): Boolean {
        val command = "-i $videoPath -i $audioPath -c:v copy -c:a aac -map 0:v:0 -map 1:a:0 $outputPath"
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }

    fun createVideoFromImage(imagePath: String, audioPath: String, outputPath: String, duration: Int): Boolean {
        val command = "-loop 1 -i $imagePath -i $audioPath -c:v libx264 -tune stillimage -c:a aac -b:a 192k -pix_fmt yuv420p -shortest -t $duration $outputPath"
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }

    fun rotateVideo(inputPath: String, outputPath: String, degrees: Int): Boolean {
        val transpose = when (degrees) {
            90 -> "1"
            180 -> "2,transpose=2"
            270 -> "2"
            else -> return false
        }

        val command = "-i $inputPath -vf transpose=$transpose -c:a copy $outputPath"
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }

    fun changeVideoSpeed(inputPath: String, outputPath: String, speed: Float): Boolean {
        val command = "-i $inputPath -filter:v setpts=${1/speed}*PTS -filter:a atempo=$speed $outputPath"
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }

    fun cropVideo(inputPath: String, outputPath: String, width: Int, height: Int): Boolean {
        val command = "-i $inputPath -vf crop=$width:$height -c:a copy $outputPath"
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }

    fun addWatermark(videoPath: String, watermarkPath: String, outputPath: String): Boolean {
        val command = "-i $videoPath -i $watermarkPath -filter_complex overlay=10:10 $outputPath"
        val session = FFmpegKit.execute(command)
        return ReturnCode.isSuccess(session.returnCode)
    }
}
