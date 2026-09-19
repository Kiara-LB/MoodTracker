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
    HAPPY("happy", "Feliz", Res.drawable.happy, Color(0xFFFBEBB2)),
    SAD("sad", "Triste", Res.drawable.sad, Color(0xFFBCD9FC)),
    ANXIOUS("anxious", "Ansioso", Res.drawable.anxious, Color(0xFFCCEDD4)),
    ANGRY("angry", "Enojado", Res.drawable.angry, Color(0xFFF38383)),
    CALM("calm", "Tranquilo", Res.drawable.calm, Color(0xFFDFBBFD)),
    IN_LOVE("in_love", "Enamorado", Res.drawable.inlove, Color(0xFFFF8CBE));


    companion object {
        fun fromId(id: String): Mood =
            entries.find { it.id == id } ?: HAPPY
    }
}