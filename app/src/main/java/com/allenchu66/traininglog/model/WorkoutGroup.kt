package com.allenchu66.traininglog.model

data class WorkoutGroup(
    val category: WorkoutCategory,
    val exercise: Exercise,
    val workouts: List<Workout>
)
