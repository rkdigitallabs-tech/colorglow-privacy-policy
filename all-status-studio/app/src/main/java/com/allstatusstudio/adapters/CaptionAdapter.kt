package com.allstatusstudio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allstatusstudio.databinding.ItemCaptionBinding

class CaptionAdapter(
    private val onCaptionClick: (String) -> Unit
) : ListAdapter<String, CaptionAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemCaptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(caption: String) {
            binding.tvCaption.text = caption

            binding.btnCopy.setOnClickListener {
                onCaptionClick(caption)
            }

            binding.root.setOnClickListener {
                onCaptionClick(caption)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCaptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
