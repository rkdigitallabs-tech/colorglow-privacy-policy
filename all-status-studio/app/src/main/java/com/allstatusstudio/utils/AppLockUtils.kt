package com.allstatusstudio.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object AppLockUtils {

    private const val PREFS_NAME = "app_lock_prefs"
    private const val KEY_PIN = "user_pin"
    private const val KEY_VAULT_PIN = "vault_pin"
    private const val KEY_LOCK_ENABLED = "lock_enabled"

    fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    fun savePin(context: Context, pin: String, isVault: Boolean = false) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val encryptedPin = encryptPin(pin)
        val key = if (isVault) KEY_VAULT_PIN else KEY_PIN

        prefs.edit()
            .putString(key, encryptedPin)
            .putBoolean(KEY_LOCK_ENABLED, true)
            .apply()
    }

    fun verifyPin(context: Context, pin: String, isVault: Boolean = false): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val key = if (isVault) KEY_VAULT_PIN else KEY_PIN
        val savedPin = prefs.getString(key, null) ?: return false

        val decryptedPin = decryptPin(savedPin)
        return pin == decryptedPin
    }

    fun isLockEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_LOCK_ENABLED, false)
    }

    private fun encryptPin(pin: String): String {
        // Simple base64 encoding for demonstration
        // In production, use proper encryption
        return android.util.Base64.encodeToString(pin.toByteArray(), android.util.Base64.DEFAULT)
    }

    private fun decryptPin(encryptedPin: String): String {
        return String(android.util.Base64.decode(encryptedPin, android.util.Base64.DEFAULT))
    }

    fun setAutoLockEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean("auto_lock", enabled).apply()
    }

    fun isAutoLockEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean("auto_lock", true)
    }
}
