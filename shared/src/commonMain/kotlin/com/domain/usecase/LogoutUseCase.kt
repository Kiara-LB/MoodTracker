package com.domain.usecase

import com.domain.repository.AuthRepository
import com.domain.repository.ProfileRepository

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.signOut()
}