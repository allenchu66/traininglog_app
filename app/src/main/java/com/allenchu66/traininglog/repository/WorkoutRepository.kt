package com.allenchu66.traininglog.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.model.Workout
import kotlinx.coroutines.launch

class WorkoutRepository(private val db: WorkoutDatabase)  {

    suspend fun insertWorkout(workout: Workout) = db.getWorkoutDao().insertWorkout(workout);

    suspend fun deleteWorkout(workout: Workout) = db.getWorkoutDao().deleteWorkout(workout);

    suspend fun getWorkoutsByCategory(category: String) = db.getWorkoutDao().getWorkoutsByCategory(category)

    fun getAll() = db.getWorkoutDao().getAllWorkouts()
}
