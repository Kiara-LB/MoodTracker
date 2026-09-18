package com.domain.usecase
import com.domain.repository.NoteRepository
import com.domain.model.Mood


class SaveNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(
        mood: Mood,
        feelingText: String,
        causeText: String,
        description: String
    ) = repository.saveNote(mood, feelingText, causeText, description)
}