package com.example.moodtracker.auth

import com.domain.usecase.LoginUseCase
import com.presentation.auth.LoginViewModel
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
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authRepository: FakeAuthRepository
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = FakeAuthRepository()
        loginUseCase = LoginUseCase(authRepository)
        viewModel = LoginViewModel(loginUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login exitoso actualiza el estado a success`() = runTest(testDispatcher) {
        viewModel.login("test@test.com", "1234")
        advanceUntilIdle()

        assertTrue(viewModel.uiState.success)
        assertFalse(viewModel.uiState.isLoading)
        assertEquals("test@test.com" to "1234", authRepository.loginCalledWith)
    }

    @Test
    fun `login fallido setea el error`() = runTest(testDispatcher) {
        authRepository.loginResult = Result.failure(RuntimeException("credenciales invalidas"))

        viewModel.login("test@test.com", "wrong")
        advanceUntilIdle()

        assertFalse(viewModel.uiState.success)
        assertFalse(viewModel.uiState.isLoading)
        assertEquals("credenciales invalidas", viewModel.uiState.error)
    }
}