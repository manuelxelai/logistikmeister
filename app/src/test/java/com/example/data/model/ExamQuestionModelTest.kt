package com.example.data.model

import com.example.data.curriculum.LessonCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExamQuestionModelTest {

    @Test
    fun questionCategory_supportsRequestedLogistikmeisterCategories() {
        assertEquals(
            QuestionCategory.LOGISTIKPROZESSE,
            QuestionCategory.fromTopicId("hq_logistik")
        )
        assertEquals(
            QuestionCategory.FUEHRUNG_UND_ZUSAMMENARBEIT,
            QuestionCategory.fromTopicId("bq_zib")
        )
        assertEquals(
            QuestionCategory.FUEHRUNG_UND_ZUSAMMENARBEIT,
            QuestionCategory.fromTopicId("hq_fuehrung")
        )
        assertEquals(
            QuestionCategory.BETRIEBSWIRTSCHAFTLICHES_HANDELN,
            QuestionCategory.fromTopicId("bq_bwl")
        )
        assertEquals(
            QuestionCategory.LOGISTIKPROZESSE,
            QuestionCategory.fromTopicId("logistikprozesse")
        )
    }

    @Test
    fun examQuestion_derivesCategorySectionAndDifficultyAutomatically() {
        val question = ExamQuestion(
            id = "test_q1",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Kanban-Steuerung",
            prompt = "Welche Aufgabe hat das Kanban-Prinzip?",
            options = listOf("Holprinzip zur Bestandsbegrenzung", "Blindproduktion auf Vorrat"),
            correctIndex = 0,
            explanation = "Kanban steuert Materialflüsse nach dem Pull-Prinzip."
        )

        assertEquals(QuestionCategory.LOGISTIKPROZESSE, question.category)
        assertEquals(ExamSection.HQ, question.section)
        assertEquals(QuestionDifficulty.PRUEFUNG, question.difficulty)
        assertTrue(question.matchesCategory(QuestionCategory.LOGISTIKPROZESSE))
        assertFalse(question.matchesCategory(QuestionCategory.BETRIEBSWIRTSCHAFTLICHES_HANDELN))
    }

    @Test
    fun lessonCatalog_filtersAndCountsAllCategoriesAccurately() {
        val logistikQuestions = LessonCatalog.questionsForCategory(QuestionCategory.LOGISTIKPROZESSE)
        assertTrue(logistikQuestions.isNotEmpty())
        assertTrue(logistikQuestions.all { it.category == QuestionCategory.LOGISTIKPROZESSE })

        val fuehrungQuestions = LessonCatalog.questionsForCategory(QuestionCategory.FUEHRUNG_UND_ZUSAMMENARBEIT)
        assertTrue(fuehrungQuestions.any { it.topicId == "bq_zib" })
        assertTrue(fuehrungQuestions.any { it.topicId == "hq_fuehrung" })
        assertTrue(fuehrungQuestions.size >= 16)

        val bwlQuestions = LessonCatalog.questionsForCategory(QuestionCategory.BETRIEBSWIRTSCHAFTLICHES_HANDELN)
        assertTrue(bwlQuestions.isNotEmpty())
        assertTrue(bwlQuestions.all { it.topicId == "bq_bwl" })

        val counts = LessonCatalog.categoryQuestionCounts()
        assertEquals(QuestionCategory.entries.size, counts.size)
        assertTrue(counts.values.all { it > 0 })
        assertEquals(LessonCatalog.questions.size, counts.values.sum())

        val quickLogistik = LessonCatalog.buildQuickTestQuestions(
            count = 5,
            categoryFilter = QuestionCategory.LOGISTIKPROZESSE
        )
        assertEquals(5, quickLogistik.size)
        assertTrue(quickLogistik.all { it.matchesCategory(QuestionCategory.LOGISTIKPROZESSE) })
    }
}
