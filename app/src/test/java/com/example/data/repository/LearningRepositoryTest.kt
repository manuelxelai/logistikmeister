package com.example.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.XelsLogistikDatabase
import com.example.data.model.ExamAttempt
import com.example.data.model.LessonProgress
import com.example.data.model.MeisterRank
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class LearningRepositoryTest {

    private lateinit var database: XelsLogistikDatabase
    private lateinit var repository: LearningRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, XelsLogistikDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = LearningRepository(database.logistikDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `ensureUserProfile creates local profile and updates display name`() = runBlocking {
        val initial = repository.ensureUserProfile("Alex Logistik").getOrThrow()
        assertEquals("Alex Logistik", initial.displayName)
        assertEquals(1240L, initial.xp)
        assertEquals(25L, initial.dailyGoalMinutes)

        repository.updateDisplayName("Meister Alex").getOrThrow()
        val observed = repository.observeUserProfile().first()
        assertNotNull(observed)
        assertEquals("Meister Alex", observed?.displayName)
    }

    @Test
    fun `awardXpAndUpdateStreak updates xp rank and today minutes`() = runBlocking {
        repository.ensureUserProfile().getOrThrow()
        val updated = repository.awardXpAndUpdateStreak(xpDelta = 650L, studyMinutesDelta = 12L).getOrThrow()

        assertEquals(1890L, updated.xp)
        assertEquals(12L, updated.todayMinutes)
        assertEquals(MeisterRank.FACHKRAFT.label, updated.meisterRank)
        assertEquals(1L, updated.streakDays)
    }

    @Test
    fun `updateDailyGoalMinutes clamps value between 5 and 240`() = runBlocking {
        repository.ensureUserProfile().getOrThrow()
        repository.updateDailyGoalMinutes(500L).getOrThrow()
        val high = repository.observeUserProfile().first()
        assertEquals(240L, high?.dailyGoalMinutes)

        repository.updateDailyGoalMinutes(1L).getOrThrow()
        val low = repository.observeUserProfile().first()
        assertEquals(5L, low?.dailyGoalMinutes)
    }

    @Test
    fun `upsertLessonProgress persists and observes topic completion`() = runBlocking {
        repository.upsertLessonProgress(
            LessonProgress(
                lessonId = "log_01",
                moduleCode = "HQ1",
                topicId = "logistikprozesse",
                completed = true,
                xpEarned = 40L,
                masteryPercent = 90L,
                errorCount = 1L
            )
        ).getOrThrow()

        val progressList = repository.observeLessonProgress().first()
        assertEquals(1, progressList.size)
        assertEquals("log_01", progressList.first().lessonId)
        assertEquals(90L, progressList.first().masteryPercent)
        assertTrue(progressList.first().completed)
    }

    @Test
    fun `recordFormulaPractice increments solved count and resets streak on failure`() = runBlocking {
        repository.recordFormulaPractice("umschlag", "Umschlagshäufigkeit", "Bestand", correct = true).getOrThrow()
        repository.recordFormulaPractice("umschlag", "Umschlagshäufigkeit", "Bestand", correct = true).getOrThrow()
        var items = repository.observeFormulaMastery().first()
        assertEquals(1, items.size)
        assertEquals(2L, items.first().solvedCount)
        assertEquals(2L, items.first().dojoStreak)

        repository.recordFormulaPractice("umschlag", "Umschlagshäufigkeit", "Bestand", correct = false).getOrThrow()
        items = repository.observeFormulaMastery().first()
        assertEquals(2L, items.first().solvedCount)
        assertEquals(0L, items.first().dojoStreak)
        assertFalse(items.first().lastResultCorrect)
    }

    @Test
    fun `saveExamAttempt persists ihk attempt with passed flag`() = runBlocking {
        val attemptId = repository.saveExamAttempt(
            ExamAttempt(
                examMode = "HQ_WRITTEN",
                title = "HQ Logistikprozesse Simulation",
                scenarioSummary = "Hochregallager Engpass",
                achievedPoints = 78L,
                ihkGrade = 3L,
                durationMinutes = 42L,
                feedbackSummary = "Solide Kalkulation"
            )
        ).getOrThrow()

        assertTrue(attemptId.isNotBlank())
        val attempts = repository.observeExamAttempts().first()
        assertEquals(1, attempts.size)
        assertEquals(78L, attempts.first().achievedPoints)
        assertTrue(attempts.first().passed)
    }

    @Test
    fun `recordWeakness increments wrongCount and resolveWeakness hides it`() = runBlocking {
        repository.recordWeakness("bwh", "bwh:abc", "Was ist die Deckungsbeitragsrechnung?").getOrThrow()
        repository.recordWeakness("bwh", "bwh:abc", "Was ist die Deckungsbeitragsrechnung?").getOrThrow()

        val active = repository.observeWeaknesses().first()
        assertEquals(1, active.size)
        assertEquals(2L, active.first().wrongCount)
        assertFalse(active.first().resolved)

        repository.resolveWeakness(active.first().id).getOrThrow()
        val afterResolve = repository.observeWeaknesses().first()
        assertEquals(1, afterResolve.size)
        assertTrue(afterResolve.first().resolved)
    }
}
