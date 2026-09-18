package com.data.remote

import com.data.dto.NoteDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order


class NoteRemoteDataSource(private val postgrest: Postgrest) {

    suspend fun insertNote(dto: NoteDto) {
        postgrest.from("notes").insert(dto)
    }

    suspend fun getNotes(): List<NoteDto> =
        postgrest.from("notes")
            .select {
                order("created_at", Order.DESCENDING)
            }
            .decodeList<NoteDto>()

    suspend fun updateNote(id: String, dto: NoteDto) {
        postgrest.from("notes")
            .update(dto) {
                filter { eq("id", id) }
            }
    }
    suspend fun deleteNote(id: String) {
        postgrest.from("notes").delete {
            filter { eq("id", id) }
        }
    }
}