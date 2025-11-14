package com.allstatusstudio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allstatusstudio.R
import com.allstatusstudio.data.models.StatusModel
import com.allstatusstudio.databinding.ItemGalleryBinding
import com.bumptech.glide.Glide

class GalleryAdapter(
    private val onItemClick: (Any) -> Unit,
    private val onDeleteClick: (Any) -> Unit,
    private val onMoveToVaultClick: (Any) -> Unit,
    private val onScheduleClick: (Any) -> Unit
) : ListAdapter<StatusModel, GalleryAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(media: StatusModel) {
            Glide.with(binding.root.context)
                .load(media.path)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.ivThumbnail)

            binding.ivPlayIcon.visibility = if (media.isVideo) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }

            binding.root.setOnClickListener { onItemClick(media) }
            binding.root.setOnLongClickListener {
                showOptionsMenu(media)
                true
            }
        }

        private fun showOptionsMenu(media: StatusModel) {
            val popup = androidx.appcompat.widget.PopupMenu(binding.root.context, binding.root)
            popup.inflate(R.menu.gallery_item_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete -> {
                        onDeleteClick(media)
                        true
                    }
                    R.id.action_move_to_vault -> {
                        onMoveToVaultClick(media)
                        true
                    }
                    R.id.action_schedule -> {
                        onScheduleClick(media)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGalleryBinding.inflate(
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
