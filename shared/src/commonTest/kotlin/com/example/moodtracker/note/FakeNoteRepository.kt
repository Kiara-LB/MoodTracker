package com.example.moodtracker.note


import com.domain.model.Mood
import com.domain.model.Note
import com.domain.repository.NoteRepository

class FakeNoteRepository : NoteRepository {
    var notesResult: Result<List<Note>> = Result.success(emptyList())
    var saveResult: Result<Unit> = Result.success(Unit)
    var updateResult: Result<Unit> = Result.success(Unit)
    var deleteResult: Result<Unit> = Result.success(Unit)

    var saveCalledWith: List<String>? = null
    var updateCalledWith: Pair<String, List<String>>? = null
    var deleteCalledWith: String? = null

    override suspend fun saveNote(mood: Mood, feelingText: String, causeText: String, description: String): Result<Unit> {
        saveCalledWith = listOf(mood.id, feelingText, causeText, description)
        return saveResult
    }

    override suspend fun getNotes(): Result<List<Note>> = notesResult

    override suspend fun updateNote(id: String, mood: Mood, feelingText: String, causeText: String, description: String): Result<Unit> {
        updateCalledWith = id to listOf(mood.id, feelingText, causeText, description)
        return updateResult
    }

    override suspend fun deleteNote(id: String): Result<Unit> {
        deleteCalledWith = id
        return deleteResult
    }
}