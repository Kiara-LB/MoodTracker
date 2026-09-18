package com.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.Mood
import com.domain.usecase.SaveNoteUseCase
import kotlinx.coroutines.launch

class AddNoteViewModel(private val saveNoteUseCase: SaveNoteUseCase) : ViewModel() {
    var uiState by mutableStateOf(AddNoteUiState())
        private set

    fun saveNote(mood: Mood, feelingText: String, causeText: String, description: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            saveNoteUseCase(mood, feelingText, causeText, description)
                .onSuccess { uiState = uiState.copy(isLoading = false, success = true) }
                .onFailure { uiState = uiState.copy(isLoading = false, error = it.message) }
        }
    }
}