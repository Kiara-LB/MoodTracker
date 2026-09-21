package com.example.moodtracker.profile

import com.data.repository.ProfileRepositoryImpl
import com.domain.model.Profile
import com.example.moodtracker.auth.FakeAuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue


class ProfileRepositoryImplTest {

    private lateinit var dataSource: FakeProfileRemoteDataSource
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var repository: ProfileRepositoryImpl

    @BeforeTest
    fun setup() {
        dataSource = FakeProfileRemoteDataSource()
        authRepository = FakeAuthRepository()
        repository = ProfileRepositoryImpl(dataSource, authRepository)
    }

    @Test
    fun `getProfile devuelve el perfil cuando hay sesion activa`() = runTest {
        val result = repository.getProfile()

        assertTrue(result.isSuccess)
        assertEquals(dataSource.profileToReturn, result.getOrNull())
    }

    @Test
    fun `getProfile falla cuando no hay sesion activa`() = runTest {
        authRepository.userId = null

        val result = repository.getProfile()

        assertTrue(result.isFailure)
        assertEquals("No hay sesión activa", result.exceptionOrNull()?.message)
    }

    @Test
    fun `updateProfile llama a upsertProfile con los datos correctos`() = runTest {
        val result = repository.updateProfile(name = "Nuevo Nombre", avatarId = "avatar_2")

        assertTrue(result.isSuccess)
        assertEquals(Profile("u1", "Nuevo Nombre", "avatar_2"), dataSource.upsertCalledWith)
    }

    @Test
    fun `updateProfile falla cuando no hay sesion activa`() = runTest {
        authRepository.userId = null

        val result = repository.updateProfile(name = "X", avatarId = "avatar_1")

        assertTrue(result.isFailure)
        assertNull(dataSource.upsertCalledWith)
    }
}