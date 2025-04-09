package com.allenchu66.traininglog.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Index
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.databinding.WorkoutItemContentBinding
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class WorkoutContentViewHolder(
    private val binding: WorkoutItemContentBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(workout: Workout, workoutViewModel: WorkoutViewModel,index: Int) {
        binding.tvSetIndex.text = "第 ${index} 組"
        binding.btnWorkoutWeight.text = "${workout.weight} kg"
        binding.btnWorkoutReps.text = "${workout.reps} reps"

        binding.btnWorkoutWeight.setOnClickListener {
            showTwoPartWeightPickerDialog(
                context = binding.root.context,
                currentWeight = workout.weight
            ) { selected ->
                workout.weight = selected
                workoutViewModel.updateWorkout(workout)
                binding.btnWorkoutWeight.text = "${String.format("%.1f", selected)} kg"
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

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(binding.root.context)
                .setTitle("確定刪除這筆紀錄？")
                .setMessage("刪除後無法復原")
                .setPositiveButton("刪除") { _, _ ->
                    workoutViewModel.removeWorkout(workout)
                }
                .setNegativeButton("取消", null)
                .show()
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

    fun showTwoPartWeightPickerDialog(
        context: Context,
        currentWeight: Float,
        onWeightSelected: (Float) -> Unit
    ) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_weight_picker, null)
        val pickerInt = dialogView.findViewById<NumberPicker>(R.id.picker_integer)
        val pickerDecimal = dialogView.findViewById<NumberPicker>(R.id.picker_decimal)

        pickerInt.minValue = 0
        pickerInt.maxValue = 200

        pickerDecimal.minValue = 0
        pickerDecimal.maxValue = 9
        pickerDecimal.displayedValues = Array(10) { it.toString() } // ["0", "1", ..., "9"]

        val intPart = currentWeight.toInt()
        val decPart = ((currentWeight - intPart) * 10).toInt()

        pickerInt.value = intPart
        pickerDecimal.value = decPart

        AlertDialog.Builder(context)
            .setTitle("選擇重量 (kg)")
            .setView(dialogView)
            .setPositiveButton("確定") { _, _ ->
                val selected = pickerInt.value + pickerDecimal.value / 10f
                onWeightSelected(selected)
            }
            .setNegativeButton("取消", null)
            .show()
    }



}
