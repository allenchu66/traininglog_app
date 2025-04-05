package com.allenchu66.traininglog.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise",
    foreignKeys = [ForeignKey(
        entity = WorkoutCategory::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["name"], unique = true)]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: Int,  // 關聯到 `WorkoutCategory`
    val name: String      // 訓練動作名稱（如：啞鈴臥推）
){
    override fun toString(): String {
        return name
    }
}