package com.data.remote

import com.data.dto.ProfileDto
import com.data.dto.toDomain
import com.data.dto.toDto
import com.domain.model.Profile
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.api.AuthenticatedApiConfig
import io.github.jan.supabase.postgrest.Postgrest
class ProfileRemoteDataSource(
    private val auth: Auth,
    private val postgrest: Postgrest
) {
    suspend fun fetchProfile(userId: String): Profile {
        return postgrest.from("profiles")
            .select { filter { eq("user_id", userId) } }
            .decodeSingle<ProfileDto>()
            .toDomain()
    }

    suspend fun upsertProfile(profile: Profile) {
        postgrest.from("profiles").upsert(profile.toDto())
    }

    suspend fun signOut() = auth.signOut()

    fun currentUserId(): String? = auth.currentUserOrNull()?.id
}