package com.data.remote

import com.data.dto.NoteDto

interface NoteRemoteDataSource {
    suspend fun insertNote(dto: NoteDto)
    suspend fun getNotes(): List<NoteDto>
    suspend fun updateNote(id: String, dto: NoteDto)
    suspend fun deleteNote(id: String)
}