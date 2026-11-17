package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ExamAttempt
import com.example.data.model.FormulaMastery
import com.example.data.model.LessonProgress
import com.example.data.model.MeisterRank
import com.example.data.model.UserProfile
import com.example.data.model.WeaknessItem
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val userId: String,
    val displayName: String,
    val xp: Long = 0L,
    val streakDays: Long = 1L,
    val longestStreak: Long = 1L,
    val dailyGoalMinutes: Long = 15L,
    val todayMinutes: Long = 0L,
    val lastActiveDate: String = "",
    val meisterRank: String = MeisterRank.LAGERHELFER.label,
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    fun toDomain(): UserProfile = UserProfile(
        id = userId,
        userId = userId,
        displayName = displayName,
        xp = xp,
        streakDays = streakDays,
        longestStreak = longestStreak,
        dailyGoalMinutes = dailyGoalMinutes,
        todayMinutes = todayMinutes,
        lastActiveDate = lastActiveDate,
        meisterRank = meisterRank
    )
}

@Entity(tableName = "lesson_progress")
data class LessonProgressEntity(
    @PrimaryKey val lessonId: String,
    val userId: String,
    val moduleCode: String,
    val topicId: String,
    val completed: Boolean,
    val xpEarned: Long,
    val masteryPercent: Long,
    val errorCount: Long,
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    fun toDomain(): LessonProgress = LessonProgress(
        id = lessonId,
        lessonId = lessonId,
        moduleCode = moduleCode,
        topicId = topicId,
        completed = completed,
        xpEarned = xpEarned,
        masteryPercent = masteryPercent,
        errorCount = errorCount
    )
}

@Entity(tableName = "formula_mastery")
data class FormulaMasteryEntity(
    @PrimaryKey val formulaId: String,
    val userId: String,
    val formulaName: String,
    val category: String,
    val solvedCount: Long,
    val dojoStreak: Long,
    val lastResultCorrect: Boolean,
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    fun toDomain(): FormulaMastery = FormulaMastery(
        id = formulaId,
        formulaId = formulaId,
        formulaName = formulaName,
        category = category,
        solvedCount = solvedCount,
        dojoStreak = dojoStreak,
        lastResultCorrect = lastResultCorrect
    )
}

@Entity(tableName = "exam_attempts")
data class ExamAttemptEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val examMode: String,
    val title: String,
    val scenarioSummary: String,
    val achievedPoints: Long,
    val maxPoints: Long,
    val ihkGrade: Long,
    val passed: Boolean,
    val durationMinutes: Long,
    val feedbackSummary: String,
    val createdAtMillis: Long = System.currentTimeMillis()
) {
    fun toDomain(): ExamAttempt = ExamAttempt(
        id = id,
        examMode = examMode,
        title = title,
        scenarioSummary = scenarioSummary,
        achievedPoints = achievedPoints,
        maxPoints = maxPoints,
        ihkGrade = ihkGrade,
        passed = passed,
        durationMinutes = durationMinutes,
        feedbackSummary = feedbackSummary
    )
}

@Entity(tableName = "weaknesses")
data class WeaknessEntity(
    @PrimaryKey val questionKey: String,
    val userId: String,
    val topicId: String,
    val promptPreview: String,
    val wrongCount: Long,
    val resolved: Boolean,
    val updatedAtMillis: Long = System.currentTimeMillis()
) {
    fun toDomain(): WeaknessItem = WeaknessItem(
        id = questionKey,
        topicId = topicId,
        questionKey = questionKey,
        promptPreview = promptPreview,
        wrongCount = wrongCount,
        resolved = resolved
    )
}

@Dao
interface LogistikDao {
    @Query("SELECT * FROM user_profiles WHERE userId = :userId LIMIT 1")
    fun observeUserProfile(userId: String): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles WHERE userId = :userId LIMIT 1")
    suspend fun getUserProfile(userId: String): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUserProfile(entity: UserProfileEntity)

    @Query("SELECT * FROM lesson_progress WHERE userId = :userId")
    fun observeLessonProgress(userId: String): Flow<List<LessonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLessonProgress(entity: LessonProgressEntity)

    @Query("SELECT * FROM formula_mastery WHERE userId = :userId")
    fun observeFormulaMastery(userId: String): Flow<List<FormulaMasteryEntity>>

    @Query("SELECT * FROM formula_mastery WHERE userId = :userId AND formulaId = :formulaId LIMIT 1")
    suspend fun getFormulaMastery(userId: String, formulaId: String): FormulaMasteryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFormulaMastery(entity: FormulaMasteryEntity)

    @Query("SELECT * FROM exam_attempts WHERE userId = :userId ORDER BY createdAtMillis DESC")
    fun observeExamAttempts(userId: String): Flow<List<ExamAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamAttempt(entity: ExamAttemptEntity)

    @Query("SELECT * FROM weaknesses WHERE userId = :userId")
    fun observeWeaknesses(userId: String): Flow<List<WeaknessEntity>>

    @Query("SELECT * FROM weaknesses WHERE userId = :userId AND questionKey = :questionKey LIMIT 1")
    suspend fun getWeakness(userId: String, questionKey: String): WeaknessEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWeakness(entity: WeaknessEntity)

    @Query("UPDATE weaknesses SET resolved = 1, updatedAtMillis = :updatedAt WHERE userId = :userId AND questionKey = :questionKey")
    suspend fun resolveWeakness(userId: String, questionKey: String, updatedAt: Long)
}

@Database(
    entities = [
        UserProfileEntity::class,
        LessonProgressEntity::class,
        FormulaMasteryEntity::class,
        ExamAttemptEntity::class,
        WeaknessEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class XelsLogistikDatabase : RoomDatabase() {
    abstract fun logistikDao(): LogistikDao

    companion object {
        @Volatile
        private var INSTANCE: XelsLogistikDatabase? = null

        fun getInstance(context: Context): XelsLogistikDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    XelsLogistikDatabase::class.java,
                    "xels_logistikmeister.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
