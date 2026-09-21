package com.data.dto

import com.domain.model.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ProfileDto(
    @SerialName("user_id") val userId: String,
    val name: String,
    @SerialName("avatar_id") val avatarId: String,
)

fun ProfileDto.toDomain() = Profile(userId, name, avatarId)
fun Profile.toDto() = ProfileDto(userId, name, avatarId)