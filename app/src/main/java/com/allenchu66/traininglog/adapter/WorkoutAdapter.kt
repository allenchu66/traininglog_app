package com.allenchu66.traininglog.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.databinding.WorkoutItemLayoutBinding
import com.allenchu66.traininglog.model.Exercise
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class WorkoutAdapter(
    private val workoutViewModel: WorkoutViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private var categories: List<WorkoutCategory> = emptyList()
    private var exercises: List<Exercise> = emptyList()
    private val TAG = "WorkoutAdapter"

    fun updateCategories(newCategories: List<WorkoutCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    fun updateExercise(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    class WorkoutViewHolder(val itemBinding: WorkoutItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val spinnerCategoryAdapter = ArrayAdapter<WorkoutCategory>(
            itemBinding.root.context,
            android.R.layout.simple_spinner_item,
            mutableListOf<WorkoutCategory>()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        init {
            itemBinding.workoutCategory.adapter = spinnerCategoryAdapter
        }

        val spinnerExerciseAdapter = ArrayAdapter<Exercise>(
            itemBinding.root.context,
            android.R.layout.simple_spinner_item,
            mutableListOf<Exercise>()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        init {
            itemBinding.workoutExercise.adapter = spinnerExerciseAdapter
        }

        fun bind(
            workout: Workout,
            categories: List<WorkoutCategory>,
            exercise: List<Exercise>,
            workoutViewModel: WorkoutViewModel,
            lifecycleOwner: LifecycleOwner
        ) {
            spinnerCategoryAdapter.clear()
            spinnerCategoryAdapter.addAll(categories)
            spinnerCategoryAdapter.notifyDataSetChanged()

            val categorySelectedIndex = categories.indexOfFirst { it.id == workout.categoryId }
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
                        Log.d("WorkoutAdapter","Category spinner listener")
                        val selectedCategory = categories[position]
                        // 這裡可以觸發更新資料庫的操作
                        if(workout.categoryId != selectedCategory.id) {
                            Log.d("WorkoutAdapter", "category ID: ${selectedCategory.id}, Name: ${selectedCategory.name}")
                            workout.categoryId = selectedCategory.id
                            workout.exerciseId = null
                            workoutViewModel.updateWorkout(workout)
                            updateExerciseSpinner(
                                workout,
                                workoutViewModel,
                                workout.categoryId ?: return,
                                itemBinding,
                                lifecycleOwner
                            )
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            //set Exercise spinner
            updateExerciseSpinner(
                workout,
                workoutViewModel,
                workout.categoryId ?: return,
                itemBinding,
                lifecycleOwner
            )
        }

        private fun updateExerciseSpinner(
            workout: Workout,
            workoutViewModel: WorkoutViewModel,
            categoryId: Int,
            binding: WorkoutItemLayoutBinding,
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
                                    Log.d("WorkoutAdapter","Exercise spinner listener")
                                    val selectedExercise = spinnerExerciseAdapter.getItem(position)
                                    selectedExercise?.let {
                                        if(workout.exerciseId != it.id) {
                                            Log.d("20250404", "更新動作：ID=${it.id}, Name=${it.name}")
                                            workout.exerciseId = it.id
                                            workoutViewModel.updateWorkout(workout)
                                        }
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }

                        // 選擇對應的 Exercise 項目
                        val selectedExerciseIndex =
                            it.indexOfFirst { exercise -> exercise.id == workout.exerciseId }
                        if (selectedExerciseIndex != -1) {
                            itemBinding.workoutExercise.setSelection(selectedExerciseIndex)
                        }
                    }
                }
        }
    }


    private val differCallback = object : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.categoryId == newItem.categoryId &&
                    oldItem.exerciseId == newItem.exerciseId &&
                    oldItem.sets == newItem.sets &&
                    oldItem.reps == newItem.reps &&
                    oldItem.weight == newItem.weight &&
                    oldItem.date == newItem.date &&
                    oldItem.notes == newItem.notes
        }

        override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            WorkoutItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val currentWorkoutItem = differ.currentList[position]
        holder.bind(currentWorkoutItem, categories, exercises, workoutViewModel, lifecycleOwner)
        holder.itemBinding.workoutTitle.text = currentWorkoutItem.categoryId.toString()
    }

}