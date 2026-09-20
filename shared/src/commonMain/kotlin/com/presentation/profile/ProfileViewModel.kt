package com.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.AvatarOption
import com.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var uiState by mutableStateOf(ProfileUiState())
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        uiState = uiState.copy(
            name = authRepository.currentUserName() ?: "Usuario",
            avatar = AvatarOption.fromId(authRepository.currentUserAvatarId())
        )
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            authRepository.updateName(newName).onSuccess { loadProfile() }
        }
    }

    fun updateAvatar(avatar: AvatarOption) {
        viewModelScope.launch {
            authRepository.updateAvatar(avatar.id).onSuccess { loadProfile() }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            authRepository.signOut()
                .onSuccess { onLoggedOut() }
                .onFailure { uiState = uiState.copy(isLoading = false, error = it.message) }
        }
    }
}