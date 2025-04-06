package com.allenchu66.traininglog.adapter

import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.databinding.WorkoutItemFooterBinding
import com.allenchu66.traininglog.model.WorkoutGroup
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class WorkoutFooterViewHolder(
    private val binding: WorkoutItemFooterBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(group: WorkoutGroup, workoutViewModel: WorkoutViewModel) {
        binding.btnAddWorkout.setOnClickListener {
            val last = group.workouts.lastOrNull() ?: return@setOnClickListener

            val newWorkout = last.copy(
                id = 0, // Room auto-generate id
                date = System.currentTimeMillis()
            )

            workoutViewModel.addWorkout(newWorkout)
        }
    }
}