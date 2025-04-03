package com.allenchu66.traininglog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*
import com.allenchu66.traininglog.model.Workout

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workout ORDER BY date DESC")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM workout WHERE category = :category ORDER BY date DESC")
    fun getWorkoutsByCategory(category: String): LiveData<List<Workout>>
}