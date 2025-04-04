package com.allenchu66.traininglog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workout ORDER BY date DESC")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutCategory(workoutCategory: WorkoutCategory)

    @Delete
    suspend fun deleteWorkoutCategory(workoutCategory: WorkoutCategory)

    @Query("SELECT * FROM category")
    fun getAllWorkoutCategorys(): LiveData<List<WorkoutCategory>>
}