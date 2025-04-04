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

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Query("SELECT * FROM workout ORDER BY date DESC")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)//避免重複插入
    suspend fun insertWorkoutCategory(workoutCategory: WorkoutCategory)

    @Delete
    suspend fun deleteWorkoutCategory(workoutCategory: WorkoutCategory)

    @Query("SELECT COUNT(*) FROM category")
    fun getCategoryCount(): Int

    @Query("SELECT * FROM category")
    fun getAllWorkoutCategory(): LiveData<List<WorkoutCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<WorkoutCategory>)

    @Query("SELECT COUNT(*) FROM category WHERE name = :categoryName")
    suspend fun countCategoryByName(categoryName: String): Int //查詢是否已有相同名稱的 Category。
}