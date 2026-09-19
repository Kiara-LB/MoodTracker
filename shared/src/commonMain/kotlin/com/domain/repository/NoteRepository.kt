package com.domain.repository

import com.domain.model.Mood
import com.domain.model.Note

interface NoteRepository {
    suspend fun saveNote(
        mood: Mood,
        feelingText: String,
        causeText: String,
        description: String
    ): Result<Unit>

    suspend fun getNotes(): Result<List<Note>>

    suspend fun updateNote(
        id: String,
        mood: Mood,
        feelingText: String,
        causeText: String,
        description: String
    ): Result<Unit>

    suspend fun deleteNote(id: String): Result<Unit>

}