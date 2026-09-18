package com.data.remote.mapper

import com.data.dto.NoteDto
import com.domain.model.Mood
import com.domain.model.Note
import kotlin.time.Instant


fun NoteDto.toDomain(): Note = Note(
    id = id ?: "",
    mood = Mood.fromId(mood),
    feelingText = feelingText,
    causeText = causeText,
    description = description,
    createdAt = createdAt?.let { Instant.parse(it) } ?: Instant.DISTANT_PAST
)