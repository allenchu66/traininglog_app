package com.allenchu66.traininglog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.databinding.ItemEditableEntryBinding

class EditableEntryAdapter<T : Any>(
    private val getItemName: (T) -> String,
    private val areItemsTheSame: (T, T) -> Boolean,
    private val areContentsTheSame: (T, T) -> Boolean,
    private val onRenameClick: (T) -> Unit,
    private val onDeleteClick: (T) -> Unit
) : ListAdapter<T, EditableEntryAdapter<T>.EditableViewHolder>(
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem, newItem)
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsTheSame(oldItem, newItem)
    }
) {

    inner class EditableViewHolder(private val binding: ItemEditableEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.tvName.text = getItemName(item)
            binding.btnRename.setOnClickListener { onRenameClick(item) }
            binding.btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditableViewHolder {
        val binding = ItemEditableEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditableViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}