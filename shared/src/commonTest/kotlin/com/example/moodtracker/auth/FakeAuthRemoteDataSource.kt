package com.example.moodtracker.auth

import com.data.remote.AuthRemoteDataSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthRemoteDataSource : AuthRemoteDataSource {
    var userId: String? = "u1"
    var signUpShouldFail: Boolean = false
    var signInShouldFail: Boolean = false
    var signOutShouldFail: Boolean = false
    var signUpCalledWith: Triple<String, String, String>? = null
    var signInCalledWith: Pair<String, String>? = null
    var signOutCalled: Boolean = false

    override val sessionStatus = MutableStateFlow<SessionStatus>(SessionStatus.NotAuthenticated(false))

    override suspend fun signUp(name: String, email: String, password: String) {
        if (signUpShouldFail) error("signup failed")
        signUpCalledWith = Triple(name, email, password)
    }

    override suspend fun signIn(email: String, password: String) {
        if (signInShouldFail) error("signin failed")
        signInCalledWith = email to password
    }

    override fun currentUserId(): String? = userId

    override suspend fun signOut() {
        if (signOutShouldFail) error("signout failed")
        signOutCalled = true
    }
}