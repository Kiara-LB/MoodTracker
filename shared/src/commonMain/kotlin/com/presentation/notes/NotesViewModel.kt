package com.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.launch


class NotesViewModel(private val getNotesUseCase: GetNotesUseCase) : ViewModel() {
    var uiState by mutableStateOf(NotesUiState())
        private set

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            getNotesUseCase()
                .onSuccess { notes -> uiState = uiState.copy(isLoading = false, notes = notes) }
                .onFailure { uiState = uiState.copy(isLoading = false, error = it.message) }
        }
    }
}