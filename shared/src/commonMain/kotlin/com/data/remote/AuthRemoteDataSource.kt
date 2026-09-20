package com.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
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
    suspend fun signOut() = auth.signOut()
    fun currentUserName(): String? =
        auth.currentUserOrNull()?.userMetadata?.get("name")?.jsonPrimitive?.content

    fun currentUserAvatarId(): String? =
        auth.currentUserOrNull()?.userMetadata?.get("avatar_id")?.jsonPrimitive?.content

    private suspend fun updateMetadata(newFields: Map<String, String>) {
        val current = auth.currentUserOrNull()?.userMetadata
        auth.updateUser {
            data = buildJsonObject {
                current?.forEach { (key, value) -> put(key, value) }
                newFields.forEach { (key, value) -> put(key, value) }
            }
        }
    }

    suspend fun updateName(newName: String) = updateMetadata(mapOf("name" to newName))

    suspend fun updateAvatar(avatarId: String) = updateMetadata(mapOf("avatar_id" to avatarId))

}