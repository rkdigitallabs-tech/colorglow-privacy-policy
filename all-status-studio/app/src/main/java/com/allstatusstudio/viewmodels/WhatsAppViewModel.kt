package com.allstatusstudio.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allstatusstudio.data.models.StatusModel
import com.allstatusstudio.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class WhatsAppViewModel(application: Application) : AndroidViewModel(application) {

    private val _statuses = MutableLiveData<List<StatusModel>>()
    val statuses: LiveData<List<StatusModel>> = _statuses

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadStatuses(isWhatsApp: Boolean) {
        viewModelScope.launch {
            try {
                val statusList = withContext(Dispatchers.IO) {
                    val basePath = if (isWhatsApp) {
                        "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"
                    } else {
                        "/storage/emulated/0/Android/media/com.whatsapp.w4b/WhatsApp Business/Media/.Statuses"
                    }

                    val folder = File(basePath)
                    if (folder.exists() && folder.isDirectory) {
                        folder.listFiles()?.filter {
                            it.extension in listOf("jpg", "jpeg", "png", "mp4", "gif")
                        }?.map { file ->
                            StatusModel(
                                path = file.absolutePath,
                                name = file.name,
                                isVideo = file.extension == "mp4",
                                size = file.length(),
                                timestamp = file.lastModified()
                            )
                        } ?: emptyList()
                    } else {
                        emptyList()
                    }
                }
                _statuses.value = statusList
            } catch (e: Exception) {
                _error.value = "Failed to load statuses: ${e.message}"
            }
        }
    }

    fun saveStatus(status: StatusModel) {
        viewModelScope.launch {
            try {
                val saved = withContext(Dispatchers.IO) {
                    FileUtils.saveStatusToGallery(getApplication(), status.path)
                }
                _saveSuccess.value = saved
            } catch (e: Exception) {
                _error.value = "Failed to save: ${e.message}"
            }
        }
    }

    fun shareStatus(status: StatusModel) {
        viewModelScope.launch {
            FileUtils.shareFile(getApplication(), status.path)
        }
    }

    fun toggleFavorite(status: StatusModel) {
        viewModelScope.launch {
            // Toggle favorite in database
        }
    }

    fun previewStatus(status: StatusModel) {
        // Open preview dialog
    }
}
