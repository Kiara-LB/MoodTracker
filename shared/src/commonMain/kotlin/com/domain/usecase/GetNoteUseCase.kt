package com.domain.usecase

import com.domain.repository.NoteRepository

class GetNotesUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke() = repository.getNotes()
}