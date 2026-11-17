package com.example.data.model

enum class MeisterRank(
    val label: String,
    val minXp: Long,
    val nextThresholdXp: Long
) {
    LAGERHELFER("Lagerhelfer", 0L, 500L),
    FACHKRAFT("Fachkraft für Lagerlogistik", 500L, 1500L),
    SCHICHTFUEHRER("Schichtführer", 1500L, 3500L),
    KOORDINATOR("Logistik-Koordinator", 3500L, 7000L),
    LOGISTIKMEISTER("Geprüfter Logistikmeister (SIHK)", 7000L, 10000L);

    companion object {
        fun fromXp(xp: Long): MeisterRank {
            return entries.lastOrNull { xp >= it.minXp } ?: LAGERHELFER
        }
    }
}

data class UserProfile(
    val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val xp: Long = 0L,
    val streakDays: Long = 0L,
    val longestStreak: Long = 0L,
    val dailyGoalMinutes: Long = 15L,
    val todayMinutes: Long = 0L,
    val lastActiveDate: String = "",
    val meisterRank: String = MeisterRank.LAGERHELFER.label
)

data class LessonProgress(
    val id: String = "",
    val lessonId: String = "",
    val moduleCode: String = "BQ",
    val topicId: String = "",
    val completed: Boolean = false,
    val xpEarned: Long = 0L,
    val masteryPercent: Long = 0L,
    val errorCount: Long = 0L
)

data class FormulaMastery(
    val id: String = "",
    val formulaId: String = "",
    val formulaName: String = "",
    val category: String = "BQ",
    val solvedCount: Long = 0L,
    val dojoStreak: Long = 0L,
    val lastResultCorrect: Boolean = false
)

data class ExamAttempt(
    val id: String = "",
    val examMode: String = "BQ",
    val title: String = "",
    val scenarioSummary: String = "",
    val achievedPoints: Long = 0L,
    val maxPoints: Long = 100L,
    val ihkGrade: Long = 5L,
    val passed: Boolean = false,
    val durationMinutes: Long = 0L,
    val feedbackSummary: String = ""
)

data class WeaknessItem(
    val id: String = "",
    val topicId: String = "",
    val questionKey: String = "",
    val promptPreview: String = "",
    val wrongCount: Long = 1L,
    val resolved: Boolean = false
)
