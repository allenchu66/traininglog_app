package com.allenchu66.traininglog.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.allenchu66.traininglog.CategoryType
import com.allenchu66.traininglog.database.WorkoutDatabase.Companion.getDatabase
import com.allenchu66.traininglog.model.Exercise
import com.allenchu66.traininglog.model.Workout
import com.allenchu66.traininglog.model.WorkoutCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@Database(entities = [Workout::class, WorkoutCategory::class,Exercise::class], version = 1)
abstract class WorkoutDatabase : RoomDatabase() {
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
                            INSTANCE?.getWorkoutDao()?.apply {
                                insertCategories(
                                    listOf(
                                        WorkoutCategory(name = "胸"),
                                        WorkoutCategory(name = "背"),
                                        WorkoutCategory(name = "腿"),
                                        WorkoutCategory(name = "手"),
                                        WorkoutCategory(name = "肩")
                                    )
                                )

                                //init chest exercise
                                insertExercise(Exercise(name = "啞鈴臥推(上斜)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "啞鈴臥推(平板)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "啞鈴臥推(下斜)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "槓鈴臥推(上斜)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "槓鈴臥推(平板)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "槓鈴臥推(下斜)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "Cable夾胸", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "機械胸推(上胸)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "機械胸推(下胸)", categoryId = CategoryType.CHEST.id))
                                insertExercise(Exercise(name = "雙槓胸推(下胸)", categoryId = CategoryType.CHEST.id))

                                //init back exercise
                                insertExercise(Exercise(name = "滑輪下拉", categoryId = CategoryType.BACK.id))
                                insertExercise(Exercise(name = "坐姿划船", categoryId = CategoryType.BACK.id))
                                insertExercise(Exercise(name = "下背伸展", categoryId = CategoryType.BACK.id))

                                //init shoulder exercise
                                insertExercise(Exercise(name = "啞鈴坐姿肩推", categoryId =  CategoryType.SHOULDERS.id))
                                insertExercise(Exercise(name = "啞鈴側平舉", categoryId = CategoryType.SHOULDERS.id))
                                insertExercise(Exercise(name = "蝴蝶飛鳥", categoryId = CategoryType.SHOULDERS.id))
                                insertExercise(Exercise(name = "Cable面拉", categoryId = CategoryType.SHOULDERS.id))

                                //init leg exercise
                                insertExercise(Exercise(name = "槓鈴深蹲", categoryId = CategoryType.LEGS.id))
                                insertExercise(Exercise(name = "史密斯深蹲", categoryId = CategoryType.LEGS.id))
                                insertExercise(Exercise(name = "坐姿腿伸(前側)", categoryId = CategoryType.LEGS.id))
                                insertExercise(Exercise(name = "臥姿腿彎舉(後側)", categoryId = CategoryType.LEGS.id))
                                insertExercise(Exercise(name = "坐姿腿彎舉(後側)", categoryId = CategoryType.LEGS.id))
                                insertExercise(Exercise(name = "站立式小腿", categoryId = CategoryType.LEGS.id))

                                //init arm exercise
                                insertExercise(Exercise(name = "Cable三頭下壓", categoryId = CategoryType.ARMS.id))
                                insertExercise(Exercise(name = "Cable三頭屈伸", categoryId = CategoryType.ARMS.id))
                                insertExercise(Exercise(name = "啞鈴三頭伸展", categoryId = CategoryType.ARMS.id))
                                insertExercise(Exercise(name = "Cable二頭彎舉", categoryId = CategoryType.ARMS.id))
                                insertExercise(Exercise(name = "槓鈴二頭彎舉", categoryId = CategoryType.ARMS.id))


                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
