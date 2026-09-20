package com.data.repository

import com.data.remote.AuthRemoteDataSource
import com.domain.repository.AuthRepository

class AuthRepositoryImpl(private val dataSource: AuthRemoteDataSource) : AuthRepository {
    override suspend fun register(name: String, email: String, password: String) = runCatching {
        dataSource.signUp(name, email, password)
    }
    override suspend fun login(email: String, password: String) = runCatching {
        dataSource.signIn(email, password)
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            dataSource.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override fun currentUserId() = dataSource.currentUserId()
    override fun currentUserName() = dataSource.currentUserName()
    override fun currentUserAvatarId() = dataSource.currentUserAvatarId()

    override suspend fun updateName(newName: String) = runCatching {
        dataSource.updateName(newName)
    }

    override suspend fun updateAvatar(avatarId: String) = runCatching {
        dataSource.updateAvatar(avatarId)
    }
}