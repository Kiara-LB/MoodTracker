package com.presentation.home


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domain.model.DayMood
import com.domain.model.Mood
import com.domain.model.MoodPercentage
import com.domain.model.Note
import com.domain.repository.AuthRepository
import com.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import kotlinx.datetime.toLocalDateTime

class HomeViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            val userName = authRepository.currentUserName() ?: "Usuario"

            getNotesUseCase()
                .onSuccess { notes ->
                    uiState = uiState.copy(
                        isLoading = false,
                        userName = userName,
                        weeklyMoods = computeWeeklyMoods(notes),
                        monthlyPercentages = computeMonthlyPercentages(notes)
                    )
                }
                .onFailure {
                    uiState = uiState.copy(isLoading = false, userName = userName)
                }
        }
    }

    private fun computeWeeklyMoods(notes: List<Note>): List<DayMood> {
        val timeZone = TimeZone.currentSystemDefault()
        val today = kotlin.time.Clock.System.todayIn(timeZone)

        // últimos 5 días, del más viejo al más nuevo
        val days = (4 downTo 0).map { offset -> today.minus(offset, kotlinx.datetime.DateTimeUnit.DAY) }

        return days.map { date ->
            val notesOfDay = notes.filter {
                it.createdAt.toLocalDateTime(timeZone).date == date
            }
            val lastMoodOfDay = notesOfDay.maxByOrNull { it.createdAt }?.mood
            DayMood(dayLabel = date.dayOfWeek.toSpanishAbbreviation(), mood = lastMoodOfDay)
        }
    }

    private fun computeMonthlyPercentages(notes: List<Note>): List<MoodPercentage> {
        val timeZone = TimeZone.currentSystemDefault()
        val today = kotlin.time.Clock.System.todayIn(timeZone)

        val notesThisMonth = notes.filter {
            val date = it.createdAt.toLocalDateTime(timeZone).date
            date.year == today.year && date.monthNumber == today.monthNumber
        }

        if (notesThisMonth.isEmpty()) return emptyList()

        val total = notesThisMonth.size
        return Mood.entries.mapNotNull { mood ->
            val count = notesThisMonth.count { it.mood == mood }
            if (count == 0) null
            else MoodPercentage(mood = mood, percentage = (count.toFloat() / total) * 100f)
        }
    }
}

private fun DayOfWeek.toSpanishAbbreviation(): String = when (this) {
    DayOfWeek.MONDAY -> "lun"
    DayOfWeek.TUESDAY -> "mar"
    DayOfWeek.WEDNESDAY -> "mié"
    DayOfWeek.THURSDAY -> "jue"
    DayOfWeek.FRIDAY -> "vie"
    DayOfWeek.SATURDAY -> "sáb"
    DayOfWeek.SUNDAY -> "dom"
    else -> ""
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val weeklyMoods: List<DayMood> = emptyList(),
    val monthlyPercentages: List<MoodPercentage> = emptyList()
)