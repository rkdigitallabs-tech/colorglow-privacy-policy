package com.allstatusstudio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allstatusstudio.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

class CleanerViewModel(application: Application) : AndroidViewModel(application) {

    private val _isScanning = MutableLiveData<Boolean>()
    val isScanning: LiveData<Boolean> = _isScanning

    private val _junkFiles = MutableLiveData<List<JunkFile>>()
    val junkFiles: LiveData<List<JunkFile>> = _junkFiles

    private val _cleanSuccess = MutableLiveData<Boolean>()
    val cleanSuccess: LiveData<Boolean> = _cleanSuccess

    data class JunkFile(
        val path: String,
        val name: String,
        val size: Long,
        val type: String
    )

    fun scanDuplicates() {
        _isScanning.value = true
        viewModelScope.launch {
            val duplicates = withContext(Dispatchers.IO) {
                findDuplicateFiles()
            }
            _junkFiles.value = duplicates
            _isScanning.value = false
        }
    }

    fun scanLargeFiles() {
        _isScanning.value = true
        viewModelScope.launch {
            val largeFiles = withContext(Dispatchers.IO) {
                findLargeFiles()
            }
            _junkFiles.value = largeFiles
            _isScanning.value = false
        }
    }

    fun scanOldEdits() {
        _isScanning.value = true
        viewModelScope.launch {
            val oldFiles = withContext(Dispatchers.IO) {
                findOldEditedFiles()
            }
            _junkFiles.value = oldFiles
            _isScanning.value = false
        }
    }

    private fun findDuplicateFiles(): List<JunkFile> {
        val files = mutableListOf<JunkFile>()
        val hashes = mutableMapOf<String, String>()

        val savedDir = File(getApplication<Application>().getExternalFilesDir(null), "saved")
        savedDir.listFiles()?.forEach { file ->
            val hash = calculateMD5(file)
            if (hashes.containsKey(hash)) {
                files.add(JunkFile(file.absolutePath, file.name, file.length(), "Duplicate"))
            } else {
                hashes[hash] = file.absolutePath
            }
        }

        return files
    }

    private fun findLargeFiles(): List<JunkFile> {
        val files = mutableListOf<JunkFile>()
        val savedDir = File(getApplication<Application>().getExternalFilesDir(null), "saved")

        savedDir.listFiles()?.forEach { file ->
            if (file.length() > 50 * 1024 * 1024) { // 50MB
                files.add(JunkFile(file.absolutePath, file.name, file.length(), "Large File"))
            }
        }

        return files
    }

    private fun findOldEditedFiles(): List<JunkFile> {
        val files = mutableListOf<JunkFile>()
        val editedDir = File(getApplication<Application>().getExternalFilesDir(null), "edited")
        val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)

        editedDir.listFiles()?.forEach { file ->
            if (file.lastModified() < thirtyDaysAgo) {
                files.add(JunkFile(file.absolutePath, file.name, file.length(), "Old Edit"))
            }
        }

        return files
    }

    private fun calculateMD5(file: File): String {
        val digest = MessageDigest.getInstance("MD5")
        file.inputStream().use { input ->
            val buffer = ByteArray(8192)
            var bytesRead = input.read(buffer)
            while (bytesRead != -1) {
                digest.update(buffer, 0, bytesRead)
                bytesRead = input.read(buffer)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    fun cleanJunkFiles() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _junkFiles.value?.forEach { junkFile ->
                    File(junkFile.path).delete()
                }
            }
            _cleanSuccess.value = true
            _junkFiles.value = emptyList()
        }
    }
}
