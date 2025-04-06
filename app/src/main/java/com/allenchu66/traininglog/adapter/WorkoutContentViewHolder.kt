package com.allenchu66.traininglog.adapter

import android.app.AlertDialog
import android.content.Context
import android.widget.NumberPicker
import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.databinding.WorkoutItemContentBinding
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class WorkoutContentViewHolder(
    private val binding: WorkoutItemContentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(workout: Workout, workoutViewModel: WorkoutViewModel) {
        binding.btnWorkoutWeight.text = "${workout.weight} kg"
        binding.btnWorkoutReps.text = "${workout.reps} reps"

        binding.btnWorkoutWeight.setOnClickListener {
            showNumberPickerDialog(
                context = binding.root.context,
                title = "選擇重量 (kg)",
                min = 0, max = 200,
                currentValue = workout.weight.toInt()
            ) { selected ->
                workout.weight = selected.toFloat()
                workoutViewModel.updateWorkout(workout)
                binding.btnWorkoutWeight.text = "$selected kg"
            }
        }

        binding.btnWorkoutReps.setOnClickListener {
            showNumberPickerDialog(
                context = binding.root.context,
                title = "選擇次數",
                min = 1, max = 30,
                currentValue = workout.reps
            ) { selected ->
                workout.reps = selected
                workoutViewModel.updateWorkout(workout)
                binding.btnWorkoutReps.text = "$selected reps"
            }
        }
    }

    private fun showNumberPickerDialog(
        context: Context,
        title: String,
        min: Int,
        max: Int,
        currentValue: Int,
        onValueSelected: (Int) -> Unit
    ) {
        val numberPicker = NumberPicker(context).apply {
            minValue = min
            maxValue = max
            value = currentValue
        }

        AlertDialog.Builder(context)
            .setTitle(title)
            .setView(numberPicker)
            .setPositiveButton("確定") { _, _ -> onValueSelected(numberPicker.value) }
            .setNegativeButton("取消", null)
            .show()
    }
}
