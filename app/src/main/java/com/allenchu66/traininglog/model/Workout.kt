package com.allenchu66.traininglog.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "workout")
@Parcelize
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // 自動生成 ID
    val category: String, // 訓練類別（胸、背、腿）
    val exercise: String, // 訓練動作（槓鈴胸推、啞鈴胸推）
    val sets: Int,        // 組數
    val reps: Int,        // 每組次數
    val weight: Float,    // 重量（kg）
    val date: Long,       // 訓練日期（以 timestamp 存）
    val notes: String? = null  // 備註（可選）
):Parcelable