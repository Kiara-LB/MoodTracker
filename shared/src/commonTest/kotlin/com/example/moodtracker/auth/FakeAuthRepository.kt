package com.example.moodtracker.auth

import com.domain.repository.AuthRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow


class FakeAuthRepository : AuthRepository {
    var userId: String? = "u1"
    var signOutShouldFail: Boolean = false
    var signOutCalled: Boolean = false
    var registerResult: Result<Unit> = Result.success(Unit)
    var loginResult: Result<Unit> = Result.success(Unit)
    var registerCalledWith: Triple<String, String, String>? = null
    var loginCalledWith: Pair<String, String>? = null

    override suspend fun register(name: String, email: String, password: String): Result<Unit> {
        registerCalledWith = Triple(name, email, password)
        return registerResult
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        loginCalledWith = email to password
        return loginResult
    }


    override suspend fun signOut(): Result<Unit> {
        if (signOutShouldFail) return Result.failure(RuntimeException("signout failed"))
        signOutCalled = true
        return Result.success(Unit)
    }

    override fun currentUserId(): String? = userId

    override val sessionStatus = MutableStateFlow<SessionStatus>(SessionStatus.NotAuthenticated(false))
}