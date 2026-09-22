package com.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.AvatarOption
import com.domain.model.Profile
import com.domain.repository.AuthRepository
import com.domain.repository.ProfileRepository
import com.domain.usecase.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val logoutUseCase: LogoutUseCase

) : ViewModel() {
    var uiState by mutableStateOf(ProfileUiState())
        private set
    private var currentProfile: Profile? = null

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().onSuccess { profile ->
                currentProfile = profile
                uiState = uiState.copy(
                    name = profile.name,
                    avatar = AvatarOption.fromId(profile.avatarId)
                )
            }
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            val avatarId = currentProfile?.avatarId ?: AvatarOption.CAT.id
            profileRepository.updateProfile(newName, avatarId)
                .onSuccess { loadProfile() }
        }
    }
    fun updateAvatar(avatar: AvatarOption) {
        val profile = currentProfile ?: return
        viewModelScope.launch {
            profileRepository.updateProfile(profile.name, avatar.id)
                .onSuccess { loadProfile() }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            logoutUseCase()
                .onSuccess { onLoggedOut() }
                .onFailure { uiState = uiState.copy(isLoading = false, error = it.message) }
        }
    }
}