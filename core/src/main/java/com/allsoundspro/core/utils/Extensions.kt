package com.allsoundspro.core.utils

import android.content.Context
import android.widget.Toast

/**
 * Common extension functions used across the app
 */

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun String.isValidUrl(): Boolean {
    return this.startsWith("http://") || this.startsWith("https://")
}
