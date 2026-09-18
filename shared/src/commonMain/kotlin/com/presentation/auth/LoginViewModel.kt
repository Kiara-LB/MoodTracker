package com.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            loginUseCase(email, password)
                .onSuccess { uiState = uiState.copy(isLoading = false, success = true) }
                .onFailure { uiState = uiState.copy(isLoading = false, error = it.message) }
        }
    }
}