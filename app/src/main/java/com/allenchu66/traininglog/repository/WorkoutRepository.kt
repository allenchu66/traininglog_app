package com.allenchu66.traininglog.repository

import androidx.lifecycle.map
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.model.Exercise
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory

class WorkoutRepository(private val db: WorkoutDatabase)  {

    suspend fun insertWorkout(workout: Workout) = db.getWorkoutDao().insertWorkout(workout);

    suspend fun deleteWorkout(workout: Workout) = db.getWorkoutDao().deleteWorkout(workout);
    suspend fun updateWorkout(workout: Workout) = db.getWorkoutDao().updateWorkout(workout);

    fun getAllWorkouts() = db.getWorkoutDao().getAllWorkouts().map { list -> list.sortedBy { it.id }}
    fun getWorkoutsByDate(targetDate: String) = db.getWorkoutDao().getWorkoutsByDate(targetDate)
    suspend fun getWorkoutDates(): List<String> = db.getWorkoutDao().getAllWorkoutDates()

    suspend fun insertCategory(workoutCategory: WorkoutCategory) = db.getWorkoutDao().insertWorkoutCategory(workoutCategory)
    suspend fun deleteCategory(workoutCategory: WorkoutCategory) = db.getWorkoutDao().deleteWorkoutCategory(workoutCategory)
    suspend fun categoryExists(name:String):Boolean{return db.getWorkoutDao().countCategoryByName(name)>0}
    suspend fun updateCategory(category: WorkoutCategory) = db.getWorkoutDao().updateWorkoutCategory(category)

    fun getAllWorkoutCategory() = db.getWorkoutDao().getAllWorkoutCategory()
    suspend fun getAllCategoriesDirect(): List<WorkoutCategory> = db.getWorkoutDao().getAllCategoriesDirect()

    fun getExercisesByCategory(categoryId: Int) = db.getWorkoutDao().getExercisesByCategory(categoryId)
    suspend  fun getExercisesByCategoryDirect(categoryId: Int): List<Exercise> = db.getWorkoutDao().getExercisesByCategoryDirect(categoryId)
    suspend fun insertExercise(exercise: Exercise) = db.getWorkoutDao().insertExercise(exercise)
    suspend fun deleteExercise(exercise: Exercise) = db.getWorkoutDao().deleteExercise(exercise)
    suspend fun updateExercise(exercise: Exercise) = db.getWorkoutDao().updateExercise(exercise)
}
