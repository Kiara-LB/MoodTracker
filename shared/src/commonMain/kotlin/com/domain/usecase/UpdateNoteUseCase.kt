package com.domain.usecase

import com.domain.model.Mood
import com.domain.repository.NoteRepository

class UpdateNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(
        id: String,
        mood: Mood,
        feelingText: String,
        causeText: String,
        description: String
    ) = repository.updateNote(id, mood, feelingText, causeText, description)
}