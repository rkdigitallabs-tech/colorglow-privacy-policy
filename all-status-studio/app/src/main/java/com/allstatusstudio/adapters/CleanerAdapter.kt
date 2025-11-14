package com.allstatusstudio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allstatusstudio.databinding.ItemCleanerBinding
import com.allstatusstudio.viewmodels.CleanerViewModel

class CleanerAdapter :
    ListAdapter<CleanerViewModel.JunkFile, CleanerAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemCleanerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(junkFile: CleanerViewModel.JunkFile) {
            binding.tvFileName.text = junkFile.name
            binding.tvFileType.text = junkFile.type

            val sizeMB = junkFile.size / (1024 * 1024)
            binding.tvFileSize.text = "${sizeMB}MB"

            binding.checkbox.isChecked = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCleanerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<CleanerViewModel.JunkFile>() {
        override fun areItemsTheSame(
            oldItem: CleanerViewModel.JunkFile,
            newItem: CleanerViewModel.JunkFile
        ): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(
            oldItem: CleanerViewModel.JunkFile,
            newItem: CleanerViewModel.JunkFile
        ): Boolean {
            return oldItem == newItem
        }
    }
}
