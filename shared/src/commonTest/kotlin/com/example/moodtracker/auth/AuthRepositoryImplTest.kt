package com.example.moodtracker.auth

import com.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AuthRepositoryImplTest {

    private lateinit var dataSource: FakeAuthRemoteDataSource
    private lateinit var repository: AuthRepositoryImpl

    @BeforeTest
    fun setup() {
        dataSource = FakeAuthRemoteDataSource()
        repository = AuthRepositoryImpl(dataSource)
    }

    @Test
    fun `register llama a signUp con los datos correctos`() = runTest {
        val result = repository.register("Test", "test@test.com", "1234")

        assertTrue(result.isSuccess)
        assertEquals(Triple("Test", "test@test.com", "1234"), dataSource.signUpCalledWith)
    }

    @Test
    fun `register propaga el error si signUp falla`() = runTest {
        dataSource.signUpShouldFail = true

        val result = repository.register("Test", "test@test.com", "1234")

        assertTrue(result.isFailure)
        assertEquals("signup failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login llama a signIn con los datos correctos`() = runTest {
        val result = repository.login("test@test.com", "1234")

        assertTrue(result.isSuccess)
        assertEquals("test@test.com" to "1234", dataSource.signInCalledWith)
    }

    @Test
    fun `login propaga el error si signIn falla`() = runTest {
        dataSource.signInShouldFail = true

        val result = repository.login("test@test.com", "1234")

        assertTrue(result.isFailure)
        assertEquals("signin failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `signOut delega en el dataSource y devuelve exito`() = runTest {
        val result = repository.signOut()

        assertTrue(result.isSuccess)
        assertTrue(dataSource.signOutCalled)
    }

    @Test
    fun `signOut propaga el error si falla`() = runTest {
        dataSource.signOutShouldFail = true

        val result = repository.signOut()

        assertTrue(result.isFailure)
        assertEquals("signout failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `currentUserId delega en el dataSource`() {
        dataSource.userId = "u42"

        assertEquals("u42", repository.currentUserId())
    }
}