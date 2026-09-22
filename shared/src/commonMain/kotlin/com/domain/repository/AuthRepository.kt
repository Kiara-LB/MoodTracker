package com.domain.repository

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    fun currentUserId(): String?
//    fun currentUserName(): String?
//    fun currentUserAvatarId(): String?
//    suspend fun updateName(newName: String): Result<Unit>
//    suspend fun updateAvatar(avatarId: String): Result<Unit>
    val sessionStatus: kotlinx.coroutines.flow.Flow<io.github.jan.supabase.auth.status.SessionStatus>
}