package com.example.core

import android.content.Context
import com.example.data.model.ExamQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LeitnerStats(
    val totalQuestions: Int,
    val newOrWeakCount: Int,
    val consolidatingCount: Int,
    val masteredCount: Int
) {
    val readinessPercent: Int
        get() {
            if (totalQuestions <= 0) return 0
            val weighted = (consolidatingCount * 0.55) + (masteredCount * 1.0)
            return ((weighted / totalQuestions.toDouble()) * 100.0).toInt().coerceIn(0, 100)
        }
}

/**
 * Adaptives 5-Box-Leitner-Lernsystem (inspiriert von QuizAcademy-Lernplan & Spaced Repetition):
 * - Box 1: Neu / Fehler / Unsicher -> höchste Priorität im adaptiven Lernplan
 * - Box 2..4: In Festigung -> mittlere Wiederholungsfrequenz
 * - Box 5: Sicher beherrscht -> wird nur noch selten zur Auffrischung eingestreut
 */
class AdaptiveLearningStore(
    context: Context,
    private val userId: String
) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val prefix = "box_${userId}_"

    private val _boxMap = MutableStateFlow(loadBoxes())
    val boxMap: StateFlow<Map<String, Int>> = _boxMap.asStateFlow()

    fun getBox(questionId: String): Int = _boxMap.value[questionId] ?: 1

    fun recordResult(questionId: String, correct: Boolean) {
        val current = getBox(questionId)
        val next = if (correct) {
            (current + 1).coerceAtMost(5)
        } else {
            1
        }
        prefs.edit().putInt("$prefix$questionId", next).apply()
        _boxMap.value = _boxMap.value + (questionId to next)
    }

    fun computeStats(allQuestions: List<ExamQuestion>): LeitnerStats {
        val map = _boxMap.value
        var weakOrNew = 0
        var consolidating = 0
        var mastered = 0
        allQuestions.forEach { q ->
            when (map[q.id] ?: 1) {
                in 1..2 -> weakOrNew += 1
                in 3..4 -> consolidating += 1
                else -> mastered += 1
            }
        }
        return LeitnerStats(
            totalQuestions = allQuestions.size,
            newOrWeakCount = weakOrNew,
            consolidatingCount = consolidating,
            masteredCount = mastered
        )
    }

    /**
     * Erstellt einen adaptiven Lern-Plan (analog "Lerne das, was du solltest"):
     * Priorisiert Box 1–2 (Fehler/Lücken) stark, ergänzt Box 3–4 (Festigung) und streut max.
     * wenige Box-5-Karten zur Langzeit-Absicherung ein.
     */
    fun buildAdaptivePlanQuestions(
        allQuestions: List<ExamQuestion>,
        moduleFilter: String? = null,
        count: Int = 15
    ): List<ExamQuestion> {
        val pool = if (moduleFilter.isNullOrBlank() || moduleFilter == "ALLE") {
            allQuestions
        } else {
            allQuestions.filter { it.moduleCode == moduleFilter }
        }.ifEmpty { allQuestions }

        val map = _boxMap.value
        val box1 = pool.filter { (map[it.id] ?: 1) == 1 }.shuffled()
        val box2to3 = pool.filter { (map[it.id] ?: 1) in 2..3 }.shuffled()
        val box4 = pool.filter { (map[it.id] ?: 1) == 4 }.shuffled()
        val box5 = pool.filter { (map[it.id] ?: 1) >= 5 }.shuffled()

        return (box1 + box2to3 + box4 + box5).take(count.coerceAtLeast(1))
    }

    private fun loadBoxes(): Map<String, Int> {
        return prefs.all.entries.mapNotNull { (k, v) ->
            if (k.startsWith(prefix) && v is Int) {
                k.removePrefix(prefix) to v.coerceIn(1, 5)
            } else {
                null
            }
        }.toMap()
    }

    private companion object {
        const val PREFS_NAME = "xels_logistik_leitner_boxes"
    }
}
