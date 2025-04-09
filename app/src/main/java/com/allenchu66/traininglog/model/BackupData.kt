package com.allenchu66.traininglog.model

data class BackupData(
    val categories: List<WorkoutCategory>,
    val exercises: List<Exercise>,
    val workouts: List<Workout>
)