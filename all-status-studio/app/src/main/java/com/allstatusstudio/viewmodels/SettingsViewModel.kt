package com.allstatusstudio.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getCurrentTheme(): Int {
        return prefs.getInt("theme", 0)
    }

    fun setTheme(theme: Int) {
        prefs.edit().putInt("theme", theme).apply()
    }

    fun setLanguage(language: Int) {
        val langCode = when (language) {
            1 -> "hi"
            2 -> "es"
            else -> "en"
        }
        prefs.edit().putString("language", langCode).apply()
    }

    fun createBackup() {
        // Create ZIP of database and preferences
        val backupFile = File(getApplication<Application>().getExternalFilesDir(null), "backup.zip")
        ZipOutputStream(backupFile.outputStream()).use { zip ->
            // Add database
            val dbFile = getApplication<Application>().getDatabasePath("app_database")
            if (dbFile.exists()) {
                zip.putNextEntry(ZipEntry("database.db"))
                dbFile.inputStream().copyTo(zip)
                zip.closeEntry()
            }

            // Add preferences
            val prefsFile = File(getApplication<Application>().dataDir, "shared_prefs/app_prefs.xml")
            if (prefsFile.exists()) {
                zip.putNextEntry(ZipEntry("prefs.xml"))
                prefsFile.inputStream().copyTo(zip)
                zip.closeEntry()
            }
        }
    }

    fun restoreBackup() {
        // Restore from ZIP
        val backupFile = File(getApplication<Application>().getExternalFilesDir(null), "backup.zip")
        if (backupFile.exists()) {
            ZipInputStream(backupFile.inputStream()).use { zip ->
                var entry = zip.nextEntry
                while (entry != null) {
                    when (entry.name) {
                        "database.db" -> {
                            val dbFile = getApplication<Application>().getDatabasePath("app_database")
                            zip.copyTo(dbFile.outputStream())
                        }
                        "prefs.xml" -> {
                            val prefsFile = File(getApplication<Application>().dataDir, "shared_prefs/app_prefs.xml")
                            zip.copyTo(prefsFile.outputStream())
                        }
                    }
                    entry = zip.nextEntry
                }
            }
        }
    }
}
