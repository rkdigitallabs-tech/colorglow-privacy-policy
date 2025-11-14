package com.allstatusstudio.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.allstatusstudio.R
import com.allstatusstudio.databinding.ActivitySettingsBinding
import com.allstatusstudio.viewmodels.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManagerFactory

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        setupUI()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Theme
        binding.itemTheme.setOnClickListener {
            showThemeDialog()
        }

        // Language
        binding.itemLanguage.setOnClickListener {
            showLanguageDialog()
        }

        // Change App Lock PIN
        binding.itemChangePin.setOnClickListener {
            startActivity(Intent(this, AppLockActivity::class.java).apply {
                putExtra("mode", "set")
            })
        }

        // Change Vault PIN
        binding.itemChangeVaultPin.setOnClickListener {
            startActivity(Intent(this, AppLockActivity::class.java).apply {
                putExtra("mode", "set_vault")
            })
        }

        // Notification Settings
        binding.itemNotifications.setOnClickListener {
            openNotificationSettings()
        }

        // Accessibility
        binding.itemAccessibility.setOnClickListener {
            showAccessibilityDialog()
        }

        // Backup & Restore
        binding.itemBackup.setOnClickListener {
            viewModel.createBackup()
        }

        binding.itemRestore.setOnClickListener {
            viewModel.restoreBackup()
        }

        // Privacy Policy
        binding.itemPrivacy.setOnClickListener {
            showHtmlPolicy("privacy_policy.html")
        }

        // Terms of Service
        binding.itemTerms.setOnClickListener {
            showHtmlPolicy("terms.html")
        }

        // DMCA
        binding.itemDmca.setOnClickListener {
            showHtmlPolicy("dmca.html")
        }

        // Rate App
        binding.itemRate.setOnClickListener {
            requestInAppReview()
        }

        // Share App
        binding.itemShare.setOnClickListener {
            shareApp()
        }

        // About
        binding.itemAbout.setOnClickListener {
            showAboutDialog()
        }
    }

    private fun showThemeDialog() {
        val themes = arrayOf("Dark", "Pure Black", "Auto")
        val currentTheme = viewModel.getCurrentTheme()

        MaterialAlertDialogBuilder(this)
            .setTitle("Choose Theme")
            .setSingleChoiceItems(themes, currentTheme) { dialog, which ->
                viewModel.setTheme(which)
                applyTheme(which)
                dialog.dismiss()
            }
            .show()
    }

    private fun applyTheme(theme: Int) {
        when (theme) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English", "Hindi (हिंदी)", "Spanish (Español)")

        MaterialAlertDialogBuilder(this)
            .setTitle("Choose Language")
            .setItems(languages) { _, which ->
                viewModel.setLanguage(which)
                recreate()
            }
            .show()
    }

    private fun openNotificationSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, packageName)
        startActivity(intent)
    }

    private fun showAccessibilityDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Accessibility Options")
            .setMultiChoiceItems(
                arrayOf("Large Text", "High Contrast"),
                booleanArrayOf(false, false)
            ) { _, _, _ -> }
            .setPositiveButton("Apply", null)
            .show()
    }

    private fun showHtmlPolicy(fileName: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("file:///android_asset/html/$fileName")
        startActivity(intent)
    }

    private fun requestInAppReview() {
        val reviewManager = ReviewManagerFactory.create(this)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(this, reviewInfo)
            }
        }
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Check out All Status Studio app!")
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("About")
            .setMessage("All Status Studio v1.0.0\n\nCreate, Save & Repost Like a Pro")
            .setPositiveButton("OK", null)
            .show()
    }
}
