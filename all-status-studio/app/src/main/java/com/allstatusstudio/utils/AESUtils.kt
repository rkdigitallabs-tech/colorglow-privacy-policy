package com.allstatusstudio.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESUtils {

    private const val ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val KEY_SIZE = 256

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(KEY_SIZE, SecureRandom())
        return keyGenerator.generateKey()
    }

    private fun getKeyFromPassword(password: String): SecretKey {
        val keyBytes = password.padEnd(32, '0').substring(0, 32).toByteArray()
        return SecretKeySpec(keyBytes, "AES")
    }

    fun encryptFile(sourceFile: File, destFile: File) {
        try {
            val key = getKeyFromPassword("AllStatusStudio2024SecureVault")
            val cipher = Cipher.getInstance(ALGORITHM)

            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            val ivSpec = IvParameterSpec(iv)

            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)

            FileInputStream(sourceFile).use { input ->
                FileOutputStream(destFile).use { output ->
                    // Write IV first
                    output.write(iv)

                    val buffer = ByteArray(8192)
                    var bytesRead: Int

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        val encrypted = cipher.update(buffer, 0, bytesRead)
                        if (encrypted != null) {
                            output.write(encrypted)
                        }
                    }

                    val finalBytes = cipher.doFinal()
                    if (finalBytes != null) {
                        output.write(finalBytes)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun decryptFile(encryptedFile: File, destFile: File) {
        try {
            val key = getKeyFromPassword("AllStatusStudio2024SecureVault")
            val cipher = Cipher.getInstance(ALGORITHM)

            FileInputStream(encryptedFile).use { input ->
                // Read IV
                val iv = ByteArray(16)
                input.read(iv)
                val ivSpec = IvParameterSpec(iv)

                cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)

                FileOutputStream(destFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int

                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        val decrypted = cipher.update(buffer, 0, bytesRead)
                        if (decrypted != null) {
                            output.write(decrypted)
                        }
                    }

                    val finalBytes = cipher.doFinal()
                    if (finalBytes != null) {
                        output.write(finalBytes)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
