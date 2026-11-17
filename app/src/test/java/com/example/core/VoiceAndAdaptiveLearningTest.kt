package com.example.core

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.curriculum.DrillMode
import com.example.data.model.ExamQuestion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class VoiceAndAdaptiveLearningTest {

    @Test
    fun parseQuizVoiceCommand_handlesBooleanAndMultipleChoiceCommands() {
        val fourOptions = listOf(
            "A) ABC-Analyse nach Verbrauchswert",
            "B) XYZ-Analyse nach Schwankungen",
            "C) Nutzwertanalyse für Standorte",
            "D) Deckungsbeitragsrechnung I"
        )
        assertEquals(
            QuizVoiceAction.SelectOption(0),
            VoiceStudyController.parseQuizVoiceCommand("Antwort A", fourOptions)
        )
        assertEquals(
            QuizVoiceAction.SelectOption(1),
            VoiceStudyController.parseQuizVoiceCommand("Option B bitte", fourOptions)
        )
        assertEquals(
            QuizVoiceAction.SelectOption(2),
            VoiceStudyController.parseQuizVoiceCommand("3", fourOptions)
        )
        assertEquals(
            QuizVoiceAction.SelectOption(3),
            VoiceStudyController.parseQuizVoiceCommand("Antwort D", fourOptions)
        )
        assertEquals(
            QuizVoiceAction.SelectOption(1),
            VoiceStudyController.parseQuizVoiceCommand("Schwankungen", fourOptions)
        )
        // Plain "wahr" / "falsch" must not trigger arbitrary indices on 4-option MC questions
        assertNull(VoiceStudyController.parseQuizVoiceCommand("wahr", fourOptions))
        assertNull(VoiceStudyController.parseQuizVoiceCommand("falsch", fourOptions))

        val booleanOptions = listOf("Wahr", "Falsch")
        assertEquals(
            QuizVoiceAction.SelectOption(0),
            VoiceStudyController.parseQuizVoiceCommand("Wahr", booleanOptions)
        )
        assertEquals(
            QuizVoiceAction.SelectOption(1),
            VoiceStudyController.parseQuizVoiceCommand("Falsch", booleanOptions)
        )
    }

    @Test
    fun parseQuizVoiceCommand_handlesFlashcardAndNavigationCommands() {
        assertEquals(
            QuizVoiceAction.FlipOrReveal,
            VoiceStudyController.parseQuizVoiceCommand("Bitte aufdecken", emptyList())
        )
        assertEquals(
            QuizVoiceAction.RateKnown,
            VoiceStudyController.parseQuizVoiceCommand("Gewusst", emptyList())
        )
        assertEquals(
            QuizVoiceAction.RateUnsure,
            VoiceStudyController.parseQuizVoiceCommand("Noch unsicher", emptyList())
        )
        assertEquals(
            QuizVoiceAction.SubmitOrNext,
            VoiceStudyController.parseQuizVoiceCommand("Antwort prüfen", emptyList())
        )
        assertEquals(
            QuizVoiceAction.SubmitOrNext,
            VoiceStudyController.parseQuizVoiceCommand("Weiter", emptyList())
        )
    }

    @Test
    fun adaptiveLearningStore_promotesResetsAndPrioritizesWeakBoxes() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val store = AdaptiveLearningStore(context, userId = "test_user_${System.nanoTime()}")

        val questions = (1..6).map { idx ->
            ExamQuestion(
                id = "q_$idx",
                topicId = "hq_logistik",
                moduleCode = if (idx <= 3) "HQ" else "BQ",
                mode = DrillMode.PRUEFUNGS_CHECK,
                contextBanner = "Test",
                prompt = "Frage $idx?",
                options = listOf("Wahr", "Falsch"),
                correctIndex = 0,
                explanation = "Erklärung $idx"
            )
        }

        // Promote q_1 and q_2 to Box 5 (mastered)
        repeat(4) {
            store.recordResult("q_1", correct = true)
            store.recordResult("q_2", correct = true)
        }
        assertEquals(5, store.getBox("q_1"))

        // Fail q_1 -> resets immediately back to Box 1
        store.recordResult("q_1", correct = false)
        assertEquals(1, store.getBox("q_1"))

        // Promote q_3 to Box 3 (consolidating)
        store.recordResult("q_3", correct = true)
        store.recordResult("q_3", correct = true)
        assertEquals(3, store.getBox("q_3"))

        val stats = store.computeStats(questions)
        assertEquals(6, stats.totalQuestions)
        assertEquals(1, stats.masteredCount)      // q_2 in Box 5
        assertEquals(1, stats.consolidatingCount) // q_3 in Box 3
        assertEquals(4, stats.newOrWeakCount)     // q_1, q_4, q_5, q_6 in Box 1..2

        // Adaptive plan of size 4 must pick Box 1 items before Box 5 (q_2 must be excluded from top 4!)
        val plan = store.buildAdaptivePlanQuestions(questions, moduleFilter = "ALLE", count = 4)
        assertEquals(4, plan.size)
        val planIds = plan.map { it.id }.toSet()
        assertTrue(planIds.contains("q_1"))
        assertTrue(!planIds.contains("q_2"))
    }
}
