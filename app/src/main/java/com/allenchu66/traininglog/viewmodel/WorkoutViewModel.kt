package com.allenchu66.traininglog.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.model.Exercise
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import com.allenchu66.traininglog.model.WorkoutGroup
import com.allenchu66.traininglog.repository.WorkoutRepository

import kotlinx.coroutines.launch
import java.util.Calendar

class WorkoutViewModel(app:Application,private val workoutRepository: WorkoutRepository) : AndroidViewModel(app){
    fun addWorkout(workout: Workout) = viewModelScope.launch { workoutRepository.insertWorkout(workout) }
    fun removeWorkout(workout: Workout) = viewModelScope.launch { workoutRepository.deleteWorkout(workout) }
    fun updateWorkout(workout: Workout) = viewModelScope.launch { workoutRepository.updateWorkout(workout) }
    fun getAllWorkout() = workoutRepository.getAllWorkouts()

    fun getGroupedWorkoutByToday(): LiveData<List<WorkoutGroup>> {
        val todayStart = getTodayStartTimestamp()
        val todayEnd = getTodayEndTimestamp()

        val resultLiveData = MutableLiveData<List<WorkoutGroup>>()

        viewModelScope.launch {
            workoutRepository.getWorkoutsByDate(todayStart, todayEnd).observeForever { workoutList ->
                viewModelScope.launch {
                    val categories = workoutRepository.getAllCategoriesDirect()

                    // 只要有 categoryId 就參與分組（允許 exerciseId 為 null）
                    val grouped = workoutList
                        .filter { it.categoryId != null }
                        .groupBy { Pair(it.categoryId, it.exerciseId) }
                        .mapNotNull { (key, group) ->
                            val categoryId = key.first!!
                            val category = categories.find { it.id == categoryId }

                            val exercise = if (key.second != null) {
                                workoutRepository.getExercisesByCategoryDirect(categoryId)
                                    .find { it.id == key.second }
                            } else {
                                // 動作尚未選擇，建立虛擬項目
                                Exercise(
                                    id = -1,
                                    name = "請選擇動作",
                                    categoryId = categoryId
                                )
                            }

                            if (category != null && exercise != null) {
                                WorkoutGroup(category, exercise, group)
                            } else null
                        }

                    resultLiveData.postValue(grouped)
                }
            }
        }

        return resultLiveData
    }


    fun getGroupedWorkoutWithCategories(): LiveData<Pair<List<WorkoutGroup>, List<WorkoutCategory>>> {
        val result = MediatorLiveData<Pair<List<WorkoutGroup>, List<WorkoutCategory>>>()

        val groupLiveData = getGroupedWorkoutByToday()
        val categoryLiveData = getAllWorkoutCategory()

        var latestGroups: List<WorkoutGroup>? = null
        var latestCategories: List<WorkoutCategory>? = null

        result.addSource(groupLiveData) {
            latestGroups = it
            if (latestCategories != null) {
                result.value = Pair(it, latestCategories!!)
            }
        }

        result.addSource(categoryLiveData) {
            latestCategories = it
            if (latestGroups != null) {
                result.value = Pair(latestGroups!!, it)
            }
        }

        return result
    }



    fun getTodayStartTimestamp(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    fun getTodayEndTimestamp(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }



    fun addCategory(name:String) = viewModelScope.launch {
        if(workoutRepository.categoryExists(name)){
            Log.d("CategoryViewModel", "Category already exists")
            return@launch
        }
        workoutRepository.insertCategory(WorkoutCategory(name = name))
    }
    fun deleteCategory(workoutCategory: WorkoutCategory) = viewModelScope.launch { workoutRepository.deleteCategory(workoutCategory) }
    fun getAllWorkoutCategory() = workoutRepository.getAllWorkoutCategory()

    fun addExercise(name: String, categoryId: Int) = viewModelScope.launch { workoutRepository.insertExercise(
        Exercise(name = name, categoryId = categoryId)
    ) }

    fun getExercisesByCategory(categoryId: Int) = workoutRepository.getExercisesByCategory(categoryId)

    fun addWorkoutWithDefaults() {
        viewModelScope.launch {
            val categories = workoutRepository.getAllCategoriesDirect() // suspend func
            if (categories.isNotEmpty()) {
                val firstCategory = categories.first()
                val exercises = workoutRepository.getExercisesByCategoryDirect(firstCategory.id)
                val firstExerciseId = exercises.firstOrNull()?.id

                val workout = Workout(
                    categoryId = firstCategory.id,
                    exerciseId = null,
                    sets = 5,
                    reps = 12,
                    weight = 50f,
                    date = System.currentTimeMillis()
                )
                workoutRepository.insertWorkout(workout)
            }
        }
    }
}

