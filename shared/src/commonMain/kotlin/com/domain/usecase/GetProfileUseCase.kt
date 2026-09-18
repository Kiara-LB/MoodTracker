package com.domain.usecase

import com.domain.repository.ProfileRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke() = repository.getProfile()
}
