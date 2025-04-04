package com.allenchu66.traininglog.repository

import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory

class WorkoutRepository(private val db: WorkoutDatabase)  {

    suspend fun insertWorkout(workout: Workout) = db.getWorkoutDao().insertWorkout(workout);

    suspend fun deleteWorkout(workout: Workout) = db.getWorkoutDao().deleteWorkout(workout);
    suspend fun updateWorkout(workout: Workout) = db.getWorkoutDao().updateWorkout(workout);

    fun getAllWorkouts() = db.getWorkoutDao().getAllWorkouts()

    suspend fun insertCategory(workoutCategory: WorkoutCategory) = db.getWorkoutDao().insertWorkoutCategory(workoutCategory)
    suspend fun deleteCategory(workoutCategory: WorkoutCategory) = db.getWorkoutDao().deleteWorkoutCategory(workoutCategory)
    suspend fun categoryExists(name:String):Boolean{return db.getWorkoutDao().countCategoryByName(name)>0}

    fun getAllWorkoutCategory() = db.getWorkoutDao().getAllWorkoutCategory()
}
