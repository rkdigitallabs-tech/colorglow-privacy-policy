package com.allstatusstudio.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allstatusstudio.R
import com.allstatusstudio.data.models.TemplateModel
import com.allstatusstudio.databinding.ItemTemplateBinding
import com.bumptech.glide.Glide

class TemplateAdapter(
    private val onTemplateClick: (TemplateModel) -> Unit
) : ListAdapter<TemplateModel, TemplateAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemTemplateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(template: TemplateModel) {
            // Load template preview
            Glide.with(binding.root.context)
                .load(template.thumbnailPath)
                .placeholder(R.drawable.placeholder_template)
                .into(binding.ivTemplate)

            binding.tvCategory.text = template.category

            // Premium badge
            binding.ivPremium.visibility = if (template.isPremium) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }

            binding.root.setOnClickListener { onTemplateClick(template) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTemplateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<TemplateModel>() {
        override fun areItemsTheSame(oldItem: TemplateModel, newItem: TemplateModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TemplateModel, newItem: TemplateModel): Boolean {
            return oldItem == newItem
        }
    }
}
