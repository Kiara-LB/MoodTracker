package com.data.remote

import com.data.dto.NoteDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order


class NoteRemoteDataSourceImpl(private val postgrest: Postgrest) : NoteRemoteDataSource {
    override suspend fun insertNote(dto: NoteDto) {
        postgrest.from("notes").insert(dto)
    }

    override suspend fun getNotes(): List<NoteDto> =
        postgrest.from("notes")
            .select { order("created_at", Order.DESCENDING) }
            .decodeList<NoteDto>()

    override suspend fun updateNote(id: String, dto: NoteDto) {
        postgrest.from("notes")
            .update(dto) { filter { eq("id", id) } }
    }

    override suspend fun deleteNote(id: String) {
        postgrest.from("notes").delete { filter { eq("id", id) } }
    }
}