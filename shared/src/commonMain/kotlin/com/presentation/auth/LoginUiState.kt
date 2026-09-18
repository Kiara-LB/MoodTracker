package com.presentation.auth

data class LoginUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)