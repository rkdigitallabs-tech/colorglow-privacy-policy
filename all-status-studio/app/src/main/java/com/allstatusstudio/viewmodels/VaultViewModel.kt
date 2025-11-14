package com.allstatusstudio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allstatusstudio.utils.AESUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class VaultViewModel(application: Application) : AndroidViewModel(application) {

    private val _vaultFiles = MutableLiveData<List<VaultFile>>()
    val vaultFiles: LiveData<List<VaultFile>> = _vaultFiles

    data class VaultFile(
        val name: String,
        val path: String,
        val size: Long,
        val isVideo: Boolean
    )

    fun loadVaultFiles() {
        viewModelScope.launch {
            val files = withContext(Dispatchers.IO) {
                val vaultDir = File(getApplication<Application>().getExternalFilesDir(null), "vault")
                if (!vaultDir.exists()) {
                    vaultDir.mkdirs()
                }

                vaultDir.listFiles()?.map { file ->
                    VaultFile(
                        name = file.name,
                        path = file.absolutePath,
                        size = file.length(),
                        isVideo = file.extension == "enc.mp4"
                    )
                } ?: emptyList()
            }
            _vaultFiles.value = files
        }
    }

    fun addToVault(filePath: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sourceFile = File(filePath)
                val vaultDir = File(getApplication<Application>().getExternalFilesDir(null), "vault")
                val encryptedFile = File(vaultDir, "${sourceFile.nameWithoutExtension}.enc.${sourceFile.extension}")

                AESUtils.encryptFile(sourceFile, encryptedFile)
                sourceFile.delete()
            }
            loadVaultFiles()
        }
    }

    fun removeFromVault(vaultFile: VaultFile) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val encFile = File(vaultFile.path)
                val savedDir = File(getApplication<Application>().getExternalFilesDir(null), "saved")
                val decryptedFile = File(savedDir, vaultFile.name.replace(".enc.", "."))

                AESUtils.decryptFile(encFile, decryptedFile)
                encFile.delete()
            }
            loadVaultFiles()
        }
    }
}
