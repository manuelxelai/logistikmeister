package com.example.ui.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ExamAttempt
import com.example.data.model.FormulaMastery
import com.example.data.model.LessonProgress
import com.example.data.model.UserProfile
import com.example.data.model.WeaknessItem
import com.example.data.repository.LearningRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Ready(
        val profile: UserProfile,
        val lessonProgress: List<LessonProgress>,
        val formulaMastery: List<FormulaMastery>,
        val examAttempts: List<ExamAttempt>,
        val weaknesses: List<WeaknessItem>
    ) : DashboardUiState

    data class Error(val message: String) : DashboardUiState
}

class MainViewModel(
    private val repository: LearningRepository,
    private val currentUserId: String,
    private val initialDisplayName: String
) : ViewModel() {

    private val initError = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            repository.ensureUserProfile(initialDisplayName).onFailure { err ->
                Log.w(TAG, "Failed to ensure user profile", err)
                initError.value = err.localizedMessage ?: "Profil konnte nicht initialisiert werden"
            }
        }
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.observeUserProfile(currentUserId),
        repository.observeLessonProgress(currentUserId),
        repository.observeFormulaMastery(currentUserId),
        repository.observeExamAttempts(currentUserId),
        repository.observeWeaknesses(currentUserId)
    ) { profile, lessons, formulas, exams, weaknesses ->
        val resolvedProfile = profile ?: UserProfile(
            id = currentUserId,
            userId = currentUserId,
            displayName = initialDisplayName.ifBlank { LearningRepository.DEFAULT_DISPLAY_NAME }
        )
        DashboardUiState.Ready(
            profile = resolvedProfile,
            lessonProgress = lessons,
            formulaMastery = formulas,
            examAttempts = exams,
            weaknesses = weaknesses
        ) as DashboardUiState
    }.catch { error ->
        if (error is CancellationException) throw error
        Log.w(TAG, "Error observing learning dashboard", error)
        emit(DashboardUiState.Error(error.localizedMessage ?: "Daten konnten nicht geladen werden"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = DashboardUiState.Loading
    )

    fun updateDailyGoalMinutes(minutes: Long) {
        viewModelScope.launch {
            repository.updateDailyGoalMinutes(minutes).onFailure { err ->
                Log.w(TAG, "Failed to update daily goal minutes", err)
            }
        }
    }

    fun updateDisplayName(displayName: String) {
        viewModelScope.launch {
            repository.updateDisplayName(displayName).onFailure { err ->
                Log.w(TAG, "Failed to update display name", err)
            }
        }
    }

    private companion object {
        const val TAG = "MainViewModel"
        const val STOP_TIMEOUT_MILLIS = 5000L
    }
}
