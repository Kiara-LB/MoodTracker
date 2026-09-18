package com.data.repository

import com.data.remote.ProfileRemoteDataSource
import com.domain.model.Profile
import com.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val dataSource: ProfileRemoteDataSource
) : ProfileRepository {

    override suspend fun getProfile(): Result<Profile> = runCatching {
        val userId = dataSource.currentUserId() ?: error("No hay sesión activa")
        dataSource.fetchProfile(userId)
    }

    override suspend fun updateProfile(name: String, avatarId: String, bannerColorId: String): Result<Unit> =
        runCatching {
            val userId = dataSource.currentUserId() ?: error("No hay sesión activa")
            dataSource.upsertProfile(Profile(userId, name, avatarId, bannerColorId))
        }

    override suspend fun logout(): Result<Unit> = runCatching {
        dataSource.signOut()
    }
}