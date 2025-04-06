package com.allenchu66.traininglog.adapter

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.databinding.WorkoutItemHeaderBinding
import com.allenchu66.traininglog.model.Exercise
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.model.WorkoutGroup
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class WorkoutHeaderViewHolder(val itemBinding: WorkoutItemHeaderBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    private val spinnerCategoryAdapter = ArrayAdapter<WorkoutCategory>(
        itemBinding.root.context,
        android.R.layout.simple_spinner_item,
        mutableListOf<WorkoutCategory>()
    ).apply {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    private val spinnerExerciseAdapter = ArrayAdapter<Exercise>(
        itemBinding.root.context,
        android.R.layout.simple_spinner_item,
        mutableListOf<Exercise>()
    ).apply {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    init {
        itemBinding.workoutCategory.adapter = spinnerCategoryAdapter
        itemBinding.workoutExercise.adapter = spinnerExerciseAdapter
    }


    fun bind(
        group: WorkoutGroup,
        categories: List<WorkoutCategory>,
        workoutViewModel: WorkoutViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        val firstWorkout = group.workouts.firstOrNull() ?: return

        spinnerCategoryAdapter.clear()
        spinnerCategoryAdapter.addAll(categories)
        spinnerCategoryAdapter.notifyDataSetChanged()

        val categorySelectedIndex = categories.indexOfFirst { it.id == group.category.id }
        if (categorySelectedIndex != -1) {
            itemBinding.workoutCategory.setSelection(categorySelectedIndex)
        }

        itemBinding.workoutCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    Log.d("WorkoutAdapter", "Category spinner listener")
                    val selectedCategory = categories[position]
                    // 這裡可以觸發更新資料庫的操作
                    if (group.category.id != selectedCategory.id) {
                        Log.d(
                            "WorkoutAdapter",
                            "category ID: ${selectedCategory.id}, Name: ${selectedCategory.name}"
                        )
                        // 更新 group 所有 Workout 的 categoryId 與 exerciseId（清空）
                        group.workouts.forEach { workout ->
                            workout.categoryId = selectedCategory.id
                            workout.exerciseId = null
                            workoutViewModel.updateWorkout(workout)
                        }

                        updateExerciseSpinner(
                            group,
                            workoutViewModel,
                            group.category.id ?: return,
                            itemBinding,
                            lifecycleOwner
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        //set Exercise spinner
        updateExerciseSpinner(
            group,
            workoutViewModel,
            group.category.id ?: return,
            itemBinding,
            lifecycleOwner
        )
    }

    private fun updateExerciseSpinner(
        group: WorkoutGroup,
        workoutViewModel: WorkoutViewModel,
        categoryId: Int,
        binding: WorkoutItemHeaderBinding,
        lifecycleOwner: LifecycleOwner
    ) {
//            // 清空原有的 Exercise 資料
        val emptyList = mutableListOf<Exercise>()
        spinnerExerciseAdapter.clear()
        spinnerExerciseAdapter.addAll(emptyList)
        spinnerExerciseAdapter.notifyDataSetChanged()

        // 加載新的 Exercise 資料
        workoutViewModel.getExercisesByCategory(categoryId)
            .observe(lifecycleOwner) { exercises ->
                exercises?.let {
                    // 更新 spinner 資料
                    spinnerExerciseAdapter.clear()
                    spinnerExerciseAdapter.addAll(it)
                    spinnerExerciseAdapter.notifyDataSetChanged()

                    // 確保設置 onItemSelectedListener 在更新後依然存在
                    itemBinding.workoutExercise.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                Log.d("WorkoutAdapter", "Exercise spinner listener")
                                val selectedExercise = spinnerExerciseAdapter.getItem(position)
                                selectedExercise?.let {
                                    if (group.exercise.id != it.id) {
                                        Log.d(
                                            "20250404",
                                            "更新動作：ID=${it.id}, Name=${it.name}"
                                        )
                                        group.workouts.forEach { workout ->
                                            workout.exerciseId = selectedExercise.id
                                            workoutViewModel.updateWorkout(workout)
                                        }
                                    }
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }

                    // 選擇對應的 Exercise 項目
                    val selectedExerciseIndex =
                        it.indexOfFirst { exercise -> exercise.id == group.exercise.id }
                    if (selectedExerciseIndex != -1) {
                        itemBinding.workoutExercise.setSelection(selectedExerciseIndex)
                    }
                }
            }
    }
}