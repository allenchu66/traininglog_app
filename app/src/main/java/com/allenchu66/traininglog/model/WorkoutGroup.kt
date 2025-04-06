package com.allenchu66.traininglog.model

data class WorkoutGroup(
    var category: WorkoutCategory,
    var exercise: Exercise,
    val workouts: List<Workout>
)
