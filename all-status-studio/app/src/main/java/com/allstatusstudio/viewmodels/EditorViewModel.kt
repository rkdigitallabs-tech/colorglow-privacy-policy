package com.allstatusstudio.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allstatusstudio.utils.VideoUtils
import com.allstatusstudio.utils.ImageUtils
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val _isProcessing = MutableLiveData<Boolean>()
    val isProcessing: LiveData<Boolean> = _isProcessing

    private val _exportSuccess = MutableLiveData<Boolean>()
    val exportSuccess: LiveData<Boolean> = _exportSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> = _progress

    private var currentMediaUri: Uri? = null
    private var isVideo = false

    fun loadImage(uri: Uri) {
        currentMediaUri = uri
        isVideo = false
    }

    fun loadVideo(uri: Uri) {
        currentMediaUri = uri
        isVideo = true
    }

    fun loadAudio(uri: Uri) {
        // Store audio URI for photo+audio merge
    }

    fun openTrimTool() {
        // Open trim interface
    }

    fun openMergeTool() {
        // Open merge interface
    }

    fun openMusicTool() {
        // Open music picker
    }

    fun openTextTool() {
        // Open text editor
    }

    fun openStickerTool() {
        // Open sticker picker
    }

    fun openFrameTool() {
        // Open frame picker
    }

    fun rotateMedia() {
        viewModelScope.launch {
            if (isVideo) {
                rotateVideo()
            } else {
                rotateImage()
            }
        }
    }

    private suspend fun rotateImage() {
        withContext(Dispatchers.IO) {
            // Use ImageUtils to rotate
        }
    }

    private suspend fun rotateVideo() {
        withContext(Dispatchers.IO) {
            // Use FFmpeg to rotate
        }
    }

    fun openCropTool() {
        // Open crop interface
    }

    fun openSpeedTool() {
        // Open speed control
    }

    fun openFiltersTool() {
        // Open filters gallery
    }

    fun exportMedia(quality: Int) {
        _isProcessing.value = true
        viewModelScope.launch {
            try {
                val output = withContext(Dispatchers.IO) {
                    if (isVideo) {
                        exportVideo(quality)
                    } else {
                        exportImage(quality)
                    }
                }
                _exportSuccess.value = output != null
            } catch (e: Exception) {
                _error.value = "Export failed: ${e.message}"
                _exportSuccess.value = false
            } finally {
                _isProcessing.value = false
            }
        }
    }

    private fun exportVideo(quality: Int): String? {
        val inputPath = VideoUtils.getPathFromUri(getApplication(), currentMediaUri!!)
        val outputPath = "${getApplication<Application>().getExternalFilesDir(null)}/edited/video_${System.currentTimeMillis()}.mp4"

        val resolution = when (quality) {
            0 -> "1280:720"
            1 -> "1920:1080"
            2 -> "3840:2160"
            else -> "1280:720"
        }

        val command = "-i $inputPath -vf scale=$resolution -c:v libx264 -preset fast -crf 23 $outputPath"

        val session = FFmpegKit.execute(command)
        return if (ReturnCode.isSuccess(session.returnCode)) {
            outputPath
        } else {
            null
        }
    }

    private fun exportImage(quality: Int): String? {
        // Use ImageUtils to export
        return ImageUtils.exportImage(getApplication(), currentMediaUri!!, quality)
    }
}
