package com.example.moodtracker.profile

import com.domain.model.AvatarOption
import com.domain.usecase.LogoutUseCase
import com.example.moodtracker.auth.FakeAuthRepository
import com.presentation.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var profileRepository: FakeProfileRepository
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var logoutUseCase: LogoutUseCase
    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        profileRepository = FakeProfileRepository()
        authRepository = FakeAuthRepository()
        logoutUseCase = LogoutUseCase(authRepository)
        viewModel = ProfileViewModel(profileRepository, logoutUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al iniciar carga el perfil correctamente`() = runTest {
        advanceUntilIdle()

        assertEquals("Test", viewModel.uiState.name)
        assertEquals(AvatarOption.fromId("avatar_1"), viewModel.uiState.avatar)
        assertFalse(viewModel.uiState.isLoading)
        assertNull(viewModel.uiState.error)
    }
    @Test
    fun `loadProfile mantiene el ultimo estado bueno si falla`() = runTest {
        advanceUntilIdle()
        profileRepository.profileResult = Result.failure(RuntimeException("no connection"))

        viewModel.loadProfile()
        advanceUntilIdle()

        assertEquals("Test", viewModel.uiState.name)
    }


    @Test
    fun `updateName llama a updateProfile con el nombre nuevo y el avatar actual`() = runTest {
        advanceUntilIdle()

        viewModel.updateName("Nuevo Nombre")
        advanceUntilIdle()

        assertEquals("Nuevo Nombre" to "avatar_1", profileRepository.updateProfileCalledWith)
    }

    @Test
    fun `updateAvatar llama a updateProfile con el avatar nuevo y el nombre actual`() = runTest {
        advanceUntilIdle()

        val nuevoAvatar = AvatarOption.entries[1]
        viewModel.updateAvatar(nuevoAvatar)
        advanceUntilIdle()

        assertEquals("Test" to nuevoAvatar.id, profileRepository.updateProfileCalledWith)
    }


    @Test
    fun `updateAvatar no hace nada si el perfil todavia no cargo`() = runTest(testDispatcher) {
        val nuevoAvatar = AvatarOption.entries[1]
        viewModel.updateAvatar(nuevoAvatar)
        advanceUntilIdle()

        assertNull(profileRepository.updateProfileCalledWith)
    }


    @Test
    fun `logout invoca el callback cuando tiene exito`() = runTest {
        var callbackLlamado = false

        viewModel.logout { callbackLlamado = true }
        advanceUntilIdle()

        assertTrue(callbackLlamado)
        assertTrue(authRepository.signOutCalled)
    }

    @Test
    fun `logout setea error y no llama al callback si falla`() = runTest {
        authRepository.signOutShouldFail = true
        var callbackLlamado = false

        viewModel.logout { callbackLlamado = true }
        advanceUntilIdle()

        assertFalse(callbackLlamado)
        assertEquals("signout failed", viewModel.uiState.error)
        assertFalse(viewModel.uiState.isLoading)
    }
}