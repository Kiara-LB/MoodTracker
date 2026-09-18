package com.domain.repository

import com.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Result<Profile>
    suspend fun updateProfile(name: String, avatarId: String, bannerColorId: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}