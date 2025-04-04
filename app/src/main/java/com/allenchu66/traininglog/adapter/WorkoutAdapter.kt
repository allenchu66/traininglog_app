package com.allenchu66.traininglog.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.allenchu66.traininglog.databinding.WorkoutItemLayoutBinding
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel

class WorkoutAdapter(private val workoutViewModel: WorkoutViewModel) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    private var categories: List<WorkoutCategory> = emptyList()
    fun updateCategories(newCategories: List<WorkoutCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    class WorkoutViewHolder(val itemBinding: WorkoutItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val spinnerAdapter = ArrayAdapter<WorkoutCategory>(
            itemBinding.root.context,
            android.R.layout.simple_spinner_item,
            mutableListOf<WorkoutCategory>()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        init {
            itemBinding.workoutCategory.adapter = spinnerAdapter
        }

        fun bind(workout: Workout, categories: List<WorkoutCategory>,workoutViewModel: WorkoutViewModel) {
            spinnerAdapter.clear()
            spinnerAdapter.addAll(categories)
            spinnerAdapter.notifyDataSetChanged()

            val selectedIndex = categories.indexOfFirst { it.id == workout.categoryId }
            if (selectedIndex != -1) {
                itemBinding.workoutCategory.setSelection(selectedIndex)
            }

            itemBinding.workoutCategory.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedCategory = categories[position]
                        Log.d("20250404","ID: ${selectedCategory.id}, Name: ${selectedCategory.name}")
                        // 這裡可以觸發更新資料庫的操作
                        workout.categoryId = selectedCategory.id
                        workoutViewModel.updateWorkout(workout)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.categoryId == newItem.categoryId &&
                    oldItem.exercise == newItem.exercise &&
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
        holder.bind(currentWorkoutItem, categories,workoutViewModel)
        holder.itemBinding.workoutTitle.text = currentWorkoutItem.categoryId.toString()
    }

}