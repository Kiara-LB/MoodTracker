package com.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch


class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {
    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            registerUseCase(name, email, password)
                .onSuccess { uiState = uiState.copy(isLoading = false, success = true) }
                .onFailure { uiState = uiState.copy(isLoading = false, error = it.message) }
        }
    }
}
