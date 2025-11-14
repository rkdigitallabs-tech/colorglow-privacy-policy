package com.allstatusstudio.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.allstatusstudio.R
import com.allstatusstudio.adapters.StatusAdapter
import com.allstatusstudio.databinding.ActivityWhatsappBinding
import com.allstatusstudio.utils.AdsUtils
import com.allstatusstudio.viewmodels.WhatsAppViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

class WhatsAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWhatsappBinding
    private lateinit var viewModel: WhatsAppViewModel
    private lateinit var adapter: StatusAdapter
    private lateinit var adsUtils: AdsUtils

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
        val isWhatsApp = binding.tabLayout.selectedTabPosition == 0
        viewModel.loadStatuses(isWhatsApp)
    }
}
