package com.allstatusstudio.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.allstatusstudio.adapters.CleanerAdapter
import com.allstatusstudio.databinding.ActivityCleanerBinding
import com.allstatusstudio.utils.AdsUtils
import com.allstatusstudio.viewmodels.CleanerViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class CleanerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCleanerBinding
    private lateinit var viewModel: CleanerViewModel
    private lateinit var adapter: CleanerAdapter
    private lateinit var adsUtils: AdsUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCleanerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CleanerViewModel::class.java]
        adsUtils = AdsUtils(this)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnScanDuplicates.setOnClickListener {
            viewModel.scanDuplicates()
        }

        binding.btnScanLargeFiles.setOnClickListener {
            viewModel.scanLargeFiles()
        }

        binding.btnScanOldEdits.setOnClickListener {
            viewModel.scanOldEdits()
        }

        binding.btnCleanAll.setOnClickListener {
            showCleanConfirmation()
        }
    }

    private fun setupRecyclerView() {
        adapter = CleanerAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.isScanning.observe(this) { scanning ->
            if (scanning) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.junkFiles.observe(this) { files ->
            if (files.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.btnCleanAll.isEnabled = false
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.btnCleanAll.isEnabled = true
                adapter.submitList(files)

                val totalSize = files.sumOf { it.size } / (1024 * 1024)
                binding.tvTotalSize.text = "Total: ${totalSize} MB"
            }
        }

        viewModel.cleanSuccess.observe(this) { success ->
            if (success) {
                Snackbar.make(binding.root, "Cleanup completed!", Snackbar.LENGTH_LONG).show()
                adsUtils.showInterstitial(this)
            }
        }
    }

    private fun showCleanConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Clean Junk Files")
            .setMessage("Are you sure you want to delete all selected files? This cannot be undone.")
            .setPositiveButton("Clean") { _, _ ->
                viewModel.cleanJunkFiles()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
