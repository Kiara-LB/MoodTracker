package com.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: String? = null,
    @SerialName("user_id") val userId: String? = null,
    val mood: String,
    @SerialName("feeling_text") val feelingText: String,
    @SerialName("cause_text") val causeText: String,
    val description: String,
    @SerialName("created_at") val createdAt: String? = null
)