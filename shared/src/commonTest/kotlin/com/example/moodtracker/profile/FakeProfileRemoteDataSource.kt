package com.example.moodtracker.profile

import com.data.remote.ProfileRemoteDataSource
import com.data.remote.ProfileRemoteDataSourceImpl
import com.domain.model.Profile

class FakeProfileRemoteDataSource : ProfileRemoteDataSource {
    var profileToReturn: Profile = Profile("u1", "Test", "avatar_1")
    var upsertShouldFail: Boolean = false
    var upsertCalledWith: Profile? = null

    override suspend fun fetchProfile(userId: String): Profile = profileToReturn

    override suspend fun upsertProfile(profile: Profile) {
        if (upsertShouldFail) error("upsert failed")
        upsertCalledWith = profile
    }
}