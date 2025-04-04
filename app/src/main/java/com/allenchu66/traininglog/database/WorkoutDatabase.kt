package com.allenchu66.traininglog.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.allenchu66.traininglog.database.WorkoutDatabase.Companion.getDatabase
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [Workout::class,WorkoutCategory::class], version = 1)
abstract class WorkoutDatabase : RoomDatabase(){
    abstract fun getWorkoutDao(): WorkoutDao
    companion object {
        @Volatile
        private var INSTANCE: WorkoutDatabase? = null

        fun getDatabase(context: Context): WorkoutDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkoutDatabase::class.java,
                    "workout_database"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // 在背景執行插入預設資料
                        CoroutineScope(Dispatchers.IO).launch {
                            // 確保資料庫實例已經初始化，然後再執行插入
                            INSTANCE?.getWorkoutDao()?.insertCategories(
                                listOf(
                                    WorkoutCategory(name = "胸"),
                                    WorkoutCategory(name = "背"),
                                    WorkoutCategory(name = "腿"),
                                    WorkoutCategory(name = "手"),
                                    WorkoutCategory(name = "肩")
                                )
                            )
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
