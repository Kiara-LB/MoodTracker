package com.domain.model

enum class Mood(val id: String, val displayName: String) {
    HAPPY("happy", "Feliz"),
    SAD("sad", "Triste"),
    ANXIOUS("anxious", "Ansioso"),
    ANGRY("angry", "Enojado"),
    CALM("calm", "Tranquilo"),
    IN_LOVE("in_love", "Enamorado");

    companion object {
        fun fromId(id: String): Mood =
            entries.find { it.id == id } ?: HAPPY
    }
}