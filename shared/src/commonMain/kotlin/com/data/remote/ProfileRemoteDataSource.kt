package com.data.remote

import com.domain.model.Profile

interface ProfileRemoteDataSource{
    suspend fun fetchProfile(userId: String): Profile
    suspend fun upsertProfile(profile: Profile)
}
