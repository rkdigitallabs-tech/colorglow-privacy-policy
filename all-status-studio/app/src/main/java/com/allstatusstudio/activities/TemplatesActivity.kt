package com.allstatusstudio.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.allstatusstudio.R
import com.allstatusstudio.adapters.TemplateAdapter
import com.allstatusstudio.databinding.ActivityTemplatesBinding
import com.allstatusstudio.viewmodels.TemplatesViewModel
import com.google.android.material.chip.Chip

class TemplatesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTemplatesBinding
    private lateinit var viewModel: TemplatesViewModel
    private lateinit var adapter: TemplateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTemplatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TemplatesViewModel::class.java]

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        adapter = TemplateAdapter { template ->
            val intent = Intent(this, EditorActivity::class.java)
            intent.putExtra("template_id", template.id)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.categories.observe(this) { categories ->
            binding.chipGroup.removeAllViews()
            categories.forEach { category ->
                val chip = Chip(this)
                chip.text = category
                chip.isCheckable = true
                chip.setOnClickListener {
                    viewModel.filterByCategory(category)
                }
                binding.chipGroup.addView(chip)
            }
        }

        viewModel.templates.observe(this) { templates ->
            if (templates.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(templates)
            }
        }
    }
}
