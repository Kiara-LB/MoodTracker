package com.data.repository

import com.data.remote.ProfileRemoteDataSource
import com.data.remote.ProfileRemoteDataSourceImpl
import com.domain.model.Profile
import com.domain.repository.AuthRepository
import com.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val dataSource: ProfileRemoteDataSource,
    private val authRepository: AuthRepository
) : ProfileRepository {

    override suspend fun getProfile(): Result<Profile> = runCatching {
        val userId = authRepository.currentUserId() ?: error("No hay sesión activa")
        dataSource.fetchProfile(userId)
    }

    override suspend fun updateProfile(name: String, avatarId: String): Result<Unit> =
        runCatching {
            val userId = authRepository.currentUserId() ?: error("No hay sesión activa")
            dataSource.upsertProfile(Profile(userId, name, avatarId))
        }
}