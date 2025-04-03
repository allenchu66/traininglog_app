package com.allenchu66.traininglog.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.allenchu66.traininglog.repository.WorkoutRepository

class WorkoutViewModelFactory(val app:Application,private val workoutRepository: WorkoutRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WorkoutViewModel(app,workoutRepository) as T
    }
}