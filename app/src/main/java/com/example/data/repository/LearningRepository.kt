package com.example.data.repository

import android.content.Context
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.FormulaMasteryEntity
import com.example.data.local.LessonProgressEntity
import com.example.data.local.LogistikDao
import com.example.data.local.UserProfileEntity
import com.example.data.local.WeaknessEntity
import com.example.data.local.XelsLogistikDatabase
import com.example.data.model.ExamAttempt
import com.example.data.model.FormulaMastery
import com.example.data.model.LessonProgress
import com.example.data.model.MeisterRank
import com.example.data.model.UserProfile
import com.example.data.model.WeaknessItem
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class LearningRepository(
    private val dao: LogistikDao,
    private val localUserId: String = DEFAULT_LOCAL_USER_ID
) {
    constructor(context: Context) : this(
        dao = XelsLogistikDatabase.getInstance(context).logistikDao(),
        localUserId = DEFAULT_LOCAL_USER_ID
    )

    private fun todayIsoString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    }

    fun observeUserProfile(userId: String = localUserId): Flow<UserProfile?> {
        return dao.observeUserProfile(userId).map { it?.toDomain() }
    }

    suspend fun ensureUserProfile(displayName: String = DEFAULT_DISPLAY_NAME): Result<UserProfile> = runCatchingExceptCancel {
        val existing = dao.getUserProfile(localUserId)
        if (existing != null) {
            return@runCatchingExceptCancel existing.toDomain()
        }

        val cleanName = displayName.trim().ifBlank { DEFAULT_DISPLAY_NAME }.take(80)
        val today = todayIsoString()
        // A first launch should feel like a real study workspace, not an empty account.
        // All demo records live in Room and can be changed by the learner just like real data.
        val entity = UserProfileEntity(
            userId = localUserId,
            displayName = cleanName,
            xp = 1240L,
            streakDays = 6L,
            longestStreak = 12L,
            dailyGoalMinutes = 25L,
            todayMinutes = 12L,
            lastActiveDate = today,
            meisterRank = MeisterRank.fromXp(1240L).label
        )
        dao.upsertUserProfile(entity)
        seedDemoWorkspace()
        entity.toDomain()
    }

    private suspend fun seedDemoWorkspace() {
        val now = System.currentTimeMillis()
        listOf(
            LessonProgressEntity("demo_bq_1", localUserId, "BQ", "personal", true, 120, 82, 1, now),
            LessonProgressEntity("demo_bq_2", localUserId, "BQ", "logistics", true, 180, 74, 2, now),
            LessonProgressEntity("demo_hq_1", localUserId, "HQ", "leadership", true, 160, 91, 0, now),
            LessonProgressEntity("demo_hq_2", localUserId, "HQ", "economics", false, 0, 38, 3, now)
        ).forEach { dao.upsertLessonProgress(it) }
        listOf(
            FormulaMasteryEntity("demo_lager", localUserId, "Lagerumschlag", "BQ", 8, 3, true, now),
            FormulaMasteryEntity("demo_kapazitaet", localUserId, "Lagerkapazität", "BQ", 5, 2, true, now),
            FormulaMasteryEntity("demo_personal", localUserId, "Personalbedarf", "HQ", 3, 0, false, now)
        ).forEach { dao.upsertFormulaMastery(it) }
        listOf(
            ExamAttemptEntity("demo_exam_1", localUserId, "BQ", "Beschaffung & Lager", "Disposition für ein Ersatzteillager", 78, 100, 3, true, 32, "Gute Analyse; Sicherheitsbestand noch einmal wiederholen.", now - 86_400_000L),
            ExamAttemptEntity("demo_exam_2", localUserId, "HQ", "Führung & Personal", "Schichtplanung bei erhöhtem Krankenstand", 64, 100, 4, true, 41, "Solide bestanden. Arbeitsrechtliche Begründungen vertiefen.", now - 3 * 86_400_000L)
        ).forEach { dao.insertExamAttempt(it) }
        listOf(
            WeaknessEntity("demo_weakness_1", localUserId, "logistics", "Wie wird der Meldebestand unter Berücksichtigung der Lieferzeit berechnet?", 2, false, now),
            WeaknessEntity("demo_weakness_2", localUserId, "economics", "Welche Kosten zählen zu den auftragsfixen Kosten?", 1, false, now)
        ).forEach { dao.upsertWeakness(it) }
    }

    suspend fun updateDisplayName(displayName: String): Result<Unit> = runCatchingExceptCancel {
        val cleanName = displayName.trim().ifBlank { DEFAULT_DISPLAY_NAME }.take(80)
        val existing = dao.getUserProfile(localUserId)
        if (existing != null) {
            dao.upsertUserProfile(
                existing.copy(
                    displayName = cleanName,
                    updatedAtMillis = System.currentTimeMillis()
                )
            )
        } else {
            ensureUserProfile(cleanName).getOrThrow()
        }
    }

    suspend fun awardXpAndUpdateStreak(
        xpDelta: Long,
        studyMinutesDelta: Long = 5L
    ): Result<UserProfile> = runCatchingExceptCancel {
        val existing = dao.getUserProfile(localUserId) ?: run {
            ensureUserProfile().getOrThrow()
            dao.getUserProfile(localUserId)!!
        }

        val today = todayIsoString()
        val newXp = (existing.xp + xpDelta).coerceAtLeast(0L)
        val newRank = MeisterRank.fromXp(newXp).label
        val newStreak = if (existing.lastActiveDate == today) {
            existing.streakDays.coerceAtLeast(1L)
        } else {
            existing.streakDays + 1L
        }
        val newLongest = maxOf(existing.longestStreak, newStreak)
        val newTodayMinutes = if (existing.lastActiveDate == today) {
            (existing.todayMinutes + studyMinutesDelta).coerceAtMost(1440L)
        } else {
            studyMinutesDelta.coerceIn(0L, 1440L)
        }

        val updated = existing.copy(
            xp = newXp,
            streakDays = newStreak,
            longestStreak = newLongest,
            todayMinutes = newTodayMinutes,
            lastActiveDate = today,
            meisterRank = newRank,
            updatedAtMillis = System.currentTimeMillis()
        )
        dao.upsertUserProfile(updated)
        updated.toDomain()
    }

    suspend fun updateDailyGoalMinutes(minutes: Long): Result<Unit> = runCatchingExceptCancel {
        val clamped = minutes.coerceIn(5L, 240L)
        val existing = dao.getUserProfile(localUserId) ?: run {
            ensureUserProfile().getOrThrow()
            dao.getUserProfile(localUserId)!!
        }
        dao.upsertUserProfile(
            existing.copy(
                dailyGoalMinutes = clamped,
                updatedAtMillis = System.currentTimeMillis()
            )
        )
    }

    fun observeLessonProgress(userId: String = localUserId): Flow<List<LessonProgress>> {
        return dao.observeLessonProgress(userId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun upsertLessonProgress(progress: LessonProgress): Result<Unit> = runCatchingExceptCancel {
        require(progress.lessonId.isNotBlank()) { "lessonId darf nicht leer sein" }
        dao.upsertLessonProgress(
            LessonProgressEntity(
                lessonId = progress.lessonId,
                userId = localUserId,
                moduleCode = progress.moduleCode,
                topicId = progress.topicId,
                completed = progress.completed,
                xpEarned = progress.xpEarned.coerceAtLeast(0L),
                masteryPercent = progress.masteryPercent.coerceIn(0L, 100L),
                errorCount = progress.errorCount.coerceAtLeast(0L),
                updatedAtMillis = System.currentTimeMillis()
            )
        )
    }

    fun observeFormulaMastery(userId: String = localUserId): Flow<List<FormulaMastery>> {
        return dao.observeFormulaMastery(userId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun recordFormulaPractice(
        formulaId: String,
        formulaName: String,
        category: String,
        correct: Boolean
    ): Result<Unit> = runCatchingExceptCancel {
        val current = dao.getFormulaMastery(localUserId, formulaId)
        val nextSolved = (current?.solvedCount ?: 0L) + if (correct) 1L else 0L
        val nextStreak = if (correct) (current?.dojoStreak ?: 0L) + 1L else 0L
        dao.upsertFormulaMastery(
            FormulaMasteryEntity(
                formulaId = formulaId,
                userId = localUserId,
                formulaName = formulaName.take(120),
                category = category,
                solvedCount = nextSolved,
                dojoStreak = nextStreak,
                lastResultCorrect = correct,
                updatedAtMillis = System.currentTimeMillis()
            )
        )
    }

    fun observeExamAttempts(userId: String = localUserId): Flow<List<ExamAttempt>> {
        return dao.observeExamAttempts(userId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun saveExamAttempt(attempt: ExamAttempt): Result<String> = runCatchingExceptCancel {
        val points = attempt.achievedPoints.coerceIn(0L, 100L)
        val generatedId = attempt.id.ifBlank { UUID.randomUUID().toString() }
        dao.insertExamAttempt(
            ExamAttemptEntity(
                id = generatedId,
                userId = localUserId,
                examMode = attempt.examMode,
                title = attempt.title.ifBlank { "SIHK-Prüfungssimulation" }.take(160),
                scenarioSummary = attempt.scenarioSummary.ifBlank { "Praxisfall Logistikmeister" }.take(2000),
                achievedPoints = points,
                maxPoints = 100L,
                ihkGrade = attempt.ihkGrade.coerceIn(1L, 6L),
                passed = points >= 50L,
                durationMinutes = attempt.durationMinutes.coerceIn(0L, 480L),
                feedbackSummary = attempt.feedbackSummary.ifBlank { "Bewertet nach IHK-Schlüssel" }.take(4000),
                createdAtMillis = System.currentTimeMillis()
            )
        )
        generatedId
    }

    fun observeWeaknesses(userId: String = localUserId): Flow<List<WeaknessItem>> {
        return dao.observeWeaknesses(userId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun recordWeakness(
        topicId: String,
        questionKey: String,
        promptPreview: String
    ): Result<Unit> = runCatchingExceptCancel {
        val sanitizedId = questionKey.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").take(120)
        val existing = dao.getWeakness(localUserId, sanitizedId)
        val nextCount = if (existing != null) (existing.wrongCount + 1L).coerceAtMost(1000L) else 1L
        dao.upsertWeakness(
            WeaknessEntity(
                questionKey = sanitizedId,
                userId = localUserId,
                topicId = topicId.take(80),
                promptPreview = promptPreview.take(300),
                wrongCount = nextCount,
                resolved = false,
                updatedAtMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun resolveWeakness(weaknessId: String): Result<Unit> = runCatchingExceptCancel {
        dao.resolveWeakness(
            userId = localUserId,
            questionKey = weaknessId,
            updatedAt = System.currentTimeMillis()
        )
    }

    private inline fun <T> runCatchingExceptCancel(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val DEFAULT_LOCAL_USER_ID = "local_meister"
        const val DEFAULT_DISPLAY_NAME = "Meisterkandidat"
    }
}
