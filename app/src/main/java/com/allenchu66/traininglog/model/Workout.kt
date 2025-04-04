package com.allenchu66.traininglog.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = "workout",
    foreignKeys = [ForeignKey(
        entity = WorkoutCategory::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE // 如果類別被刪除，對應的 Workout 也會被刪除
    )]
)
@Parcelize
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // 自動生成 ID
    var categoryId: Int?, // 訓練類別（胸、背、腿） 使用 categoryId，而不是直接存名稱
    val exercise: String, // 訓練動作（槓鈴胸推、啞鈴胸推）
    val sets: Int,        // 組數
    val reps: Int,        // 每組次數
    val weight: Float,    // 重量（kg）
    val date: Long,       // 訓練日期（以 timestamp 存）
    val notes: String? = null  // 備註（可選）
):Parcelable