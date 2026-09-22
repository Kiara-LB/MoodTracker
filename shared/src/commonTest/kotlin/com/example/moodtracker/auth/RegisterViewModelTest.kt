package com.example.moodtracker.auth

import com.domain.model.PredefinedAvatar
import com.domain.usecase.RegisterUseCase
import com.example.moodtracker.profile.FakeProfileRepository
import com.presentation.auth.RegisterViewModel
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
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authRepository: FakeAuthRepository
    private lateinit var profileRepository: FakeProfileRepository
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var viewModel: RegisterViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = FakeAuthRepository()
        profileRepository = FakeProfileRepository()
        registerUseCase = RegisterUseCase(authRepository, profileRepository)
        viewModel = RegisterViewModel(registerUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `register exitoso crea el perfil y actualiza el estado`() = runTest(testDispatcher) {
        viewModel.register("Test", "test@test.com", "1234")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.success)
        assertFalse(viewModel.uiState.isLoading)
        assertEquals("Test" to PredefinedAvatar.AVATAR_1.id, profileRepository.updateProfileCalledWith)
    }

    @Test
    fun `register falla si el registro en auth falla`() = runTest(testDispatcher) {
        authRepository.registerResult = Result.failure(RuntimeException("email ya registrado"))

        viewModel.register("Kiara", "kiara@test.com", "1234")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.success)
        assertEquals("email ya registrado", viewModel.uiState.error)
    }

    @Test
    fun `register falla si la creacion del perfil falla`() = runTest(testDispatcher) {
        profileRepository.updateResult = Result.failure(RuntimeException("no se pudo crear el perfil"))

        viewModel.register("Kiara", "kiara@test.com", "1234")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.success)
        assertEquals("no se pudo crear el perfil", viewModel.uiState.error)
    }
}