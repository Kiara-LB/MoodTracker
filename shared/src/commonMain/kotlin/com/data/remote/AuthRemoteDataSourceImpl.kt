package com.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
class AuthRemoteDataSourceImpl(private val auth: Auth) : AuthRemoteDataSource {
    override val sessionStatus: Flow<SessionStatus> get() = auth.sessionStatus

    override suspend fun signUp(name: String, email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject {
                put("name", name)
            }
        }
    }

    override suspend fun signIn(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override fun currentUserId(): String? = auth.currentUserOrNull()?.id
    override suspend fun signOut() = auth.signOut()
}