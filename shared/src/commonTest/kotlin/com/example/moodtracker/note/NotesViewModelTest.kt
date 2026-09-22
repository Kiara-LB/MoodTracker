package com.example.moodtracker.note
import com.domain.model.Mood
import com.domain.model.Note
import com.domain.usecase.DeleteNoteUseCase
import com.domain.usecase.GetNotesUseCase
import com.domain.usecase.SaveNoteUseCase
import com.domain.usecase.UpdateNoteUseCase
import com.example.moodtracker.auth.FakeAuthRepository
import com.presentation.notes.NotesViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import kotlin.time.Instant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.JsonObject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: FakeNoteRepository
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var viewModel: NotesViewModel

    private val sampleNote = Note(
        id = "n1",
        mood = Mood.HAPPY,
        feelingText = "Bien",
        causeText = "Trabajo",
        description = "Un buen día",
        createdAt = Instant.DISTANT_PAST
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeNoteRepository()
        authRepository = FakeAuthRepository()
        viewModel = NotesViewModel(
            getNotesUseCase = GetNotesUseCase(repository),
            updateNoteUseCase = UpdateNoteUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            saveNoteUseCase = SaveNoteUseCase(repository),
            authRepository = authRepository
        )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun fakeUserInfo(id: String = "u1") = UserInfo(
        id = id,
        aud = "authenticated",
        appMetadata = JsonObject(emptyMap()),
        userMetadata = JsonObject(emptyMap()),
        createdAt = Instant.DISTANT_PAST,
        updatedAt = Instant.DISTANT_PAST
    )
    @Test
    fun `carga las notas cuando la sesion se autentica`() = runTest(testDispatcher) {
        repository.notesResult = Result.success(listOf(sampleNote))

        authRepository.sessionStatus.value = SessionStatus.Authenticated(
            UserSession(
                accessToken = "fake-access-token",
                refreshToken = "fake-refresh-token",
                expiresIn = 3600,
                tokenType = "bearer",
                user = fakeUserInfo()
            )
        )
        advanceUntilIdle()

        assertEquals(listOf(sampleNote), viewModel.uiState.notes)
        assertTrue(viewModel.uiState.hasLoadedOnce)
        assertFalse(viewModel.uiState.isLoading)
    }

    @Test
    fun `saveNote llama al usecase y recarga las notas`() = runTest(testDispatcher) {
        viewModel.saveNote(Mood.CALM, "Relajada", "Playa", "Vacaciones")
        advanceUntilIdle()

        assertEquals(listOf(Mood.CALM.id, "Relajada", "Playa", "Vacaciones"), repository.saveCalledWith)
        assertFalse(viewModel.uiState.isSaving)
    }

    @Test
    fun `saveNote setea error si falla`() = runTest(testDispatcher) {
        repository.saveResult = Result.failure(RuntimeException("no se pudo guardar"))

        viewModel.saveNote(Mood.SAD, "Mal", "Examen", "Salió mal")
        advanceUntilIdle()

        assertEquals("no se pudo guardar", viewModel.uiState.error)
        assertFalse(viewModel.uiState.isSaving)
    }

    @Test
    fun `updateNote llama al usecase con los datos correctos`() = runTest(testDispatcher) {
        viewModel.updateNote("n1", Mood.ANGRY, "Furiosa", "Tráfico", "Llegué tarde")
        advanceUntilIdle()

        assertEquals("n1" to listOf(Mood.ANGRY.id, "Furiosa", "Tráfico", "Llegué tarde"), repository.updateCalledWith)
    }

    @Test
    fun `deleteNote llama al usecase con el id correcto`() = runTest(testDispatcher) {
        viewModel.deleteNote("n1")
        advanceUntilIdle()

        assertEquals("n1", repository.deleteCalledWith)
    }

    @Test
    fun `findNoteById devuelve la nota si existe`() = runTest(testDispatcher) {
        repository.notesResult = Result.success(listOf(sampleNote))
        viewModel.loadNotes()
        advanceUntilIdle()

        assertEquals(sampleNote, viewModel.findNoteById("n1"))
    }

    @Test
    fun `findNoteById devuelve null si no existe`() = runTest(testDispatcher) {
        assertEquals(null, viewModel.findNoteById("no-existe"))
    }
}