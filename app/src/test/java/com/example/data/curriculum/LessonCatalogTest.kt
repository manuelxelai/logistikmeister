package com.example.data.curriculum

import com.example.data.model.SihkCurriculum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonCatalogTest {

    @Test
    fun catalog_allQuestionsValidAndUnique() {
        val ids = LessonCatalog.questions.map { it.id }
        assertEquals("Question IDs must be unique", ids.size, ids.toSet().size)
        assertTrue("Catalog should have at least 135 Logistikmeister questions", LessonCatalog.questions.size >= 135)
        assertTrue(
            "QuizAcademy microlearning questions must be included",
            QuizAcademySyncBank.questions.all { qa -> ids.contains(qa.id) }
        )
        assertTrue(
            "DeinePruefung 4-option MC questions must be included",
            DeinePruefungSyncBank.questions.all { dp -> ids.contains(dp.id) && dp.options.size == 4 }
        )

        LessonCatalog.questions.forEach { q ->
            assertTrue("Question ${q.id} prompt blank", q.prompt.isNotBlank())
            assertTrue("Question ${q.id} options < 2", q.options.size >= 2)
            assertTrue("Question ${q.id} invalid correctIndex", q.correctIndex in q.options.indices)
            assertTrue("Question ${q.id} explanation blank", q.explanation.isNotBlank())
            assertFalse("No AEVO module allowed", q.moduleCode.equals("AEVO", ignoreCase = true))
        }
    }

    @Test
    fun catalog_everyCurriculumTopicHasQuestions() {
        assertFalse(SihkCurriculum.modules.any { it.code.equals("AEVO", ignoreCase = true) })
        SihkCurriculum.modules.flatMap { it.topics }.forEach { topic ->
            val qs = LessonCatalog.questionsForTopic(topic.id)
            assertTrue("Topic ${topic.id} should have >= 10 questions", qs.size >= 10)
            assertTrue("Topic ${topic.id} needs examDurationLabel", topic.examDurationLabel.isNotBlank())
        }
    }

    @Test
    fun quickTestAndMcExam_respectFiltersAndCounts() {
        val bqQuick = LessonCatalog.buildQuickTestQuestions(count = 10, moduleCodeFilter = "BQ")
        assertEquals(10, bqQuick.size)
        assertTrue(bqQuick.all { it.moduleCode == "BQ" })

        val hqQuick = LessonCatalog.buildQuickTestQuestions(count = 10, moduleCodeFilter = "HQ")
        assertEquals(10, hqQuick.size)
        assertTrue(hqQuick.all { it.moduleCode == "HQ" })

        val bqExam = LessonCatalog.buildMcExamQuestions(count = 25, moduleCodeFilter = "BQ")
        assertEquals(25, bqExam.size)
        assertTrue(bqExam.all { it.moduleCode == "BQ" })

        val hqExam = LessonCatalog.buildMcExamQuestions(count = 25, moduleCodeFilter = "HQ")
        assertEquals(25, hqExam.size)
        assertTrue(hqExam.all { it.moduleCode == "HQ" })

        val rechtExam = LessonCatalog.buildMcExamQuestions(count = 10, topicIdFilter = "bq_recht")
        assertEquals(10, rechtExam.size)
        assertTrue(rechtExam.all { it.topicId == "bq_recht" })

        val fullExam = LessonCatalog.buildMcExamQuestions(count = 40)
        assertEquals(40, fullExam.size)
        assertEquals(40, fullExam.map { it.id }.toSet().size)

        assertTrue(ExaminerTipsCatalog.tips.isNotEmpty())
        assertTrue(ExaminerTipsCatalog.operators.isNotEmpty())
        assertTrue(ExaminerTipsCatalog.keyNorms.isNotEmpty())
    }
}
