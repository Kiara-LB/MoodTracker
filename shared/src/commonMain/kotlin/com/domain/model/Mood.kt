package com.domain.model

import androidx.compose.ui.graphics.Color
import moodtracker.shared.generated.resources.Res
import moodtracker.shared.generated.resources.angry
import moodtracker.shared.generated.resources.anxious
import moodtracker.shared.generated.resources.calm
import moodtracker.shared.generated.resources.happy
import moodtracker.shared.generated.resources.inlove
import moodtracker.shared.generated.resources.sad
import org.jetbrains.compose.resources.DrawableResource

enum class Mood(
    val id: String,
    val displayName: String,
    val iconRes: DrawableResource,
    val color: Color
) {
    HAPPY("happy", "Feliz", Res.drawable.happy, Color(0xFFFFC107)),
    SAD("sad", "Triste", Res.drawable.sad, Color(0xFF64B5F6)),
    ANXIOUS("anxious", "Ansioso", Res.drawable.anxious, Color(0xFF68C87D)),
    ANGRY("angry", "Enojado", Res.drawable.angry, Color(0xFFE57373)),
    CALM("calm", "Tranquilo", Res.drawable.calm, Color(0xFFA681C7)),
    IN_LOVE("in_love", "Enamorado", Res.drawable.inlove, Color(0xFFF06292));


    companion object {
        fun fromId(id: String): Mood =
            entries.find { it.id == id } ?: HAPPY
    }
}