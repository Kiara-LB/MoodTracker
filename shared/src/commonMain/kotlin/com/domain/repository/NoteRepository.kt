package com.domain.repository

import com.domain.model.Note

interface NoteRepository {
    suspend fun saveNote(
        mood: com.domain.model.Mood,
        feelingText: String,
        causeText: String,
        description: String
    ): Result<Unit>

    suspend fun getNotes(): Result<List<Note>>
}