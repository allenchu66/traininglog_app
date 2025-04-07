package com.allenchu66.traininglog.activity

import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.databinding.MainActivityBinding
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.repository.WorkoutRepository
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel
import com.allenchu66.traininglog.viewmodel.WorkoutViewModelFactory
import com.google.android.ads.mediationtestsuite.viewmodels.ViewModelFactory

class MainActivity : AppCompatActivity(){

    private lateinit var binding: MainActivityBinding;
    lateinit var workoutViewModel : WorkoutViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        initWorkoutViewModel()

        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController

        // 設定漢堡選單控制 Drawer
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        NavigationUI.setupWithNavController(binding.navView, navController)
    }



    private fun initWorkoutViewModel(){
        val workoutRepository  = WorkoutRepository(WorkoutDatabase.getDatabase(this))
        val viewModelProviderFactory = WorkoutViewModelFactory(application,workoutRepository)
        workoutViewModel = ViewModelProvider(this,viewModelProviderFactory)[WorkoutViewModel::class.java]
    }
}

