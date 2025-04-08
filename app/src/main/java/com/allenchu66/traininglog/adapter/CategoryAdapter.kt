package com.allenchu66.traininglog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.databinding.CategoryRecyclerviewItemBinding
import com.allenchu66.traininglog.model.WorkoutCategory

class CategoryAdapter(
    private val onRenameClick: (WorkoutCategory) -> Unit,
    private val onDeleteClick: (WorkoutCategory) -> Unit
) : ListAdapter<WorkoutCategory, CategoryAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    inner class CategoryViewHolder(private val binding: CategoryRecyclerviewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: WorkoutCategory) {
            binding.tvCategoryName.text = category.name
            binding.btnRename.setOnClickListener { onRenameClick(category) }
            binding.btnDelete.setOnClickListener { onDeleteClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorkoutCategory>() {
            override fun areItemsTheSame(oldItem: WorkoutCategory, newItem: WorkoutCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WorkoutCategory, newItem: WorkoutCategory): Boolean {
                return oldItem == newItem
            }
        }
    }
}
