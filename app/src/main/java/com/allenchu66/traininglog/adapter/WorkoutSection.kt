package com.allenchu66.traininglog.adapter

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.databinding.WorkoutItemContentBinding
import com.allenchu66.traininglog.databinding.WorkoutItemFooterBinding
import com.allenchu66.traininglog.databinding.WorkoutItemHeaderBinding
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.model.WorkoutGroup
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters

class WorkoutSection(
    private var group: WorkoutGroup,
    private var categories: List<WorkoutCategory>,
    private val workoutViewModel: WorkoutViewModel,
    private val lifecycleOwner: LifecycleOwner
) : Section(
    SectionParameters.builder()
        .itemResourceId(R.layout.workout_item_content)
        .headerResourceId(R.layout.workout_item_header)
        .footerResourceId(R.layout.workout_item_footer)
        .build()
) {

    fun getGroup(): WorkoutGroup = group

    init {
        Log.d("WorkoutSection", "init with group: ${group.category.name}, items=${group.workouts.size}")
    }

    override fun getContentItemsTotal(): Int {
        val total = group.workouts.size
        Log.d("WorkoutSection", "getContentItemsTotal: $total")
        return total // 每個 section 只有一個 workout
    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        Log.d("WorkoutSection", "getItemViewHolder called")
        val binding = WorkoutItemContentBinding.bind(view!!)
        return WorkoutContentViewHolder(binding)
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val workout = group.workouts[position]
        Log.d("WorkoutSection", "test1")
        if (holder is WorkoutContentViewHolder) {
            Log.d("WorkoutSection", "Binding workout id=${workout.id}, weight=${workout.weight}, reps=${workout.reps}")
            holder.bind(workout, workoutViewModel,position+1)
        }
    }

    // Optional: Header 處理
    override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
        Log.d("WorkoutSection", "getHeaderViewHolder called")
        val binding = WorkoutItemHeaderBinding.bind(view)
        return WorkoutHeaderViewHolder(binding)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder) {
        Log.d("WorkoutSection", "test2")
        if (holder is WorkoutHeaderViewHolder) {
            Log.d("WorkoutSection", "Binding header for [${group.category.name}][${group.exercise.name}]")
            holder.bind(group,categories, workoutViewModel, lifecycleOwner)
        }
    }

    override fun getFooterViewHolder(view: View): RecyclerView.ViewHolder {
        val binding = WorkoutItemFooterBinding.bind(view)
        return WorkoutFooterViewHolder(binding)
    }

    override fun onBindFooterViewHolder(holder: RecyclerView.ViewHolder) {
        if (holder is WorkoutFooterViewHolder) {
            holder.bind(group, workoutViewModel)
        }
    }

    fun updateGroup(newGroup: WorkoutGroup, newCategories: List<WorkoutCategory>) {
        this.group = newGroup
        this.categories = newCategories
    }
}
