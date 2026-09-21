package com.example.moodtracker.auth

import com.domain.repository.AuthRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow


class FakeAuthRepository : AuthRepository {
    var userId: String? = "u1"
    var signOutShouldFail: Boolean = false
    var signOutCalled: Boolean = false

    override suspend fun register(name: String, email: String, password: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun login(email: String, password: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun signOut(): Result<Unit> {
        if (signOutShouldFail) return Result.failure(RuntimeException("signout failed"))
        signOutCalled = true
        return Result.success(Unit)
    }

    override fun currentUserId(): String? = userId

    override val sessionStatus = MutableStateFlow<SessionStatus>(SessionStatus.NotAuthenticated(false))
}