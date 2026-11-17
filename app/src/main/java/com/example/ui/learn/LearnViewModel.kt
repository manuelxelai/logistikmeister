package com.example.ui.learn

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.AdaptiveLearningStore
import com.example.data.curriculum.DrillMode
import com.example.data.curriculum.LessonCatalog
import com.example.data.model.CategoryBreakdown
import com.example.data.model.CurriculumTopic
import com.example.data.model.ExamAttempt
import com.example.data.model.ExamQuestion
import com.example.data.model.LessonProgress
import com.example.data.model.QuestionCategory
import com.example.data.model.SihkCurriculum
import com.example.data.model.WeaknessItem
import com.example.data.repository.LearningRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SessionKind(val badge: String) {
    TOPIC_BLOCK("Handlungsfeld im Block"),
    PRACTICE_UNTIMED("Übungsmodus · Ohne Zeitdruck"),
    CATEGORY_BLOCK("Kategorie-Training"),
    ALL_BLOCK("Alle Fragen im Block"),
    QUICK_TEST("Schnelltest"),
    FALSCH_GEDACHT("Falsch gedacht"),
    ADAPTIVE_PLAN("Adaptiver Lernplan (Leitner)"),
    DAILY_SPRINT("Kennzahlen-Sprint"),
    FLASHCARDS("Karteikarten-Modus"),
    BOOKMARKS("Merkliste"),
    MC_EXAM("BQ/HQ-Prüfungsmodus")
}

data class ModuleBreakdown(
    val moduleCode: String,
    val correct: Int,
    val total: Int
)

data class LessonSessionState(
    val isActive: Boolean = false,
    val sessionKind: SessionKind = SessionKind.TOPIC_BLOCK,
    val isSmartReview: Boolean = false,
    val topicId: String = "",
    val moduleCode: String = "BQ",
    val title: String = "",
    val questions: List<ExamQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val mcAnswers: Map<String, Int> = emptyMap(),
    val isSubmitted: Boolean = false,
    val isFlashcardFlipped: Boolean = false,
    val correctCount: Int = 0,
    val errorCount: Int = 0,
    val sprintSecondsLeft: Int? = null,
    val examSecondsLeft: Int? = null,
    val totalExamSeconds: Int = 0,
    val isFinished: Boolean = false,
    val earnedXp: Long = 0L,
    val ihkPoints: Int? = null,
    val ihkGrade: Int? = null,
    val moduleBreakdowns: List<ModuleBreakdown> = emptyList(),
    val categoryBreakdowns: List<CategoryBreakdown> = emptyList(),
    val wrongReviewItems: List<ExamQuestion> = emptyList(),
    val statusMessage: String? = null
) {
    val currentQuestion: ExamQuestion?
        get() = questions.getOrNull(currentIndex)
}

class LearnViewModel(
    private val repository: LearningRepository,
    private val adaptiveLearningStore: AdaptiveLearningStore? = null
) : ViewModel() {

    private val _session = MutableStateFlow(LessonSessionState())
    val session: StateFlow<LessonSessionState> = _session.asStateFlow()

    private var timerJob: Job? = null
    private var activeWeaknessLookup: Map<String, WeaknessItem> = emptyMap()

    fun startAdaptivePlan(
        count: Int = 15,
        moduleFilter: String? = null,
        asFlashcards: Boolean = false
    ) {
        val qs = adaptiveLearningStore?.buildAdaptivePlanQuestions(
            allQuestions = LessonCatalog.questions,
            moduleFilter = moduleFilter,
            count = count
        ) ?: LessonCatalog.buildQuickTestQuestions(count = count, moduleCodeFilter = moduleFilter)
        if (qs.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = if (asFlashcards) SessionKind.FLASHCARDS else SessionKind.ADAPTIVE_PLAN,
            isSmartReview = true,
            topicId = "adaptive_plan",
            moduleCode = moduleFilter ?: "ALLE",
            title = if (asFlashcards) {
                "Lernplan · Karteikarten (${qs.size})"
            } else {
                "Intelligenter Lernplan (${qs.size} Fragen)"
            },
            questions = qs,
            currentIndex = 0
        )
    }

    fun startTopicSession(topic: CurriculumTopic, untimed: Boolean = true) {
        val qs = LessonCatalog.questionsForTopic(topic.id)
        if (qs.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = if (untimed) SessionKind.PRACTICE_UNTIMED else SessionKind.TOPIC_BLOCK,
            isSmartReview = false,
            topicId = topic.id,
            moduleCode = topic.moduleCode,
            title = if (untimed) "Übungsmodus · ${topic.numberLabel}" else "${topic.numberLabel} · ${topic.title}",
            questions = qs,
            currentIndex = 0
        )
        if (!untimed) {
            startTimerIfSprint(qs.first())
        }
    }

    fun startCategorySession(category: QuestionCategory) {
        val qs = LessonCatalog.questionsForCategory(category)
        if (qs.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = SessionKind.CATEGORY_BLOCK,
            isSmartReview = false,
            topicId = category.topicIds.firstOrNull() ?: category.id,
            moduleCode = category.section.code,
            title = "${category.badgeCode} · ${category.displayName}",
            questions = qs,
            currentIndex = 0
        )
        startTimerIfSprint(qs.first())
    }

    fun startFlashcardsSession(
        title: String = "Karteikarten · Logistikmeister",
        moduleFilter: String? = null,
        categoryFilter: QuestionCategory? = null,
        count: Int = 15
    ) {
        val pool = when {
            categoryFilter != null -> LessonCatalog.questionsForCategory(categoryFilter)
            moduleFilter.isNullOrBlank() || moduleFilter == "ALLE" -> LessonCatalog.questions
            else -> LessonCatalog.questions.filter { it.moduleCode == moduleFilter }
        }.shuffled().take(count.coerceAtLeast(1))
        if (pool.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = SessionKind.FLASHCARDS,
            isSmartReview = false,
            topicId = "flashcards",
            moduleCode = moduleFilter ?: "ALLE",
            title = title,
            questions = pool,
            currentIndex = 0
        )
    }

    fun startBookmarkedSession(
        bookmarkedIds: Set<String>,
        asFlashcards: Boolean = false
    ) {
        val qs = LessonCatalog.questions.filter { it.id in bookmarkedIds }
        if (qs.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = if (asFlashcards) SessionKind.FLASHCARDS else SessionKind.BOOKMARKS,
            isSmartReview = false,
            topicId = "bookmarks",
            moduleCode = "ALLE",
            title = "Merkliste (${qs.size} Fragen)",
            questions = qs,
            currentIndex = 0
        )
    }

    fun flipFlashcard() {
        val state = _session.value
        if (state.sessionKind != SessionKind.FLASHCARDS || state.isFinished) return
        _session.update { it.copy(isFlashcardFlipped = !it.isFlashcardFlipped) }
    }

    fun rateFlashcard(known: Boolean) {
        val state = _session.value
        val question = state.currentQuestion ?: return
        if (state.sessionKind != SessionKind.FLASHCARDS || state.isFinished) return

        val nextCorrect = state.correctCount + if (known) 1 else 0
        val nextErrors = state.errorCount + if (known) 0 else 1
        val nextWrong = if (known) state.wrongReviewItems else state.wrongReviewItems + question

        adaptiveLearningStore?.recordResult(question.id, known)

        if (!known) {
            viewModelScope.launch {
                try {
                    repository.recordWeakness(
                        topicId = question.topicId,
                        questionKey = question.id,
                        promptPreview = question.prompt
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to record unsure flashcard", e)
                }
            }
        }

        val nextIdx = state.currentIndex + 1
        if (nextIdx >= state.questions.size) {
            _session.update {
                it.copy(
                    correctCount = nextCorrect,
                    errorCount = nextErrors,
                    wrongReviewItems = nextWrong
                )
            }
            finishSession()
        } else {
            _session.update {
                it.copy(
                    currentIndex = nextIdx,
                    isFlashcardFlipped = false,
                    correctCount = nextCorrect,
                    errorCount = nextErrors,
                    wrongReviewItems = nextWrong
                )
            }
        }
    }

    fun startAllQuestionsBlock() {
        val qs = LessonCatalog.questions
        if (qs.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = SessionKind.ALL_BLOCK,
            isSmartReview = false,
            topicId = "all_block",
            moduleCode = "ALLE",
            title = "Alle Fragen im Block (${qs.size} Fragen)",
            questions = qs,
            currentIndex = 0
        )
    }

    fun startQuickTest(
        count: Int = 10,
        moduleFilter: String? = null,
        categoryFilter: QuestionCategory? = null
    ) {
        val qs = LessonCatalog.buildQuickTestQuestions(
            count = count,
            moduleCodeFilter = moduleFilter,
            categoryFilter = categoryFilter
        )
        if (qs.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        val label = when {
            categoryFilter != null -> "Schnelltest ${categoryFilter.shortLabel} · ${qs.size} Fragen"
            moduleFilter.isNullOrBlank() || moduleFilter == "ALLE" -> "Schnelltest · ${qs.size} Zufallsfragen"
            else -> "Schnelltest $moduleFilter · ${qs.size} Fragen"
        }
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = SessionKind.QUICK_TEST,
            isSmartReview = false,
            topicId = "quick_test",
            moduleCode = categoryFilter?.section?.code ?: (moduleFilter ?: "ALLE"),
            title = label,
            questions = qs,
            currentIndex = 0
        )
    }

    fun startSmartReview(weaknesses: List<WeaknessItem>) {
        val openWeaknesses = weaknesses.filter { !it.resolved }
        val mapped = openWeaknesses.mapNotNull { w ->
            LessonCatalog.findByKey(w.questionKey)?.let { q -> w to q }
        }
        val questions = if (mapped.isNotEmpty()) {
            activeWeaknessLookup = mapped.associate { (w, q) -> q.id to w }
            mapped.map { it.second }
        } else {
            activeWeaknessLookup = emptyMap()
            LessonCatalog.buildQuickTestQuestions(count = 5)
        }
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = SessionKind.FALSCH_GEDACHT,
            isSmartReview = true,
            topicId = "falsch_gedacht",
            moduleCode = "BQ",
            title = if (mapped.isNotEmpty()) {
                "„Falsch gedacht“ · ${questions.size} Fehler"
            } else {
                "„Falsch gedacht“ · Trainings-Mix"
            },
            questions = questions,
            currentIndex = 0
        )
    }

    fun startDailySprint() {
        val sprintQuestions = LessonCatalog.questions
            .filter { it.mode == DrillMode.KENNZAHLEN_SPRINT }
            .shuffled()
            .take(5)
        activeWeaknessLookup = emptyMap()
        timerJob?.cancel()
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = SessionKind.DAILY_SPRINT,
            isSmartReview = false,
            topicId = "daily_sprint",
            moduleCode = "HQ",
            title = "Kognitiver Kennzahlen-Sprint",
            questions = sprintQuestions,
            currentIndex = 0
        )
        sprintQuestions.firstOrNull()?.let { startTimerIfSprint(it) }
    }

    fun startMcExamSimulation(
        questionCount: Int = 25,
        durationMinutes: Int = 30,
        moduleFilter: String? = null,
        topicIdFilter: String? = null,
        customTitle: String? = null
    ) {
        val qs = LessonCatalog.buildMcExamQuestions(
            count = questionCount,
            moduleCodeFilter = moduleFilter,
            topicIdFilter = topicIdFilter
        )
        if (qs.isEmpty()) return
        activeWeaknessLookup = emptyMap()
        val totalSec = durationMinutes * 60
        val labelScope = customTitle ?: when (moduleFilter) {
            "BQ" -> "BQ-Klausurcheck"
            "HQ" -> "HQ-Prüfungscheck"
            else -> "BQ/HQ-Prüfungssimulation"
        }
        _session.value = LessonSessionState(
            isActive = true,
            sessionKind = SessionKind.MC_EXAM,
            isSmartReview = false,
            topicId = topicIdFilter ?: "mc_exam",
            moduleCode = moduleFilter ?: qs.first().moduleCode,
            title = "$labelScope (${qs.size} Fragen)",
            questions = qs,
            currentIndex = 0,
            examSecondsLeft = totalSec,
            totalExamSeconds = totalSec
        )
        startExamCountdown()
    }

    fun selectOption(index: Int) {
        val state = _session.value
        val q = state.currentQuestion ?: return
        if (state.sessionKind == SessionKind.MC_EXAM) {
            if (state.isFinished) return
            _session.update {
                it.copy(
                    selectedOptionIndex = index,
                    mcAnswers = it.mcAnswers + (q.id to index)
                )
            }
            return
        }
        if (state.isSubmitted) return
        _session.update { it.copy(selectedOptionIndex = index) }
    }

    fun jumpToQuestion(index: Int) {
        val state = _session.value
        if (state.sessionKind != SessionKind.MC_EXAM || state.isFinished) return
        val target = state.questions.getOrNull(index) ?: return
        _session.update {
            it.copy(
                currentIndex = index,
                selectedOptionIndex = it.mcAnswers[target.id]
            )
        }
    }

    fun previousQuestion() {
        val state = _session.value
        if (state.sessionKind != SessionKind.MC_EXAM || state.isFinished) return
        val prevIdx = (state.currentIndex - 1).coerceAtLeast(0)
        jumpToQuestion(prevIdx)
    }

    fun submitAnswer() {
        val state = _session.value
        if (state.sessionKind == SessionKind.MC_EXAM) {
            nextQuestion()
            return
        }
        val question = state.currentQuestion ?: return
        val selected = state.selectedOptionIndex ?: return
        if (state.isSubmitted) return

        timerJob?.cancel()
        val isCorrect = selected == question.correctIndex
        adaptiveLearningStore?.recordResult(question.id, isCorrect)

        _session.update {
            it.copy(
                isSubmitted = true,
                correctCount = it.correctCount + if (isCorrect) 1 else 0,
                errorCount = it.errorCount + if (isCorrect) 0 else 1,
                wrongReviewItems = if (isCorrect) it.wrongReviewItems else it.wrongReviewItems + question
            )
        }

        viewModelScope.launch {
            try {
                if (isCorrect) {
                    activeWeaknessLookup[question.id]?.let { weakness ->
                        repository.resolveWeakness(weakness.id)
                    }
                } else {
                    repository.recordWeakness(
                        topicId = question.topicId,
                        questionKey = question.id,
                        promptPreview = question.prompt
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.w(TAG, "Failed to update Falsch-gedacht weakness", e)
            }
        }
    }

    fun nextQuestion() {
        val state = _session.value
        val nextIdx = state.currentIndex + 1
        if (nextIdx >= state.questions.size) {
            if (state.sessionKind == SessionKind.MC_EXAM) {
                finishMcExam()
            } else {
                finishSession()
            }
            return
        }
        val nextQ = state.questions[nextIdx]
        _session.update {
            it.copy(
                currentIndex = nextIdx,
                selectedOptionIndex = if (it.sessionKind == SessionKind.MC_EXAM) it.mcAnswers[nextQ.id] else null,
                isSubmitted = false
            )
        }
        if (state.sessionKind == SessionKind.DAILY_SPRINT || state.sessionKind == SessionKind.TOPIC_BLOCK) {
            startTimerIfSprint(nextQ)
        }
    }

    fun finishMcExam() {
        timerJob?.cancel()
        val state = _session.value
        if (state.isFinished) return

        val total = state.questions.size.coerceAtLeast(1)
        var correct = 0
        val wrongList = mutableListOf<ExamQuestion>()
        val byModule = linkedMapOf<String, Pair<Int, Int>>()
        val byCategory = linkedMapOf<QuestionCategory, Pair<Int, Int>>()

        state.questions.forEach { q ->
            val picked = state.mcAnswers[q.id]
            val isOk = picked == q.correctIndex
            adaptiveLearningStore?.recordResult(q.id, isOk)
            if (isOk) correct += 1 else wrongList += q
            val cur = byModule[q.moduleCode] ?: (0 to 0)
            byModule[q.moduleCode] = (cur.first + if (isOk) 1 else 0) to (cur.second + 1)

            val catCur = byCategory[q.category] ?: (0 to 0)
            byCategory[q.category] = (catCur.first + if (isOk) 1 else 0) to (catCur.second + 1)
        }

        val points = ((correct.toDouble() / total.toDouble()) * 100.0).toInt().coerceIn(0, 100)
        val grade = SihkCurriculum.calculateIhkGrade(points)
        val passed = points >= 50
        val xp = (correct * 15L) + if (passed) 60L else 20L
        val elapsedMin = (((state.totalExamSeconds - (state.examSecondsLeft ?: state.totalExamSeconds)) / 60)
            .coerceAtLeast(1)).toLong()

        val breakdowns = byModule.map { (code, pair) ->
            ModuleBreakdown(moduleCode = code, correct = pair.first, total = pair.second)
        }
        val catBreakdowns = byCategory.map { (cat, pair) ->
            CategoryBreakdown(category = cat, correct = pair.first, total = pair.second)
        }

        _session.update {
            it.copy(
                isFinished = true,
                correctCount = correct,
                errorCount = wrongList.size,
                earnedXp = xp,
                ihkPoints = points,
                ihkGrade = grade,
                moduleBreakdowns = breakdowns,
                categoryBreakdowns = catBreakdowns,
                wrongReviewItems = wrongList,
                statusMessage = "Prüfungsergebnis wird gespeichert…"
            )
        }

        viewModelScope.launch {
            try {
                wrongList.forEach { wrongQ ->
                    repository.recordWeakness(
                        topicId = wrongQ.topicId,
                        questionKey = wrongQ.id,
                        promptPreview = wrongQ.prompt
                    )
                }
                repository.saveExamAttempt(
                    ExamAttempt(
                        examMode = "MC_SIMULATION",
                        title = "MC-Prüfung ($total Fragen)",
                        scenarioSummary = "Strukturierte IHK-MC-Prüfung über BQ, HQ & Fachgespräch",
                        achievedPoints = points.toLong(),
                        maxPoints = 100L,
                        ihkGrade = grade.toLong(),
                        passed = passed,
                        durationMinutes = elapsedMin,
                        feedbackSummary = "$correct von $total Fragen richtig ($points P., IHK-Note $grade)"
                    )
                )
                repository.awardXpAndUpdateStreak(xpDelta = xp, studyMinutesDelta = elapsedMin)
                _session.update {
                    it.copy(
                        statusMessage = if (wrongList.isNotEmpty()) {
                            "+$xp XP gespeichert · ${wrongList.size} Fragen für „Falsch gedacht“ vorgemerkt."
                        } else {
                            "+$xp XP gespeichert · Fehlerfreie MC-Prüfung!"
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to persist MC exam", e)
                _session.update {
                    it.copy(statusMessage = "Hinweis: Speicherung fehlgeschlagen (${e.localizedMessage})")
                }
            }
        }
    }

    fun closeSession() {
        timerJob?.cancel()
        _session.value = LessonSessionState()
    }

    private fun finishSession() {
        timerJob?.cancel()
        val state = _session.value
        val total = state.questions.size.coerceAtLeast(1)
        val mastery = ((state.correctCount.toDouble() / total.toDouble()) * 100.0).toLong().coerceIn(0L, 100L)
        val ihkPts = mastery.toInt()
        val ihkGrd = SihkCurriculum.calculateIhkGrade(ihkPts)
        val xp = (state.correctCount * 25L) + if (state.correctCount == total) 25L else 10L

        _session.update {
            it.copy(
                isFinished = true,
                earnedXp = xp,
                ihkPoints = ihkPts,
                ihkGrade = ihkGrd,
                statusMessage = "Fortschritt & XP werden gespeichert…"
            )
        }

        viewModelScope.launch {
            try {
                if (state.sessionKind == SessionKind.TOPIC_BLOCK || state.sessionKind == SessionKind.PRACTICE_UNTIMED) {
                    repository.upsertLessonProgress(
                        LessonProgress(
                            lessonId = "${state.topicId}_core",
                            moduleCode = state.moduleCode,
                            topicId = state.topicId,
                            completed = mastery >= 50L,
                            xpEarned = xp,
                            masteryPercent = mastery,
                            errorCount = state.errorCount.toLong()
                        )
                    )
                }
                repository.awardXpAndUpdateStreak(xpDelta = xp, studyMinutesDelta = 5L)
                _session.update {
                    it.copy(statusMessage = "+$xp XP in deinem Meisterprofil gutgeschrieben.")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to persist lesson completion", e)
                _session.update {
                    it.copy(statusMessage = "Hinweis: Konnte nicht synchronisiert werden (${e.localizedMessage})")
                }
            }
        }
    }

    private fun startTimerIfSprint(question: ExamQuestion) {
        timerJob?.cancel()
        if (question.mode != DrillMode.KENNZAHLEN_SPRINT) {
            _session.update { it.copy(sprintSecondsLeft = null) }
            return
        }
        _session.update { it.copy(sprintSecondsLeft = 45) }
        timerJob = viewModelScope.launch {
            var remaining = 45
            while (remaining > 0) {
                delay(1000L)
                remaining -= 1
                _session.update { it.copy(sprintSecondsLeft = remaining) }
            }
            if (!_session.value.isSubmitted) {
                _session.update {
                    it.copy(
                        isSubmitted = true,
                        errorCount = it.errorCount + 1
                    )
                }
            }
        }
    }

    private fun startExamCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                val cur = _session.value
                if (!cur.isActive || cur.isFinished || cur.sessionKind != SessionKind.MC_EXAM) break
                val left = cur.examSecondsLeft ?: break
                if (left <= 1) {
                    finishMcExam()
                    break
                }
                _session.update { it.copy(examSecondsLeft = left - 1) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    private companion object {
        const val TAG = "LearnViewModel"
    }
}
