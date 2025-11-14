package com.allstatusstudio.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.allstatusstudio.R
import com.allstatusstudio.adapters.GalleryAdapter
import com.allstatusstudio.databinding.ActivityGalleryBinding
import com.allstatusstudio.utils.AdsUtils
import com.allstatusstudio.viewmodels.GalleryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding
    private lateinit var viewModel: GalleryViewModel
    private lateinit var adapter: GalleryAdapter
    private lateinit var adsUtils: AdsUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        adsUtils = AdsUtils(this)

        setupUI()
        setupRecyclerView()
        observeViewModel()
        loadAds()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.chipAll.setOnClickListener { viewModel.filterBy("all") }
        binding.chipFavorites.setOnClickListener { viewModel.filterBy("favorites") }
        binding.chipEdited.setOnClickListener { viewModel.filterBy("edited") }
    }

    private fun setupRecyclerView() {
        adapter = GalleryAdapter(
            onItemClick = { media ->
                viewModel.openMedia(media)
            },
            onDeleteClick = { media ->
                showDeleteConfirmation(media)
            },
            onMoveToVaultClick = { media ->
                viewModel.moveToVault(media)
            },
            onScheduleClick = { media ->
                viewModel.schedulePost(media)
            }
        )

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.mediaList.observe(this) { list ->
            if (list.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(list)
            }
        }

        viewModel.message.observe(this) { msg ->
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmation(media: Any) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Media")
            .setMessage("Are you sure you want to delete this file?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteMedia(media)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadAds() {
        adsUtils.loadBanner(binding.adView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.gallery_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_date -> {
                viewModel.sortBy("date")
                true
            }
            R.id.action_sort_name -> {
                viewModel.sortBy("name")
                true
            }
            R.id.action_sort_size -> {
                viewModel.sortBy("size")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
