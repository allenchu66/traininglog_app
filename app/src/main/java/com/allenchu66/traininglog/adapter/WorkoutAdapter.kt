package com.allenchu66.traininglog.adapter

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

class WorkoutAdapter : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    class WorkoutViewHolder(val itemBinding: WorkoutItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    private val differCallback = object : DiffUtil.ItemCallback<Workout>() {
        override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.category == newItem.category &&
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
    private val categories = listOf("胸", "背", "腿", "肩", "手臂")

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            WorkoutItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val  currentWorkoutItem = differ.currentList[position]

        val spinnerAdapter = ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.itemBinding.workoutCategory.adapter = spinnerAdapter;
        holder.itemBinding.workoutCategory.setSelection(categories.indexOf(currentWorkoutItem.category))
        holder.itemBinding.workoutCategory.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        holder.itemBinding.workoutTitle.text = currentWorkoutItem.category
    }

}