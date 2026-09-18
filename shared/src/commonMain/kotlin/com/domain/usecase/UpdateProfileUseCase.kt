package com.domain.usecase

import com.domain.repository.ProfileRepository

class UpdateProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(name: String, avatarId: String, bannerColorId: String) =
        repository.updateProfile(name, avatarId, bannerColorId)
}
