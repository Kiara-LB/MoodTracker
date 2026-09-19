package com.domain.model

data class DayMood(
    val dayLabel: String,
    val mood: Mood?
)

data class MoodPercentage(
    val mood: Mood,
    val percentage: Float
)