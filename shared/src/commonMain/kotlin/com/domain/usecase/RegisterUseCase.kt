package com.domain.usecase

import com.domain.model.PredefinedAvatar
import com.domain.repository.AuthRepository
import com.domain.repository.ProfileRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String): Result<Unit> {
        return authRepository.register(name, email, password).mapCatching {
            profileRepository.updateProfile(
                name = name,
                avatarId = PredefinedAvatar.AVATAR_1.id
            ).onFailure { e ->
                e.printStackTrace()
            }.getOrThrow()
        }
    }
}