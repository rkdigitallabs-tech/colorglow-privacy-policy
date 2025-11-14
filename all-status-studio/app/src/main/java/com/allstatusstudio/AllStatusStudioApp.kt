package com.allstatusstudio

import android.app.Application
import com.allstatusstudio.utils.FileUtils
import com.allstatusstudio.utils.NotificationUtils

class AllStatusStudioApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize app directories
        FileUtils.createAppDirectories(this)

        // Create notification channel
        NotificationUtils.createNotificationChannel(this)
    }
}
