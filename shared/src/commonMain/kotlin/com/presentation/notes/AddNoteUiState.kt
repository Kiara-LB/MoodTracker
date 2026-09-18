package com.presentation.notes

data class AddNoteUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)