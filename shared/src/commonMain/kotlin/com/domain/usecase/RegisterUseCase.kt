package com.domain.usecase

import com.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        repository.register(name, email, password)
}