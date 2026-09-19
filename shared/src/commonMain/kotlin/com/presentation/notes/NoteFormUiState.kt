package com.presentation.notes

data class NoteFormUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)