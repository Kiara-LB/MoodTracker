package com.data.remote

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow


interface AuthRemoteDataSource {
    val sessionStatus: Flow<SessionStatus>
    suspend fun signUp(name: String, email: String, password: String)
    suspend fun signIn(email: String, password: String)
    fun currentUserId(): String?
    suspend fun signOut()
}