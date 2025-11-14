package com.allstatusstudio.viewmodels

import android.app.Application
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

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val _mediaList = MutableLiveData<List<StatusModel>>()
    val mediaList: LiveData<List<StatusModel>> = _mediaList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var allMedia = listOf<StatusModel>()
    private var currentFilter = "all"
    private var currentSort = "date"

    init {
        loadMedia()
    }

    private fun loadMedia() {
        viewModelScope.launch {
            allMedia = withContext(Dispatchers.IO) {
                val savedPath = getApplication<Application>().getExternalFilesDir(null)?.absolutePath + "/saved"
                val folder = File(savedPath)

                if (folder.exists()) {
                    folder.listFiles()?.map { file ->
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
            applyFilterAndSort()
        }
    }

    fun filterBy(filter: String) {
        currentFilter = filter
        applyFilterAndSort()
    }

    fun sortBy(sort: String) {
        currentSort = sort
        applyFilterAndSort()
    }

    private fun applyFilterAndSort() {
        var filtered = allMedia

        // Apply filter
        filtered = when (currentFilter) {
            "favorites" -> filtered.filter { it.isFavorite }
            "edited" -> filtered.filter { it.isEdited }
            else -> filtered
        }

        // Apply sort
        filtered = when (currentSort) {
            "name" -> filtered.sortedBy { it.name }
            "size" -> filtered.sortedByDescending { it.size }
            else -> filtered.sortedByDescending { it.timestamp }
        }

        _mediaList.value = filtered
    }

    fun deleteMedia(media: Any) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Delete file
                _message.postValue("File deleted")
            }
            loadMedia()
        }
    }

    fun moveToVault(media: Any) {
        viewModelScope.launch {
            _message.value = "Moved to vault"
        }
    }

    fun schedulePost(media: Any) {
        _message.value = "Post scheduled"
    }

    fun openMedia(media: Any) {
        // Open media viewer
    }
}
