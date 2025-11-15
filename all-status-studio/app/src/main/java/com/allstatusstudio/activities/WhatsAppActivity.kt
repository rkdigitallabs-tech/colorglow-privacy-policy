package com.allstatusstudio.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.allstatusstudio.R
import com.allstatusstudio.adapters.StatusAdapter
import com.allstatusstudio.databinding.ActivityWhatsappBinding
import com.allstatusstudio.utils.AdsUtils
import com.allstatusstudio.utils.PermissionUtils
import com.allstatusstudio.viewmodels.WhatsAppViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class WhatsAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatsappBinding
    private lateinit var viewModel: WhatsAppViewModel
    private lateinit var adapter: StatusAdapter
    private lateinit var adsUtils: AdsUtils

    private val PERMISSION_REQUEST_CODE = 101

    // Launcher for Android 11+ storage permission settings
    private val manageStoragePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                loadStatuses()
            } else {
                showPermissionDeniedMessage()
            }
        }
    }

    // Launcher for regular permissions
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            loadStatuses()
        } else {
            showPermissionDeniedMessage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhatsappBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WhatsAppViewModel::class.java]
        adsUtils = AdsUtils(this)

        setupUI()
        setupRecyclerView()
        observeViewModel()
        loadStatuses()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("WhatsApp"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Business"))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    viewModel.loadStatuses(it.position == 0)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.swipeRefresh.setOnRefreshListener {
            loadStatuses()
        }
    }

    private fun setupRecyclerView() {
        adapter = StatusAdapter(
            onItemClick = { status ->
                viewModel.previewStatus(status)
            },
            onSaveClick = { status ->
                viewModel.saveStatus(status)
            },
            onShareClick = { status ->
                viewModel.shareStatus(status)
            },
            onFavoriteClick = { status ->
                viewModel.toggleFavorite(status)
            }
        )

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.statuses.observe(this) { statuses ->
            binding.swipeRefresh.isRefreshing = false
            if (statuses.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(statuses)
            }
        }

        viewModel.saveSuccess.observe(this) { success ->
            if (success) {
                Snackbar.make(binding.root, "Status saved successfully!", Snackbar.LENGTH_SHORT).show()
                adsUtils.showInterstitial(this)
            }
        }

        viewModel.error.observe(this) { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun loadStatuses() {
        // Check if we have storage permissions
        if (!PermissionUtils.hasStoragePermission(this)) {
            requestStoragePermission()
            return
        }

        val isWhatsApp = binding.tabLayout.selectedTabPosition == 0
        viewModel.loadStatuses(isWhatsApp)
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ requires MANAGE_EXTERNAL_STORAGE permission via settings
            showManageStoragePermissionDialog()
        } else {
            // Android 10 and below use regular runtime permissions
            permissionLauncher.launch(PermissionUtils.getRequiredPermissions())
        }
    }

    private fun showManageStoragePermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Storage Permission Required")
            .setMessage("To access WhatsApp statuses, this app needs 'All files access' permission.\n\nPlease enable it in the next screen.")
            .setPositiveButton("Grant Permission") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    manageStoragePermissionLauncher.launch(intent)
                } catch (e: Exception) {
                    // Fallback to general settings
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    manageStoragePermissionLauncher.launch(intent)
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                showPermissionDeniedMessage()
            }
            .setCancelable(false)
            .show()
    }

    private fun showPermissionDeniedMessage() {
        binding.swipeRefresh.isRefreshing = false
        binding.emptyView.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE

        Snackbar.make(
            binding.root,
            "Storage permission is required to access WhatsApp statuses",
            Snackbar.LENGTH_LONG
        ).setAction("Grant") {
            requestStoragePermission()
        }.show()
    }
}
