package com.allstatusstudio.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.allstatusstudio.R
import com.allstatusstudio.databinding.ActivityEditorBinding
import com.allstatusstudio.utils.AdsUtils
import com.allstatusstudio.viewmodels.EditorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var viewModel: EditorViewModel
    private lateinit var adsUtils: AdsUtils

    private val PICK_IMAGE = 1
    private val PICK_VIDEO = 2
    private val PICK_AUDIO = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[EditorViewModel::class.java]
        adsUtils = AdsUtils(this)

        setupUI()
        observeViewModel()
        checkIntentData()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        // Import buttons
        binding.btnImportImage.setOnClickListener { pickImage() }
        binding.btnImportVideo.setOnClickListener { pickVideo() }
        binding.btnImportAudio.setOnClickListener { pickAudio() }

        // Video editing tools
        binding.btnTrim.setOnClickListener { viewModel.openTrimTool() }
        binding.btnMerge.setOnClickListener { viewModel.openMergeTool() }
        binding.btnAddMusic.setOnClickListener { viewModel.openMusicTool() }
        binding.btnAddText.setOnClickListener { viewModel.openTextTool() }
        binding.btnAddSticker.setOnClickListener { viewModel.openStickerTool() }
        binding.btnAddFrame.setOnClickListener { viewModel.openFrameTool() }
        binding.btnRotate.setOnClickListener { viewModel.rotateMedia() }
        binding.btnCrop.setOnClickListener { viewModel.openCropTool() }
        binding.btnSpeed.setOnClickListener { viewModel.openSpeedTool() }
        binding.btnFilters.setOnClickListener { viewModel.openFiltersTool() }

        // Export button
        binding.btnExport.setOnClickListener {
            showExportDialog()
        }
    }

    private fun checkIntentData() {
        val mode = intent.getIntExtra("mode", -1)
        val uri = intent.getStringExtra("uri")

        when (mode) {
            1 -> uri?.let { viewModel.loadImage(Uri.parse(it)) }
            2 -> uri?.let { viewModel.loadVideo(Uri.parse(it)) }
            3 -> {
                // Create video from photo
                binding.layoutPhotoVideo.visibility = View.VISIBLE
            }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun pickVideo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        startActivityForResult(intent, PICK_VIDEO)
    }

    private fun pickAudio() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "audio/*"
        startActivityForResult(intent, PICK_AUDIO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE -> viewModel.loadImage(data.data!!)
                PICK_VIDEO -> viewModel.loadVideo(data.data!!)
                PICK_AUDIO -> viewModel.loadAudio(data.data!!)
            }
        }
    }

    private fun showExportDialog() {
        val qualities = arrayOf("720p (HD)", "1080p (Full HD)", "4K (Premium)")
        val formats = arrayOf("9:16 (Story)", "1:1 (Square)", "16:9 (Landscape)")

        MaterialAlertDialogBuilder(this)
            .setTitle("Export Settings")
            .setItems(qualities) { _, which ->
                viewModel.exportMedia(which)
            }
            .show()
    }

    private fun observeViewModel() {
        viewModel.isProcessing.observe(this) { processing ->
            if (processing) {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnExport.isEnabled = false
            } else {
                binding.progressBar.visibility = View.GONE
                binding.btnExport.isEnabled = true
            }
        }

        viewModel.exportSuccess.observe(this) { success ->
            if (success) {
                Snackbar.make(binding.root, "Export successful!", Snackbar.LENGTH_LONG).show()
                adsUtils.showInterstitial(this)
            }
        }

        viewModel.error.observe(this) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }

        viewModel.progress.observe(this) { progress ->
            binding.progressBar.progress = progress
        }
    }
}
