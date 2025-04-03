package com.allenchu66.traininglog.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutViewModel(app:Application,private val workoutRepository: WorkoutRepository) : AndroidViewModel(app){
    fun addWorkout(workout: Workout) = viewModelScope.launch { workoutRepository.insertWorkout(workout) }
    fun removeWorkout(workout: Workout) = viewModelScope.launch { workoutRepository.deleteWorkout(workout) }
    fun getWorkout(workout: String) = viewModelScope.launch { workoutRepository.getWorkoutsByCategory(workout) }
    fun getAllWorkout() = workoutRepository.getAll()
}

