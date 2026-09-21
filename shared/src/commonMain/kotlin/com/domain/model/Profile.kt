package com.domain.model

data class Profile(
    val userId: String,
    val name: String,
    val avatarId: String,
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

