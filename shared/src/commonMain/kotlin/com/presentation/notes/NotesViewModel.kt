package com.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Mood
import com.domain.model.Note
import com.domain.repository.AuthRepository
import com.domain.usecase.GetNotesUseCase
import com.domain.usecase.SaveNoteUseCase
import kotlinx.coroutines.launch
import com.domain.usecase.UpdateNoteUseCase
import io.github.jan.supabase.auth.status.SessionStatus

class NotesViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val authRepository: AuthRepository,

) : ViewModel() {
    var uiState by mutableStateOf(NotesUiState())
        private set

    init {
        viewModelScope.launch {
            authRepository.sessionStatus.collect { status ->
                if (status is SessionStatus.Authenticated) {
                    loadNotes()
                }
            }
        }
    }
    fun saveNote(mood: Mood, feelingText: String, causeText: String, description: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true, error = null)
            saveNoteUseCase(mood, feelingText, causeText, description)
                .onSuccess {
                    uiState = uiState.copy(isSaving = false)
                    loadNotes()
                }
                .onFailure { uiState = uiState.copy(isSaving = false, error = it.message) }
        }
    }
    fun loadNotes() {
        viewModelScope.launch {
            if (!uiState.hasLoadedOnce) {
                uiState = uiState.copy(isLoading = true, error = null)
            }
            getNotesUseCase()
                .onSuccess { notes ->
                    uiState = uiState.copy(isLoading = false, notes = notes, hasLoadedOnce = true)
                }
                .onFailure {
                    uiState = uiState.copy(isLoading = false, error = it.message)
                }
        }
    }
    fun updateNote(
        id: String,
        mood: Mood,
        feelingText: String,
        causeText: String,
        description: String
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true, error = null)
            updateNoteUseCase(id, mood, feelingText, causeText, description)
                .onSuccess {
                    uiState = uiState.copy(isSaving = false)
                    loadNotes()
                }
                .onFailure { uiState = uiState.copy(isSaving = false, error = it.message) }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isSaving = true, error = null)
            deleteNoteUseCase(id)
                .onSuccess {
                    uiState = uiState.copy(isSaving = false)
                    loadNotes()
                }
                .onFailure { uiState = uiState.copy(isSaving = false, error = it.message) }
        }
    }
    fun findNoteById(id: String): Note? = uiState.notes.find { it.id == id }
}