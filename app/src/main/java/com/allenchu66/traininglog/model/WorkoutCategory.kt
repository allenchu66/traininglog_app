package com.allenchu66.traininglog.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class WorkoutCategory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String // 訓練部位名稱（胸、背、腿...）
)