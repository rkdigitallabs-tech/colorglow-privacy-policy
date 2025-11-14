package com.allstatusstudio.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.allstatusstudio.R
import com.allstatusstudio.databinding.ActivityMainBinding
import com.allstatusstudio.utils.AdsUtils
import com.allstatusstudio.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adsUtils: AdsUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        adsUtils = AdsUtils(this)

        setupUI()
        loadAds()
        observeViewModel()
    }

    private fun setupUI() {
        // Status Saver Card
        binding.cardWhatsApp.setOnClickListener {
            startActivity(Intent(this, WhatsAppActivity::class.java))
        }

        // Gallery Card
        binding.cardGallery.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }

        // Editor Card
        binding.cardEditor.setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java))
        }

        // Templates Card
        binding.cardTemplates.setOnClickListener {
            startActivity(Intent(this, TemplatesActivity::class.java))
        }

        // Captions Card
        binding.cardCaptions.setOnClickListener {
            startActivity(Intent(this, CaptionActivity::class.java))
        }

        // Scheduler Card
        binding.cardScheduler.setOnClickListener {
            startActivity(Intent(this, SchedulerActivity::class.java))
        }

        // Cleaner Card
        binding.cardCleaner.setOnClickListener {
            startActivity(Intent(this, CleanerActivity::class.java))
        }

        // Vault Card
        binding.cardVault.setOnClickListener {
            startActivity(Intent(this, VaultActivity::class.java))
        }

        // Settings Icon
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // FAB - Create Status
        binding.fabCreate.setOnClickListener {
            showCreateDialog()
        }
    }

    private fun loadAds() {
        adsUtils.loadBanner(binding.adView)
    }

    private fun showCreateDialog() {
        val options = arrayOf(
            "Create from Template",
            "Edit Photo",
            "Edit Video",
            "Create Video from Photo"
        )

        MaterialAlertDialogBuilder(this)
            .setTitle("Create New Status")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> startActivity(Intent(this, TemplatesActivity::class.java))
                    1, 2, 3 -> {
                        val intent = Intent(this, EditorActivity::class.java)
                        intent.putExtra("mode", which)
                        startActivity(intent)
                    }
                }
            }
            .show()
    }

    private fun observeViewModel() {
        viewModel.dailyPack.observe(this) { pack ->
            binding.tvDailyCaption.text = pack.caption
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDailyPack()
    }
}
