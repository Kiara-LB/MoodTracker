package com.example.moodtracker.profile

import com.domain.model.Profile
import com.domain.repository.ProfileRepository


class FakeProfileRepository : ProfileRepository {
    var profileResult: Result<Profile> = Result.success(Profile("u1", "Test", "avatar_1"))
    var updateResult: Result<Unit> = Result.success(Unit)
    var updateProfileCalledWith: Pair<String, String>? = null

    override suspend fun getProfile(): Result<Profile> = profileResult

    override suspend fun updateProfile(name: String, avatarId: String): Result<Unit> {
        updateProfileCalledWith = name to avatarId
        return updateResult
    }
}