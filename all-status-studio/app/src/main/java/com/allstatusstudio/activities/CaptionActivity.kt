package com.allstatusstudio.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.allstatusstudio.R
import com.allstatusstudio.adapters.CaptionAdapter
import com.allstatusstudio.databinding.ActivityCaptionBinding
import com.allstatusstudio.viewmodels.CaptionViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

class CaptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCaptionBinding
    private lateinit var viewModel: CaptionViewModel
    private lateinit var adapter: CaptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CaptionViewModel::class.java]

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnRandom.setOnClickListener {
            viewModel.generateRandomCaption()
        }
    }

    private fun setupRecyclerView() {
        adapter = CaptionAdapter { caption ->
            copyToClipboard(caption)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.categories.observe(this) { categories ->
            binding.chipGroup.removeAllViews()
            categories.forEach { category ->
                val chip = Chip(this)
                chip.text = category.capitalize()
                chip.isCheckable = true
                chip.setOnClickListener {
                    viewModel.filterByCategory(category)
                }
                binding.chipGroup.addView(chip)
            }
        }

        viewModel.captions.observe(this) { captions ->
            if (captions.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(captions)
            }
        }

        viewModel.randomCaption.observe(this) { caption ->
            binding.tvRandomCaption.text = caption
            binding.cardRandom.visibility = View.VISIBLE
            binding.btnCopyRandom.setOnClickListener {
                copyToClipboard(caption)
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("caption", text)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(binding.root, "Caption copied!", Snackbar.LENGTH_SHORT).show()
    }
}
