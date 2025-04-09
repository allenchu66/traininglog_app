package com.allenchu66.traininglog.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*
import com.allenchu66.traininglog.model.Exercise
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory

@Dao
interface WorkoutDao {
    /** INSERT DATA */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: Workout)

    @Insert(onConflict = OnConflictStrategy.IGNORE)//避免重複插入
    suspend fun insertWorkoutCategory(workoutCategory: WorkoutCategory)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<WorkoutCategory>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)

    /** DELETE DATA */
    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkoutCategory(workoutCategory: WorkoutCategory)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    /** UPDATE DATA */
    @Update
    suspend fun updateWorkout(workout: Workout)

    @Update
    suspend fun updateWorkoutCategory(category: WorkoutCategory)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Query("SELECT * FROM workout WHERE date = :targetDate ORDER BY id")
    fun getWorkoutsByDate(targetDate: String): LiveData<List<Workout>>

    @Query("SELECT * FROM workout ORDER BY date DESC")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("SELECT COUNT(*) FROM category")
    fun getCategoryCount(): Int

    @Query("SELECT * FROM category")
    fun getAllWorkoutCategory(): LiveData<List<WorkoutCategory>>

    @Query("SELECT * FROM category")
    suspend fun getAllCategoriesDirect(): List<WorkoutCategory>

    @Query("SELECT COUNT(*) FROM category WHERE name = :categoryName")
    suspend fun countCategoryByName(categoryName: String): Int //查詢是否已有相同名稱的 Category。

    @Query("SELECT * FROM exercise WHERE categoryId = :categoryId")
    fun getExercisesByCategory(categoryId: Int): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise WHERE categoryId = :categoryId")
    suspend fun getExercisesByCategoryDirect(categoryId: Int): List<Exercise>

    @Query("SELECT DISTINCT date FROM workout")
    suspend fun getAllWorkoutDates(): List<String>

    @Query("SELECT * FROM workout")
    suspend fun getAllWorkoutsDirect(): List<Workout>

    @Query("SELECT * FROM exercise")
    suspend fun getAllExercisesDirect(): List<Exercise>

    @Query("DELETE FROM workout") suspend fun clearAllWorkouts()
    @Query("DELETE FROM exercise") suspend fun clearAllExercises()
    @Query("DELETE FROM category") suspend fun clearAllCategories()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(workouts: List<Workout>)

}