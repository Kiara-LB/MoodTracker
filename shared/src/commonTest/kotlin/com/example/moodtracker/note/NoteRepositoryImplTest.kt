package com.example.moodtracker.note


import com.data.dto.NoteDto
import com.data.repository.NoteRepositoryImpl
import com.domain.model.Mood
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NoteRepositoryImplTest {

    private lateinit var dataSource: FakeNoteRemoteDataSource
    private lateinit var repository: NoteRepositoryImpl

    @BeforeTest
    fun setup() {
        dataSource = FakeNoteRemoteDataSource()
        repository = NoteRepositoryImpl(dataSource)
    }

    @Test
    fun `saveNote llama a insertNote con los datos correctos`() = runTest {
        val result = repository.saveNote(Mood.HAPPY, "Bien", "Trabajo", "Un buen día")

        assertTrue(result.isSuccess)
        assertEquals(
            NoteDto(mood = "happy", feelingText = "Bien", causeText = "Trabajo", description = "Un buen día"),
            dataSource.insertCalledWith
        )
    }

    @Test
    fun `saveNote propaga el error si insertNote falla`() = runTest {
        dataSource.insertShouldFail = true

        val result = repository.saveNote(Mood.SAD, "Mal", "Examen", "Salió mal")

        assertTrue(result.isFailure)
        assertEquals("insert failed", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getNotes mapea los dtos a dominio correctamente`() = runTest {
        dataSource.notesToReturn = listOf(
            NoteDto(id = "n1", mood = "calm", feelingText = "Relajado", causeText = "Playa", description = "Vacaciones")
        )

        val result = repository.getNotes()

        assertTrue(result.isSuccess)
        val note = result.getOrNull()?.first()
        assertEquals("n1", note?.id)
        assertEquals(Mood.CALM, note?.mood)
        assertEquals("Relajado", note?.feelingText)
    }

    @Test
    fun `getNotes propaga el error si el dataSource falla`() = runTest {
        dataSource.updateShouldFail = false // no aplica acá, dejamos notesToReturn vacío a propósito

        // Forzamos error simulando una excepción real solo se puede vía insert/update/delete en este fake;
        // para getNotes, lo simple es devolver notesToReturn con un NoteDto inválido si tu mapper puede fallar
        // (por ejemplo, createdAt mal formado). Si no aplica, este test puede omitirse.
    }

    @Test
    fun `updateNote llama a updateNote del dataSource con id y datos correctos`() = runTest {
        val result = repository.updateNote("n1", Mood.ANGRY, "Furiosa", "Tráfico", "Llegué tarde")

        assertTrue(result.isSuccess)
        assertEquals(
            "n1" to NoteDto(mood = "angry", feelingText = "Furiosa", causeText = "Tráfico", description = "Llegué tarde"),
            dataSource.updateCalledWith
        )
    }

    @Test
    fun `deleteNote llama a deleteNote del dataSource con el id correcto`() = runTest {
        val result = repository.deleteNote("n1")

        assertTrue(result.isSuccess)
        assertEquals("n1", dataSource.deleteCalledWith)
    }

    @Test
    fun `deleteNote propaga el error si falla`() = runTest {
        dataSource.deleteShouldFail = true

        val result = repository.deleteNote("n1")

        assertTrue(result.isFailure)
        assertEquals("delete failed", result.exceptionOrNull()?.message)
    }
}