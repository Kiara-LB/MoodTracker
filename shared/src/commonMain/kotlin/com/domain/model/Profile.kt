package com.domain.model

data class Profile(
    val userId: String,
    val name: String,
    val avatarId: String,
    val bannerColorId: String
)

enum class PredefinedAvatar(val id: String, val resourceName: String) {
    AVATAR_1("avatar_1", "ic_avatar_1"),
    AVATAR_2("avatar_2", "ic_avatar_2"),
    AVATAR_3("avatar_3", "ic_avatar_3"),
    AVATAR_4("avatar_4", "ic_avatar_4");

    companion object {
        fun fromId(id: String) = entries.find { it.id == id } ?: AVATAR_1
    }
}

enum class PredefinedBanner(val id: String, val color: Long) {
    BLUE("banner_blue", 0xFF3B82F6),
    GREEN("banner_green", 0xFF22C55E),
    PURPLE("banner_purple", 0xFF8B5CF6),
    ORANGE("banner_orange", 0xFFF97316);

    companion object {
        fun fromId(id: String) = entries.find { it.id == id } ?: BLUE
    }
}