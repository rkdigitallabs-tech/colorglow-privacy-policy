package com.allstatusstudio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allstatusstudio.R
import com.allstatusstudio.data.models.StatusModel
import com.allstatusstudio.databinding.ItemStatusBinding
import com.bumptech.glide.Glide

class StatusAdapter(
    private val onItemClick: (StatusModel) -> Unit,
    private val onSaveClick: (StatusModel) -> Unit,
    private val onShareClick: (StatusModel) -> Unit,
    private val onFavoriteClick: (StatusModel) -> Unit
) : ListAdapter<StatusModel, StatusAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(status: StatusModel) {
            // Load thumbnail
            Glide.with(binding.root.context)
                .load(status.path)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.ivThumbnail)

            // Show video indicator
            binding.ivPlayIcon.visibility = if (status.isVideo) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }

            // Click listeners
            binding.root.setOnClickListener { onItemClick(status) }
            binding.btnSave.setOnClickListener { onSaveClick(status) }
            binding.btnShare.setOnClickListener { onShareClick(status) }
            binding.btnFavorite.setOnClickListener { onFavoriteClick(status) }

            // Favorite state
            binding.btnFavorite.setImageResource(
                if (status.isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_outline
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatusBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<StatusModel>() {
        override fun areItemsTheSame(oldItem: StatusModel, newItem: StatusModel): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: StatusModel, newItem: StatusModel): Boolean {
            return oldItem == newItem
        }
    }
}
