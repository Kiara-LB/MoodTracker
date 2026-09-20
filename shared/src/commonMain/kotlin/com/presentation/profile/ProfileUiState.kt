package com.presentation.profile

import com.domain.model.AvatarOption

data class ProfileUiState(
    val name: String = "",
    val avatar: AvatarOption = AvatarOption.CAT,
    val isLoading: Boolean = false,
    val error: String? = null
)