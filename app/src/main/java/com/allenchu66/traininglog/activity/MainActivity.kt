package com.allenchu66.traininglog.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.allenchu66.traininglog.R
import com.allenchu66.traininglog.databinding.MainActivityBinding
import com.allenchu66.traininglog.database.WorkoutDatabase
import com.allenchu66.traininglog.model.BackupData
import com.allenchu66.traininglog.repository.WorkoutRepository
import com.allenchu66.traininglog.viewmodel.WorkoutViewModel
import com.allenchu66.traininglog.viewmodel.WorkoutViewModelFactory
import com.google.android.ads.mediationtestsuite.viewmodels.ViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    lateinit var workoutViewModel: WorkoutViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration  // 👈 必須設為全域

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        initWorkoutViewModel()

        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment),
            binding.drawerLayout
        )

        // 讓 Toolbar 顯示正確（漢堡或返回）並接管返回事件
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        // DrawerLayout + NavigationView 的配對（選單）
        //NavigationUI.setupWithNavController(binding.navView, navController)
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> navController.navigate(R.id.homeFragment)
                R.id.nav_edit_category -> navController.navigate(R.id.editCategoryFragment)
                R.id.nav_edit_exercise -> navController.navigate(R.id.editExerciseFragment)

                R.id.nav_export -> {
                    showExportDialogOrStartExport()
                }

                R.id.nav_import -> {
                    importLauncher.launch(arrayOf("application/json")) // 只允許選擇
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun showExportDialogOrStartExport() {
        workoutViewModel.exportData(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    private fun initWorkoutViewModel() {
        val workoutRepository = WorkoutRepository(WorkoutDatabase.getDatabase(this))
        val viewModelProviderFactory = WorkoutViewModelFactory(application, workoutRepository)
        workoutViewModel = ViewModelProvider(this, viewModelProviderFactory)[WorkoutViewModel::class.java]
    }

    private val importLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            importTrainingDataFromUri(uri)
        }
    }

    private fun importTrainingDataFromUri(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val json = inputStream?.bufferedReader()?.use { it.readText() } ?: return
            val backupData = Gson().fromJson(json, BackupData::class.java)

            lifecycleScope.launch {
                // 這裡可以先清空原有資料
                workoutViewModel.replaceAllData(backupData)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "匯入完成！", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "匯入失敗：${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}


