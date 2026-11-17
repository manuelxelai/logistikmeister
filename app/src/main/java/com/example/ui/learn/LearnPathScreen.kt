package com.example.ui.learn

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.LeitnerStats
import com.example.core.QuizVoiceAction
import com.example.core.VoiceStudyController
import com.example.core.rememberVoiceInputTrigger
import com.example.data.curriculum.DrillMode
import com.example.data.curriculum.LessonCatalog
import com.example.data.model.CurriculumModule
import com.example.data.model.CurriculumTopic
import com.example.data.model.LessonProgress
import com.example.data.model.QuestionCategory
import com.example.data.model.SihkCurriculum
import com.example.data.model.WeaknessItem
import com.example.ui.theme.AmberStreak
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SkyCognitive
import java.util.Locale

@Composable
fun LearnPathScreen(
    learnViewModel: LearnViewModel,
    lessonProgress: List<LessonProgress>,
    weaknesses: List<WeaknessItem>,
    bookmarkedIds: Set<String> = emptySet(),
    onToggleBookmark: (String) -> Unit = {},
    leitnerBoxMap: Map<String, Int> = emptyMap(),
    voiceController: VoiceStudyController? = null,
    modifier: Modifier = Modifier
) {
    val session by learnViewModel.session.collectAsStateWithLifecycle()
    var quickTestFilter by remember { mutableStateOf("ALLE") }
    var showMcExamPicker by remember { mutableStateOf(false) }

    val leitnerStats = remember(leitnerBoxMap) {
        val qs = LessonCatalog.questions
        var weakOrNew = 0
        var consolidating = 0
        var mastered = 0
        qs.forEach { q ->
            when (leitnerBoxMap[q.id] ?: 1) {
                in 1..2 -> weakOrNew += 1
                in 3..4 -> consolidating += 1
                else -> mastered += 1
            }
        }
        LeitnerStats(
            totalQuestions = qs.size,
            newOrWeakCount = weakOrNew,
            consolidatingCount = consolidating,
            masteredCount = mastered
        )
    }

    if (session.isActive) {
        BackHandler {
            learnViewModel.closeSession()
        }
        if (session.sessionKind == SessionKind.FLASHCARDS) {
            FlashcardPlayerView(
                session = session,
                bookmarkedIds = bookmarkedIds,
                onToggleBookmark = onToggleBookmark,
                onFlipCard = learnViewModel::flipFlashcard,
                onRateCard = learnViewModel::rateFlashcard,
                onClose = learnViewModel::closeSession,
                voiceController = voiceController,
                modifier = modifier
            )
        } else {
            LessonPlayerView(
                session = session,
                bookmarkedIds = bookmarkedIds,
                onToggleBookmark = onToggleBookmark,
                onSelectOption = learnViewModel::selectOption,
                onSubmit = learnViewModel::submitAnswer,
                onNext = learnViewModel::nextQuestion,
                onPrevious = learnViewModel::previousQuestion,
                onJumpTo = learnViewModel::jumpToQuestion,
                onFinishExam = learnViewModel::finishMcExam,
                onClose = learnViewModel::closeSession,
                voiceController = voiceController,
                modifier = modifier
            )
        }
        return
    }

    val openWeaknesses = weaknesses.filter { !it.resolved }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 640.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Logistikmeister · Lern- & Fragenkatalog",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${LessonCatalog.questions.size} SIHK-Logistikmeister-Fragen (BQ, HQ & Fachgespräch) – im Übungsmodus ohne Zeitdruck, als Wahr/Falsch-Karten, 4-Optionen-MC-Prüfung & per Sprache.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ALLE" to "Alle Bereiche", "BQ" to "Nur BQ", "HQ" to "Nur HQ").forEach { (code, label) ->
                        FilterChip(
                            selected = quickTestFilter == code,
                            onClick = { quickTestFilter = code },
                            label = { Text("Fokus: $label") }
                        )
                    }
                }
            }

            item {
                AdaptiveLearningCockpitCard(
                    stats = leitnerStats,
                    quickTestFilter = quickTestFilter,
                    onStartAdaptiveQuiz = {
                        learnViewModel.startAdaptivePlan(
                            count = 15,
                            moduleFilter = quickTestFilter,
                            asFlashcards = false
                        )
                    },
                    onStartAdaptiveFlashcards = {
                        learnViewModel.startAdaptivePlan(
                            count = 15,
                            moduleFilter = quickTestFilter,
                            asFlashcards = true
                        )
                    }
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionDrillCard(
                            title = "Schnelltest (10 Fr.)",
                            subtitle = "Kompakt-Mix ($quickTestFilter)",
                            accent = SkyCognitive,
                            icon = Icons.Filled.Bolt,
                            onClick = { learnViewModel.startQuickTest(count = 10, moduleFilter = quickTestFilter) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_quick_test_button")
                        )
                        QuickActionDrillCard(
                            title = "„Falsch gedacht“",
                            subtitle = if (openWeaknesses.isEmpty()) "0 Fehler offen" else "${openWeaknesses.size} Fehler üben",
                            accent = SignalOrange,
                            icon = Icons.Filled.Refresh,
                            onClick = { learnViewModel.startSmartReview(weaknesses) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_smart_review_button")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionDrillCard(
                            title = "BQ/HQ-Prüfungsmodus",
                            subtitle = "25/40 Fr. · Note 1–6",
                            accent = EmeraldMastery,
                            icon = Icons.Filled.Quiz,
                            onClick = { showMcExamPicker = true },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_mc_exam_button")
                        )
                        QuickActionDrillCard(
                            title = "Alle im Block",
                            subtitle = "${LessonCatalog.questions.size} Fragen komplett",
                            accent = AmberStreak,
                            icon = Icons.Filled.LibraryBooks,
                            onClick = { learnViewModel.startAllQuestionsBlock() },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_all_block_button")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickActionDrillCard(
                            title = "Karteikarten",
                            subtitle = "15 Lernkarten ($quickTestFilter)",
                            accent = SkyCognitive,
                            icon = Icons.Filled.Flip,
                            onClick = {
                                learnViewModel.startFlashcardsSession(
                                    title = "Karteikarten ($quickTestFilter)",
                                    moduleFilter = quickTestFilter,
                                    count = 15
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_flashcards_button")
                        )
                        QuickActionDrillCard(
                            title = "Merkliste",
                            subtitle = if (bookmarkedIds.isEmpty()) "Noch keine markiert" else "${bookmarkedIds.size} gemerkte Fragen",
                            accent = SignalOrange,
                            icon = Icons.Filled.Bookmark,
                            onClick = {
                                if (bookmarkedIds.isNotEmpty()) {
                                    learnViewModel.startBookmarkedSession(bookmarkedIds, asFlashcards = true)
                                } else {
                                    learnViewModel.startFlashcardsSession(
                                        title = "Karteikarten-Mix",
                                        moduleFilter = quickTestFilter,
                                        count = 10
                                    )
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_bookmarks_button")
                        )
                    }
                }
            }

            item {
                val counts = remember { LessonCatalog.categoryQuestionCounts() }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Themen-Kategorien (Geprüfter Logistikmeister)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(QuestionCategory.entries, key = { it.id }) { category ->
                            val count = counts[category] ?: 0
                            CategoryHighlightCard(
                                category = category,
                                questionCount = count,
                                onClick = { learnViewModel.startCategorySession(category) }
                            )
                        }
                    }
                }
            }

            items(SihkCurriculum.modules, key = { it.id }) { module ->
                ModuleSkillTreeSection(
                    module = module,
                    lessonProgress = lessonProgress,
                    onStartTopic = { topic -> learnViewModel.startTopicSession(topic, untimed = true) },
                    onStartTopicExam = { topic ->
                        val count = LessonCatalog.questionsForTopic(topic.id).size.coerceAtMost(20)
                        learnViewModel.startMcExamSimulation(
                            questionCount = count,
                            durationMinutes = 20,
                            moduleFilter = topic.moduleCode,
                            topicIdFilter = topic.id,
                            customTitle = "Fach-Prüfung · ${topic.numberLabel}"
                        )
                    }
                )
            }
        }
    }

    if (showMcExamPicker) {
        val singleSubjects = remember {
            SihkCurriculum.modules
                .filter { it.code == "BQ" || it.code == "HQ" }
                .flatMap { it.topics }
        }
        AlertDialog(
            onDismissRequest = { showMcExamPicker = false },
            title = { Text("MC-Prüfungssimulation wählen") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Prüfungsmodus mit Zeitlimit, ohne Zwischenauflösung und direkter IHK-Benotung (100-Punkte-Schlüssel).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = {
                            showMcExamPicker = false
                            learnViewModel.startMcExamSimulation(25, 30, moduleFilter = "BQ")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyCognitive)
                    ) {
                        Text("BQ-Klausurcheck (25 Fragen · 30 Min.)")
                    }
                    Button(
                        onClick = {
                            showMcExamPicker = false
                            learnViewModel.startMcExamSimulation(25, 30, moduleFilter = "HQ")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
                    ) {
                        Text("HQ-Prüfungscheck (25 Fragen · 30 Min.)")
                    }
                    Button(
                        onClick = {
                            showMcExamPicker = false
                            learnViewModel.startMcExamSimulation(40, 45, moduleFilter = "ALLE")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldMastery)
                    ) {
                        Text("Voll-Simulation (40 Fragen · 45 Min.)")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Einzelfach-Simulation (20 Min.):",
                        style = MaterialTheme.typography.labelLarge
                    )
                    singleSubjects.forEach { topic ->
                        val qCount = LessonCatalog.questionsForTopic(topic.id).size.coerceAtMost(20)
                        OutlinedButton(
                            onClick = {
                                showMcExamPicker = false
                                learnViewModel.startMcExamSimulation(
                                    questionCount = qCount,
                                    durationMinutes = 20,
                                    moduleFilter = topic.moduleCode,
                                    topicIdFilter = topic.id,
                                    customTitle = "Fach-Prüfung · ${topic.numberLabel}"
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("${topic.numberLabel} · ${topic.title} ($qCount Fr.)")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMcExamPicker = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}

@Composable
private fun AdaptiveLearningCockpitCard(
    stats: LeitnerStats,
    quickTestFilter: String,
    onStartAdaptiveQuiz: () -> Unit,
    onStartAdaptiveFlashcards: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.2.dp, SkyCognitive.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = SkyCognitive,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Intelligenter Lernplan (5-Fächer-Leitner)",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Surface(
                    color = EmeraldMastery.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${stats.readinessPercent}% sicher",
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldMastery,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Priorisiert Box 1–2 (Fehler & Lücken) vor Box 3–4 (Festigung) und Box 5 (Langzeit-Wissen) – kombiniert Microlearning-Karten mit IHK-Fällen, Spracheingabe & Rechen-Dojo.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                LeitnerBoxChip(
                    label = "Box 1–2: ${stats.newOrWeakCount}",
                    accent = SignalOrange,
                    modifier = Modifier.weight(1f)
                )
                LeitnerBoxChip(
                    label = "Box 3–4: ${stats.consolidatingCount}",
                    accent = SkyCognitive,
                    modifier = Modifier.weight(1f)
                )
                LeitnerBoxChip(
                    label = "Box 5: ${stats.masteredCount}",
                    accent = EmeraldMastery,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartAdaptiveQuiz,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("start_adaptive_plan_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SkyCognitive)
                ) {
                    Text("Lernplan ($quickTestFilter · MC)")
                }
                OutlinedButton(
                    onClick = onStartAdaptiveFlashcards,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("start_adaptive_flashcards_button"),
                    border = BorderStroke(1.dp, SkyCognitive)
                ) {
                    Text("Als Lernkarten", color = SkyCognitive)
                }
            }
        }
    }
}

@Composable
private fun LeitnerBoxChip(
    label: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = accent.copy(alpha = 0.12f),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            fontFamily = JetBrainsMonoFontFamily,
            style = MaterialTheme.typography.labelSmall,
            color = accent,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun QuickActionDrillCard(
    title: String,
    subtitle: String,
    accent: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Surface(
                color = accent.copy(alpha = 0.14f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CategoryHighlightCard(
    category: QuestionCategory,
    questionCount: Int,
    onClick: () -> Unit
) {
    val accent = when (category.section.code) {
        "BQ" -> SkyCognitive
        "HQ" -> SignalOrange
        else -> EmeraldMastery
    }
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick)
            .testTag("category_card_${category.id}"),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.badgeCode,
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.labelSmall,
                    color = accent
                )
                Text(
                    text = "$questionCount Fragen",
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2
            )
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun ModuleSkillTreeSection(
    module: CurriculumModule,
    lessonProgress: List<LessonProgress>,
    onStartTopic: (CurriculumTopic) -> Unit,
    onStartTopicExam: (CurriculumTopic) -> Unit
) {
    val accent = when (module.code) {
        "BQ" -> SkyCognitive
        "HQ" -> SignalOrange
        else -> EmeraldMastery
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = accent.copy(alpha = 0.16f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = module.code,
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.labelLarge,
                    color = accent,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = module.title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = module.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        module.topics.forEach { topic ->
            val progress = lessonProgress.find { it.topicId == topic.id }
            val mastery = progress?.masteryPercent?.toInt() ?: 0
            val questionCount = LessonCatalog.questionsForTopic(topic.id).size

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStartTopic(topic) }
                    .testTag("learn_topic_${topic.id}"),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${topic.numberLabel} · ${topic.title}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "$questionCount Fragen · ${topic.examDurationLabel} · ${topic.summary}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = if (mastery >= 70) EmeraldMastery.copy(alpha = 0.15f) else accent.copy(alpha = 0.15f),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = if (progress?.completed == true) Icons.Filled.CheckCircle else Icons.Filled.PlayArrow,
                                contentDescription = "Übungsmodus starten",
                                tint = if (progress?.completed == true) EmeraldMastery else accent,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(22.dp)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { mastery / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = if (mastery >= 70) EmeraldMastery else accent,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onStartTopic(topic) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("topic_practice_${topic.id}"),
                            border = BorderStroke(1.dp, accent.copy(alpha = 0.6f))
                        ) {
                            Text("Üben (ohne Limit)", color = accent)
                        }
                        OutlinedButton(
                            onClick = { onStartTopicExam(topic) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("topic_exam_${topic.id}")
                        ) {
                            Text("Fach-Prüfung")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LessonPlayerView(
    session: LessonSessionState,
    bookmarkedIds: Set<String>,
    onToggleBookmark: (String) -> Unit,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onJumpTo: (Int) -> Unit,
    onFinishExam: () -> Unit,
    onClose: () -> Unit,
    voiceController: VoiceStudyController? = null,
    modifier: Modifier = Modifier
) {
    val isExam = session.sessionKind == SessionKind.MC_EXAM
    val activeQuestion = session.currentQuestion
    val isSpeaking by (voiceController?.isSpeaking?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val isListening by (voiceController?.isListening?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val voiceError by (voiceController?.voiceError?.collectAsStateWithLifecycle() ?: remember { mutableStateOf<String?>(null) })
    var autoReadEnabled by remember { mutableStateOf(false) }
    var voiceFeedback by remember { mutableStateOf<String?>(null) }

    val questionSpeechText = remember(activeQuestion) {
        activeQuestion?.let { q ->
            buildString {
                append(q.prompt)
                append(". ")
                q.options.forEachIndexed { idx, opt ->
                    append("Antwort ")
                    append('A' + idx)
                    append(": ")
                    append(opt)
                    append(". ")
                }
            }
        }.orEmpty()
    }

    DisposableEffect(voiceController) {
        onDispose {
            voiceController?.stopSpeaking()
            voiceController?.stopListening()
        }
    }

    LaunchedEffect(activeQuestion?.id, autoReadEnabled, session.isFinished) {
        if (voiceController != null && autoReadEnabled && !session.isFinished && questionSpeechText.isNotBlank()) {
            voiceController.speak(questionSpeechText)
        }
    }

    val voiceTrigger = voiceController?.let { controller ->
        rememberVoiceInputTrigger(
            voiceController = controller,
            onResult = { spoken ->
                val currentQ = session.currentQuestion
                when (val action = VoiceStudyController.parseQuizVoiceCommand(spoken, currentQ?.options.orEmpty())) {
                    is QuizVoiceAction.SelectOption -> {
                        if (isExam || !session.isSubmitted) {
                            onSelectOption(action.index)
                            val letter = 'A' + action.index
                            voiceFeedback = "Per Sprache gewählt: Antwort $letter („$spoken“)"
                        }
                    }

                    QuizVoiceAction.SubmitOrNext -> {
                        if (isExam) {
                            if (session.currentIndex + 1 < session.questions.size) {
                                onNext()
                                voiceFeedback = "Nächste Frage"
                            }
                        } else if (!session.isSubmitted && session.selectedOptionIndex != null) {
                            onSubmit()
                            voiceFeedback = "Antwort geprüft"
                        } else if (session.isSubmitted) {
                            onNext()
                            voiceFeedback = "Weiter zur nächsten Frage"
                        }
                    }

                    else -> {
                        voiceFeedback = "Erkannt: „$spoken“ – sage z. B. „Antwort A/B/C“, „Wahr/Falsch“, „Prüfen“ oder „Weiter“."
                    }
                }
            },
            onPermissionDenied = { msg -> voiceFeedback = msg }
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 640.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_lesson_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück zum Lernpfad"
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (!session.isFinished) {
                        val answeredInfo = if (isExam) {
                            " · ${session.mcAnswers.size}/${session.questions.size} beantwortet"
                        } else {
                            ""
                        }
                        Text(
                            text = "Frage ${session.currentIndex + 1} von ${session.questions.size}$answeredInfo",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (!session.isFinished && activeQuestion != null) {
                    if (voiceController != null) {
                        IconButton(
                            onClick = {
                                if (isSpeaking) {
                                    voiceController.stopSpeaking()
                                } else {
                                    voiceController.speak(questionSpeechText)
                                }
                            },
                            modifier = Modifier.testTag("lesson_tts_button")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Filled.StopCircle else Icons.Filled.VolumeUp,
                                contentDescription = if (isSpeaking) "Vorlesen stoppen" else "Frage und Optionen vorlesen",
                                tint = if (isSpeaking) SignalOrange else SkyCognitive
                            )
                        }
                        IconButton(
                            onClick = {
                                autoReadEnabled = !autoReadEnabled
                                if (!autoReadEnabled) {
                                    voiceController.stopSpeaking()
                                }
                                voiceFeedback = if (autoReadEnabled) {
                                    "Auto-Vorlesen aktiviert"
                                } else {
                                    "Auto-Vorlesen deaktiviert"
                                }
                            },
                            modifier = Modifier.testTag("lesson_autoread_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RecordVoiceOver,
                                contentDescription = if (autoReadEnabled) "Auto-Vorlesen ausschalten" else "Auto-Vorlesen einschalten",
                                tint = if (autoReadEnabled) EmeraldMastery else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (voiceTrigger != null) {
                        IconButton(
                            onClick = voiceTrigger,
                            modifier = Modifier.testTag("lesson_mic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "Antwort per Sprache eingeben",
                                tint = if (isListening) ErrorCrimson else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    val isBookmarked = bookmarkedIds.contains(activeQuestion.id)
                    IconButton(
                        onClick = { onToggleBookmark(activeQuestion.id) },
                        modifier = Modifier.testTag("lesson_bookmark_button")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Aus Merkliste entfernen" else "Zur Merkliste hinzufügen",
                            tint = if (isBookmarked) SignalOrange else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                val timerSeconds = session.examSecondsLeft ?: session.sprintSecondsLeft
                timerSeconds?.let { secs ->
                    if (!session.isFinished) {
                        Surface(
                            color = if (secs <= 60) ErrorCrimson else SkyCognitive,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Timer,
                                    contentDescription = "Timer",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                val text = if (isExam) {
                                    String.format(Locale.GERMANY, "%02d:%02d", secs / 60, secs % 60)
                                } else {
                                    "${secs}s"
                                }
                                Text(
                                    text = text,
                                    fontFamily = JetBrainsMonoFontFamily,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            if (session.isFinished) {
                SessionSummaryView(session = session, onClose = onClose)
                return@Column
            }

            val q = session.currentQuestion ?: return@Column
            val progressFraction = (session.currentIndex + 1).toFloat() / session.questions.size.coerceAtLeast(1).toFloat()

            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = if (isExam) EmeraldMastery else SignalOrange
            )

            val voiceBanner = when {
                isListening -> "Höre zu… Sage z. B. „Antwort A/B/C“, „Wahr“, „Falsch“, „Prüfen“ oder „Weiter“."
                !voiceError.isNullOrBlank() -> voiceError
                !voiceFeedback.isNullOrBlank() -> voiceFeedback
                else -> null
            }
            voiceBanner?.let { msg ->
                Surface(
                    color = if (isListening) ErrorCrimson.copy(alpha = 0.12f) else SkyCognitive.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            if (isExam) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(session.questions) { idx, itemQ ->
                        val isCurrent = idx == session.currentIndex
                        val isAnswered = session.mcAnswers.containsKey(itemQ.id)
                        Surface(
                            color = when {
                                isCurrent -> SignalOrange
                                isAnswered -> EmeraldMastery.copy(alpha = 0.22f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .size(34.dp)
                                .clickable { onJumpTo(idx) }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${idx + 1}",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = when (q.mode) {
                        DrillMode.PRAXIS_DIALOG -> SignalOrange.copy(alpha = 0.14f)
                        DrillMode.KENNZAHLEN_SPRINT -> SkyCognitive.copy(alpha = 0.14f)
                        DrillMode.PRUEFUNGS_CHECK -> EmeraldMastery.copy(alpha = 0.14f)
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${q.category.displayName} · ${q.contextBanner}",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }

                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text(
                        text = q.prompt,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(18.dp)
                    )
                }

                q.options.forEachIndexed { idx, optionText ->
                    val isSelected = session.selectedOptionIndex == idx
                    val isCorrectOption = idx == q.correctIndex
                    val showReveal = !isExam && session.isSubmitted
                    val borderColor = when {
                        !showReveal && isSelected -> SignalOrange
                        showReveal && isCorrectOption -> EmeraldMastery
                        showReveal && isSelected && !isCorrectOption -> ErrorCrimson
                        else -> MaterialTheme.colorScheme.outline
                    }
                    val containerColor = when {
                        showReveal && isCorrectOption -> EmeraldMastery.copy(alpha = 0.12f)
                        showReveal && isSelected && !isCorrectOption -> ErrorCrimson.copy(alpha = 0.12f)
                        isSelected -> SignalOrange.copy(alpha = 0.10f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = isExam || !session.isSubmitted) { onSelectOption(idx) }
                            .testTag("drill_option_$idx"),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        border = BorderStroke(if (isSelected || showReveal) 2.dp else 1.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f)
                            )
                            if (showReveal && isCorrectOption) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = "Richtig",
                                    tint = EmeraldMastery
                                )
                            } else if (showReveal && isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Falsch",
                                    tint = ErrorCrimson
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = !isExam && session.isSubmitted) {
                    val wasCorrect = session.selectedOptionIndex == q.correctIndex
                    Surface(
                        color = if (wasCorrect) EmeraldMastery.copy(alpha = 0.14f) else ErrorCrimson.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (wasCorrect) "Richtig! +${q.xpValue} XP" else "Für „Falsch gedacht“ vorgemerkt",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (wasCorrect) EmeraldMastery else ErrorCrimson,
                                    modifier = Modifier.weight(1f)
                                )
                                if (voiceController != null) {
                                    IconButton(
                                        onClick = {
                                            voiceController.speak("Lösung: ${q.options[q.correctIndex]}. Begründung: ${q.explanation}")
                                        },
                                        modifier = Modifier.testTag("lesson_read_explanation_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.VolumeUp,
                                            contentDescription = "Begründung vorlesen",
                                            tint = SkyCognitive
                                        )
                                    }
                                }
                            }
                            Text(
                                text = q.explanation,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            if (isExam) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onPrevious,
                        enabled = session.currentIndex > 0,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Zurück")
                    }
                    if (session.currentIndex + 1 < session.questions.size) {
                        Button(
                            onClick = onNext,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
                        ) {
                            Text("Weiter")
                        }
                    } else {
                        Button(
                            onClick = onFinishExam,
                            modifier = Modifier
                                .weight(1.4f)
                                .testTag("finish_mc_exam_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldMastery)
                        ) {
                            Text("Prüfung auswerten")
                        }
                    }
                }
                if (session.currentIndex + 1 < session.questions.size) {
                    OutlinedButton(
                        onClick = onFinishExam,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Prüfung vorzeitig beenden (${session.mcAnswers.size}/${session.questions.size} beantwortet)")
                    }
                }
            } else if (!session.isSubmitted) {
                Button(
                    onClick = onSubmit,
                    enabled = session.selectedOptionIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_drill_answer_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
                ) {
                    Text("Antwort prüfen")
                }
            } else {
                OutlinedButton(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("next_drill_question_button")
                ) {
                    Text(
                        if (session.currentIndex + 1 >= session.questions.size) "Einheit abschließen" else "Nächste Frage"
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionSummaryView(
    session: LessonSessionState,
    onClose: () -> Unit
) {
    val pts = session.ihkPoints ?: ((session.correctCount * 100) / session.questions.size.coerceAtLeast(1))
    val grade = session.ihkGrade ?: SihkCurriculum.calculateIhkGrade(pts)
    val passed = pts >= 50

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = if (passed) Icons.Filled.CheckCircle else Icons.Filled.Close,
                    contentDescription = null,
                    tint = if (passed) EmeraldMastery else ErrorCrimson,
                    modifier = Modifier.size(52.dp)
                )
                Text(
                    text = if (session.sessionKind == SessionKind.MC_EXAM) {
                        if (passed) "MC-Prüfung bestanden!" else "Noch nicht bestanden"
                    } else {
                        "Einheit abgeschlossen!"
                    },
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "${session.correctCount} von ${session.questions.size} richtig · $pts% · IHK-Note $grade",
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (passed) EmeraldMastery else ErrorCrimson
                )
                Text(
                    text = "+${session.earnedXp} XP",
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.titleLarge,
                    color = SignalOrange
                )
                session.statusMessage?.let { msg ->
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                SihkCurriculum.supplementaryOralTarget(pts)?.let { neededOral ->
                    Surface(
                        color = AmberStreak.copy(alpha = 0.16f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Ergänzungsprüfungs-Info (LogMstrV): Mit Note 5 ($pts P.) würdest du in der mündlichen Ergänzungsprüfung mind. $neededOral Punkte benötigen ((2×$pts + $neededOral)/3 = 50 P.).",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        if (session.categoryBreakdowns.isNotEmpty()) {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Auswertung nach Kategorien", style = MaterialTheme.typography.titleMedium)
                    session.categoryBreakdowns.forEach { bd ->
                        val frac = bd.correct.toFloat() / bd.total.coerceAtLeast(1).toFloat()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = bd.category.displayName,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${bd.correct}/${bd.total} (${(frac * 100).toInt()}%)",
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        LinearProgressIndicator(
                            progress = { frac },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = if (frac >= 0.5f) EmeraldMastery else ErrorCrimson,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        } else if (session.moduleBreakdowns.isNotEmpty()) {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Auswertung nach Handlungsbereichen", style = MaterialTheme.typography.titleMedium)
                    session.moduleBreakdowns.forEach { bd ->
                        val frac = bd.correct.toFloat() / bd.total.coerceAtLeast(1).toFloat()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = bd.moduleCode, style = MaterialTheme.typography.labelLarge)
                            Text(
                                text = "${bd.correct}/${bd.total} richtig (${(frac * 100).toInt()}%)",
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        LinearProgressIndicator(
                            progress = { frac },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = if (frac >= 0.5f) EmeraldMastery else ErrorCrimson,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }

        if (session.wrongReviewItems.isNotEmpty()) {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "„Falsch gedacht“ – Fehlerauflösung (${session.wrongReviewItems.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = ErrorCrimson
                    )
                    session.wrongReviewItems.forEach { wrong ->
                        Surface(
                            color = ErrorCrimson.copy(alpha = 0.08f),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = wrong.prompt, style = MaterialTheme.typography.labelLarge)
                                Text(
                                    text = "Lösung: ${wrong.options[wrong.correctIndex]}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldMastery
                                )
                                Text(
                                    text = wrong.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("finish_lesson_button")
        ) {
            Text("Zurück zur Übersicht")
        }
    }
}
