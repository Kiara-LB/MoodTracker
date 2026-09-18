package com.presentation.auth

data class RegisterUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)