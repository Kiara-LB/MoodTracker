package com.domain.model

import kotlin.time.Instant


data class Note(
    val id: String,
    val mood: Mood,
    val feelingText: String,
    val causeText: String,
    val description: String,
    val createdAt: Instant
)