package com.data.repository

import com.data.remote.NoteRemoteDataSource
import com.data.dto.NoteDto
import com.data.remote.mapper.toDomain
import com.domain.model.Mood
import com.domain.model.Note
import com.domain.repository.NoteRepository


class NoteRepositoryImpl(
    private val dataSource: NoteRemoteDataSource
) : NoteRepository {

    override suspend fun saveNote(
        mood: Mood,
        feelingText: String,
        causeText: String,
        description: String
    ) = runCatching {
        dataSource.insertNote(
            NoteDto(
                mood = mood.id,
                feelingText = feelingText,
                causeText = causeText,
                description = description
            )
        )
    }

    override suspend fun getNotes(): Result<List<Note>> = runCatching {
        dataSource.getNotes().map { it.toDomain() }
    }

    override suspend fun updateNote(
        id: String,
        mood: Mood,
        feelingText: String,
        causeText: String,
        description: String
    ) = runCatching {
        dataSource.updateNote(
            id,
            NoteDto(
                mood = mood.id,
                feelingText = feelingText,
                causeText = causeText,
                description = description
            )
        )
    }
    override suspend fun deleteNote(id: String) = runCatching {
        dataSource.deleteNote(id)
    }
}