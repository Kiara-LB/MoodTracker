package com.data.remote

import com.data.dto.ProfileDto
import com.data.dto.toDomain
import com.data.dto.toDto
import com.domain.model.Profile
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
class ProfileRemoteDataSourceImpl(
    private val postgrest: Postgrest
) : ProfileRemoteDataSource {
    override suspend fun fetchProfile(userId: String): Profile {
        return postgrest.from("profiles")
            .select { filter { eq("user_id", userId) } }
            .decodeSingle<ProfileDto>()
            .toDomain()
    }

    override suspend fun upsertProfile(profile: Profile) {
        postgrest.from("profiles").upsert(profile.toDto())
    }
}