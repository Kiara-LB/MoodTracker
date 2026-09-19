package com.presentation.notes

import com.domain.repository.NoteRepository

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(id: String) = repository.deleteNote(id)
}