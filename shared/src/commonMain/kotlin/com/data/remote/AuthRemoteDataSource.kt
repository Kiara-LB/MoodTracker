package com.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
class AuthRemoteDataSource(private val auth: Auth) {
    suspend fun signUp(name: String, email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject {
                put("name", name)
            }
        }
    }
    suspend fun signIn(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }
    fun currentUserId(): String? = auth.currentUserOrNull()?.id
}