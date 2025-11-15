package com.allsoundspro.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AllSoundsProApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize app-level components here
    }
}
