package com.allenchu66.traininglog.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.allenchu66.traininglog.databinding.MainActivityBinding
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.repository.WorkoutRepository
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel
import com.allenchu66.traininglog.viewmodel.WorkoutViewModelFactory
import com.google.android.ads.mediationtestsuite.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity(){

    private lateinit var binding: MainActivityBinding;
    lateinit var workoutViewModel : WorkoutViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWorkoutViewModel()
    }

    private fun initWorkoutViewModel(){
        val workoutRepository  = WorkoutRepository(WorkoutDatabase.getDatabase(this))
        val viewModelProviderFactory = WorkoutViewModelFactory(application,workoutRepository)
        workoutViewModel = ViewModelProvider(this,viewModelProviderFactory)[WorkoutViewModel::class.java]
    }
}

