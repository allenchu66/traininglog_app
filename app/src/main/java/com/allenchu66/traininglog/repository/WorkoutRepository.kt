package com.allenchu66.traininglog.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import kotlinx.coroutines.launch

class WorkoutRepository(private val db: WorkoutDatabase)  {

    suspend fun insertWorkout(workout: Workout) = db.getWorkoutDao().insertWorkout(workout);

    suspend fun deleteWorkout(workout: Workout) = db.getWorkoutDao().deleteWorkout(workout);

    fun getAllWorkouts() = db.getWorkoutDao().getAllWorkouts()

    suspend fun insertCategory(workoutCategory: WorkoutCategory) = db.getWorkoutDao().insertWorkoutCategory(workoutCategory)
    suspend fun deleteCategory(workoutCategory: WorkoutCategory) = db.getWorkoutDao().deleteWorkoutCategory(workoutCategory)

    fun getAllWorkoutCategory() = db.getWorkoutDao().getAllWorkoutCategorys()
}
