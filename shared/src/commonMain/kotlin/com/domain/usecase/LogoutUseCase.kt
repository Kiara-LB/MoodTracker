package com.domain.usecase

import com.domain.repository.ProfileRepository

class LogoutUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() = repository.logout()
}