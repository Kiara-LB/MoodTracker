package com.domain.model


import moodtracker.shared.generated.resources.Res
import moodtracker.shared.generated.resources.bear_avatar
import moodtracker.shared.generated.resources.cat_avatar
import moodtracker.shared.generated.resources.cow_avatar
import moodtracker.shared.generated.resources.dog_avatar
import moodtracker.shared.generated.resources.duck_avatar
import org.jetbrains.compose.resources.DrawableResource

enum class AvatarOption(val id: String, val imageRes: DrawableResource) {
    CAT("cat", Res.drawable.cat_avatar),
    DOG("dog", Res.drawable.dog_avatar),
    FOX("cow", Res.drawable.cow_avatar),
    BEAR("bear", Res.drawable.bear_avatar),
    BUNNY("duck", Res.drawable.duck_avatar);

    companion object {
        fun fromId(id: String?): AvatarOption =
            entries.find { it.id == id } ?: CAT
    }
}