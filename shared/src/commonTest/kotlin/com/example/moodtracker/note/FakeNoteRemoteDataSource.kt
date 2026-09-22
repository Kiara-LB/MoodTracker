package com.example.moodtracker.note

import com.data.dto.NoteDto
import com.data.remote.NoteRemoteDataSource

class FakeNoteRemoteDataSource : NoteRemoteDataSource {
    var notesToReturn: List<NoteDto> = emptyList()
    var insertShouldFail: Boolean = false
    var updateShouldFail: Boolean = false
    var deleteShouldFail: Boolean = false
    var insertCalledWith: NoteDto? = null
    var updateCalledWith: Pair<String, NoteDto>? = null
    var deleteCalledWith: String? = null

    override suspend fun insertNote(dto: NoteDto) {
        if (insertShouldFail) error("insert failed")
        insertCalledWith = dto
    }

    override suspend fun getNotes(): List<NoteDto> = notesToReturn

    override suspend fun updateNote(id: String, dto: NoteDto) {
        if (updateShouldFail) error("update failed")
        updateCalledWith = id to dto
    }

    override suspend fun deleteNote(id: String) {
        if (deleteShouldFail) error("delete failed")
        deleteCalledWith = id
    }
}