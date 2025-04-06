package com.allenchu66.traininglog.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


//  onDelete = ForeignKey.CASCADE // 如果類別被刪除，對應的 Workout 也會被刪除
//CASCADE	刪除 category 同時刪掉所有相關的 workout
//SET NULL	刪除 category，相關的 workout 的 categoryId 會變成 null
//RESTRICT	有 workout 使用這個 category 時，不允許刪除 category
//NO ACTION	跟 RESTRICT 類似（延遲檢查），但行為更依賴資料庫實作
@Entity(
    tableName = "workout",
    foreignKeys = [ForeignKey(
        entity = WorkoutCategory::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.RESTRICT // 有 workout 使用這個 category 時，不允許刪除 category
    ),ForeignKey(
        entity = Exercise::class,
        parentColumns = ["id"],
        childColumns = ["exerciseId"],
        onDelete = ForeignKey.RESTRICT // 有 workout 使用這個 exercise 時，不允許刪除 exercise
    )]
)
@Parcelize
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // 自動生成 ID
    var categoryId: Int?, // 訓練類別（胸、背、腿） 使用 categoryId，而不是直接存名稱
    var exerciseId: Int? = null, // 訓練動作（槓鈴胸推、啞鈴胸推）
    val sets: Int,        // 組數
    var reps: Int,        // 每組次數
    var weight: Float,    // 重量（kg）
    val date: Long,       // 訓練日期（以 timestamp 存）
    val notes: String? = null  // 備註（可選）
):Parcelable