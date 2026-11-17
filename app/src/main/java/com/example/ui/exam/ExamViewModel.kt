package com.example.ui.exam

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ExamEvaluationResult
import com.example.data.ai.GeneratedExamScenario
import com.example.data.ai.LogistikAiRepository
import com.example.data.model.ExamAttempt
import com.example.data.repository.LearningRepository
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

sealed interface WrittenExamState {
    data object Idle : WrittenExamState
    data object Generating : WrittenExamState
    data class Active(
        val scenario: GeneratedExamScenario,
        val answers: Map<Int, String>,
        val secondsRemaining: Int,
        val totalSeconds: Int
    ) : WrittenExamState

    data object Evaluating : WrittenExamState
    data class Completed(
        val scenario: GeneratedExamScenario,
        val result: ExamEvaluationResult,
        val xpAwarded: Long
    ) : WrittenExamState

    data class Error(val message: String) : WrittenExamState
}

data class CommitteeMessage(
    val isCandidate: Boolean,
    val text: String
)

sealed interface OralExamState {
    data object Idle : OralExamState
    data class Active(
        val topic: String,
        val messages: List<CommitteeMessage>,
        val streamingReply: String?,
        val isSending: Boolean,
        val prepSecondsRemaining: Int,
        val isPrepTimerRunning: Boolean
    ) : OralExamState

    data object Evaluating : OralExamState
    data class Completed(
        val topic: String,
        val result: ExamEvaluationResult,
        val xpAwarded: Long
    ) : OralExamState

    data class Error(val message: String) : OralExamState
}

class ExamViewModel(
    private val learningRepository: LearningRepository,
    private val aiRepository: LogistikAiRepository = LogistikAiRepository()
) : ViewModel() {

    private val _writtenState = MutableStateFlow<WrittenExamState>(WrittenExamState.Idle)
    val writtenState: StateFlow<WrittenExamState> = _writtenState.asStateFlow()

    private val _oralState = MutableStateFlow<OralExamState>(OralExamState.Idle)
    val oralState: StateFlow<OralExamState> = _oralState.asStateFlow()

    private var writtenTimerJob: Job? = null
    private var prepTimerJob: Job? = null

    fun startWrittenExam(examMode: String, durationMinutes: Int) {
        writtenTimerJob?.cancel()
        viewModelScope.launch {
            _writtenState.value = WrittenExamState.Generating
            try {
                val scenario = aiRepository.generateSituationsaufgabe(examMode)
                val totalSec = durationMinutes * 60
                _writtenState.value = WrittenExamState.Active(
                    scenario = scenario,
                    answers = scenario.subQuestions.associate { it.number to "" },
                    secondsRemaining = totalSec,
                    totalSeconds = totalSec
                )
                startWrittenCountdown()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to generate written exam", e)
                _writtenState.value = WrittenExamState.Error(
                    e.localizedMessage ?: "Situationsaufgabe konnte nicht generiert werden."
                )
            }
        }
    }

    fun updateWrittenAnswer(questionNumber: Int, text: String) {
        val current = _writtenState.value as? WrittenExamState.Active ?: return
        _writtenState.value = current.copy(
            answers = current.answers + (questionNumber to text)
        )
    }

    fun submitWrittenExam() {
        val current = _writtenState.value as? WrittenExamState.Active ?: return
        writtenTimerJob?.cancel()
        val elapsedMinutes = ((current.totalSeconds - current.secondsRemaining) / 60).coerceAtLeast(1)

        viewModelScope.launch {
            _writtenState.value = WrittenExamState.Evaluating
            try {
                val eval = aiRepository.evaluateExamAnswers(current.scenario, current.answers)
                val xp = if (eval.passed) 120L else 40L
                learningRepository.saveExamAttempt(
                    ExamAttempt(
                        examMode = current.scenario.examMode,
                        title = current.scenario.title,
                        scenarioSummary = current.scenario.ausgangssituation,
                        achievedPoints = eval.totalPoints.toLong(),
                        maxPoints = 100L,
                        ihkGrade = eval.ihkGrade.toLong(),
                        passed = eval.passed,
                        durationMinutes = elapsedMinutes.toLong(),
                        feedbackSummary = eval.summary
                    )
                )
                learningRepository.awardXpAndUpdateStreak(xpDelta = xp, studyMinutesDelta = elapsedMinutes.toLong())
                _writtenState.value = WrittenExamState.Completed(
                    scenario = current.scenario,
                    result = eval,
                    xpAwarded = xp
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to evaluate written exam", e)
                _writtenState.value = WrittenExamState.Error(
                    e.localizedMessage ?: "Auswertung fehlgeschlagen."
                )
            }
        }
    }

    fun resetWrittenExam() {
        writtenTimerJob?.cancel()
        _writtenState.value = WrittenExamState.Idle
    }

    fun startOralExam(topic: String) {
        prepTimerJob?.cancel()
        _oralState.value = OralExamState.Active(
            topic = topic,
            messages = listOf(
                CommitteeMessage(
                    isCandidate = false,
                    text = "Guten Tag! Der SIHK-Prüfungsausschuss begrüßt Sie zum Fachgespräch \"$topic\". " +
                        "Nutzen Sie bei Bedarf den 30-Minuten-Vorbereitungs-Timer oder starten Sie direkt mit Ihrer Gliederung."
                )
            ),
            streamingReply = null,
            isSending = false,
            prepSecondsRemaining = 30 * 60,
            isPrepTimerRunning = false
        )
    }

    fun toggleOralPrepTimer() {
        val active = _oralState.value as? OralExamState.Active ?: return
        if (active.isPrepTimerRunning) {
            prepTimerJob?.cancel()
            _oralState.value = active.copy(isPrepTimerRunning = false)
        } else {
            _oralState.value = active.copy(isPrepTimerRunning = true)
            prepTimerJob = viewModelScope.launch {
                while (true) {
                    delay(1000L)
                    val cur = _oralState.value as? OralExamState.Active ?: break
                    if (!cur.isPrepTimerRunning || cur.prepSecondsRemaining <= 0) break
                    _oralState.value = cur.copy(prepSecondsRemaining = cur.prepSecondsRemaining - 1)
                }
            }
        }
    }

    fun sendOralMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank()) return
        val active = _oralState.value as? OralExamState.Active ?: return
        if (active.isSending) return

        val history: List<Content> = active.messages.drop(1).map { msg ->
            content(if (msg.isCandidate) "user" else "model") { text(msg.text) }
        }
        val updatedMessages = active.messages + CommitteeMessage(isCandidate = true, text = trimmed)
        _oralState.value = active.copy(
            messages = updatedMessages,
            streamingReply = "",
            isSending = true
        )

        viewModelScope.launch {
            val sb = StringBuilder()
            try {
                withTimeout(60_000L) {
                    aiRepository.streamCommitteeReply(
                        topicContext = active.topic,
                        chatHistory = history,
                        userInput = trimmed
                    ).collect { chunk ->
                        sb.append(chunk)
                        val cur = _oralState.value as? OralExamState.Active
                        if (cur != null) {
                            _oralState.value = cur.copy(streamingReply = sb.toString())
                        }
                    }
                }
                val finalReply = sb.toString().ifBlank { "Bitte führen Sie diesen Punkt aus Sicht des Logistikmeisters noch etwas genauer aus." }
                val cur = _oralState.value as? OralExamState.Active
                if (cur != null) {
                    _oralState.value = cur.copy(
                        messages = cur.messages + CommitteeMessage(isCandidate = false, text = finalReply),
                        streamingReply = null,
                        isSending = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Committee chat stream failed", e)
                _oralState.value = OralExamState.Error(
                    e.localizedMessage ?: "Fehler in der Kommunikation mit dem KI-Prüfungsausschuss."
                )
            }
        }
    }

    fun finishAndGradeOralExam() {
        val active = _oralState.value as? OralExamState.Active ?: return
        prepTimerJob?.cancel()
        val transcript = active.messages.joinToString("\n") { msg ->
            val role = if (msg.isCandidate) "Prüfling" else "SIHK-Ausschuss"
            "$role: ${msg.text}"
        }

        viewModelScope.launch {
            _oralState.value = OralExamState.Evaluating
            try {
                val eval = aiRepository.evaluateOralExam(active.topic, transcript)
                val xp = if (eval.passed) 120L else 40L
                learningRepository.saveExamAttempt(
                    ExamAttempt(
                        examMode = "FACHGESPRAECH",
                        title = "Fachgespräch: ${active.topic}",
                        scenarioSummary = "Mündliche SIHK-Prüfungssimulation (${active.messages.size} Gesprächsbeiträge)",
                        achievedPoints = eval.totalPoints.toLong(),
                        maxPoints = 100L,
                        ihkGrade = eval.ihkGrade.toLong(),
                        passed = eval.passed,
                        durationMinutes = 20L,
                        feedbackSummary = eval.summary
                    )
                )
                learningRepository.awardXpAndUpdateStreak(xpDelta = xp, studyMinutesDelta = 15L)
                _oralState.value = OralExamState.Completed(
                    topic = active.topic,
                    result = eval,
                    xpAwarded = xp
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to evaluate oral exam", e)
                _oralState.value = OralExamState.Error(
                    e.localizedMessage ?: "Bewertung des Fachgesprächs fehlgeschlagen."
                )
            }
        }
    }

    fun resetOralExam() {
        prepTimerJob?.cancel()
        _oralState.value = OralExamState.Idle
    }

    private fun startWrittenCountdown() {
        writtenTimerJob?.cancel()
        writtenTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val cur = _writtenState.value as? WrittenExamState.Active ?: break
                if (cur.secondsRemaining <= 1) {
                    submitWrittenExam()
                    break
                }
                _writtenState.value = cur.copy(secondsRemaining = cur.secondsRemaining - 1)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        writtenTimerJob?.cancel()
        prepTimerJob?.cancel()
    }

    private companion object {
        const val TAG = "ExamViewModel"
    }
}
