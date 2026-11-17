package com.example.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.curriculum.LessonCatalog
import com.example.data.formulas.LogisticsFormulaEngine
import com.example.data.model.ExamAttempt
import com.example.data.model.LessonProgress
import com.example.data.model.MeisterRank
import com.example.data.model.UserProfile
import com.example.ui.theme.AmberStreak
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.Navy950
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SkyCognitive

@Composable
fun DashboardScreen(
    data: DashboardUiState.Ready,
    onStartSmartReview: () -> Unit,
    onStartQuickTest: () -> Unit,
    onStartMcExam: () -> Unit,
    onStartDailySprint: () -> Unit,
    onOpenExaminerTips: () -> Unit,
    onUpdateDailyGoalMinutes: (Long) -> Unit,
    onNavigateToFormulas: () -> Unit,
    onNavigateToExam: () -> Unit,
    modifier: Modifier = Modifier
) {
    val openWeaknesses = data.weaknesses.filter { !it.resolved }
    val bqMastery = computeModuleMastery(data.lessonProgress, "BQ")
    val hqMastery = computeModuleMastery(data.lessonProgress, "HQ")
    val formulaRatio = (data.formulaMastery.count { it.solvedCount > 0 }.toFloat() /
        LogisticsFormulaEngine.formulas.size.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)

    var showDailyGoalDialog by remember { mutableStateOf(false) }
    var showRanksDialog by remember { mutableStateOf(false) }
    var showGradeScaleDialog by remember { mutableStateOf(false) }

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
                MeisterHeroCard(
                    profile = data.profile,
                    onShowRanks = { showRanksDialog = true }
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardShortcutCard(
                            title = "Schnelltest (10 Fr.)",
                            subtitle = "Pool: ${LessonCatalog.questions.size} Fragen",
                            icon = Icons.Filled.Bolt,
                            accent = SkyCognitive,
                            onClick = onStartQuickTest,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_quick_test_card")
                        )
                        DashboardShortcutCard(
                            title = "„Falsch gedacht“",
                            subtitle = if (openWeaknesses.isEmpty()) "Keine offenen Fehler" else "${openWeaknesses.size} Fehler wiederholen",
                            icon = Icons.Filled.Refresh,
                            accent = SignalOrange,
                            onClick = onStartSmartReview,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_falsch_gedacht_card")
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DashboardShortcutCard(
                            title = "BQ/HQ-Prüfung (25 Fr.)",
                            subtitle = "30 Min. · IHK-Note 1–6",
                            icon = Icons.Filled.Quiz,
                            accent = EmeraldMastery,
                            onClick = onStartMcExam,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_mc_exam_card")
                        )
                        DashboardShortcutCard(
                            title = "Logistik-Prüfungstipps",
                            subtitle = "Vom SIHK-Prüfer",
                            icon = Icons.Filled.Lightbulb,
                            accent = AmberStreak,
                            onClick = onOpenExaminerTips,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_examiner_tips_card")
                        )
                    }
                }
            }

            item {
                DailyGoalAndSmartReviewCard(
                    profile = data.profile,
                    openWeaknessesCount = openWeaknesses.size,
                    onStartSmartReview = onStartSmartReview,
                    onStartDailySprint = onStartDailySprint,
                    onEditDailyGoal = { showDailyGoalDialog = true }
                )
            }

            item {
                MasteryRadarOverviewCard(
                    bqMastery = bqMastery,
                    hqMastery = hqMastery,
                    formulaRatio = formulaRatio,
                    examAttempts = data.examAttempts
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardShortcutCard(
                        title = "Rechen-Dojo",
                        subtitle = "${data.formulaMastery.sumOf { it.solvedCount }} Aufgaben gelöst",
                        icon = Icons.Filled.Calculate,
                        accent = SkyCognitive,
                        onClick = onNavigateToFormulas,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dashboard_formulas_shortcut")
                    )
                    DashboardShortcutCard(
                        title = "KI-Ausschuss & Fall",
                        subtitle = "${data.examAttempts.size} Simulationen",
                        icon = Icons.Filled.School,
                        accent = EmeraldMastery,
                        onClick = onNavigateToExam,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dashboard_exam_shortcut")
                    )
                }
            }

            item {
                OutlinedButton(
                    onClick = { showGradeScaleDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dashboard_grade_scale_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("IHK 100-Punkte-Notenschlüssel & Bestehensgrenzen")
                }
            }
        }
    }

    if (showDailyGoalDialog) {
        val options = listOf(10L, 15L, 30L, 45L, 60L)
        AlertDialog(
            onDismissRequest = { showDailyGoalDialog = false },
            title = { Text("Tagesziel anpassen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Wähle dein tägliches Lernziel für die SIHK-Vorbereitung:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        options.take(3).forEach { mins ->
                            FilterChip(
                                selected = data.profile.dailyGoalMinutes == mins,
                                onClick = {
                                    onUpdateDailyGoalMinutes(mins)
                                    showDailyGoalDialog = false
                                },
                                label = { Text("${mins}m") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        options.drop(3).forEach { mins ->
                            FilterChip(
                                selected = data.profile.dailyGoalMinutes == mins,
                                onClick = {
                                    onUpdateDailyGoalMinutes(mins)
                                    showDailyGoalDialog = false
                                },
                                label = { Text("${mins}m") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDailyGoalDialog = false }) {
                    Text("Schließen")
                }
            }
        )
    }

    if (showRanksDialog) {
        val currentRank = MeisterRank.fromXp(data.profile.xp)
        AlertDialog(
            onDismissRequest = { showRanksDialog = false },
            title = { Text("Meister-Rang-Übersicht") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MeisterRank.entries.forEach { rank ->
                        val isCurrent = rank == currentRank
                        val isUnlocked = data.profile.xp >= rank.minXp
                        Surface(
                            color = if (isCurrent) SignalOrange.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(10.dp),
                            border = if (isCurrent) BorderStroke(1.dp, SignalOrange) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = rank.label,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (isCurrent) SignalOrange else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "ab ${rank.minXp} XP",
                                        fontFamily = JetBrainsMonoFontFamily,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (isUnlocked) {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = "Freigeschaltet",
                                        tint = if (isCurrent) SignalOrange else EmeraldMastery,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRanksDialog = false }) {
                    Text("Verstanden")
                }
            }
        )
    }

    if (showGradeScaleDialog) {
        val scale = listOf(
            Triple("92 – 100 P.", "Note 1 (sehr gut)", EmeraldMastery),
            Triple("81 – 91 P.", "Note 2 (gut)", EmeraldMastery),
            Triple("67 – 80 P.", "Note 3 (befriedigend)", SkyCognitive),
            Triple("50 – 66 P.", "Note 4 (ausreichend · Bestanden)", AmberStreak),
            Triple("30 – 49 P.", "Note 5 (mangelhaft · Nicht best.)", ErrorCrimson),
            Triple("0 – 29 P.", "Note 6 (ungenügend · Nicht best.)", ErrorCrimson)
        )
        AlertDialog(
            onDismissRequest = { showGradeScaleDialog = false },
            title = { Text("IHK-Notenschlüssel (100 Punkte)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Ab 50 Punkten gilt eine schriftliche Situationsaufgabe bzw. das Fachgespräch als bestanden.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    scale.forEach { (pts, gradeLabel, accent) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = pts,
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.labelLarge,
                                color = accent
                            )
                            Text(
                                text = gradeLabel,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGradeScaleDialog = false }) {
                    Text("Schließen")
                }
            }
        )
    }
}

@Composable
private fun MeisterHeroCard(
    profile: UserProfile,
    onShowRanks: () -> Unit
) {
    val rank = MeisterRank.fromXp(profile.xp)
    val span = (rank.nextThresholdXp - rank.minXp).coerceAtLeast(1L)
    val progress = ((profile.xp - rank.minXp).toFloat() / span.toFloat()).coerceIn(0f, 1f)

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_logistics),
                    contentDescription = "Xels Prüfungssimulator Hub Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(listOf(Color.Transparent, Navy950.copy(alpha = 0.9f)))
                        )
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onShowRanks)
                            .testTag("hero_rank_button")
                    ) {
                        Text(
                            text = "Hallo, ${profile.displayName}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.MilitaryTech,
                                contentDescription = "Rang-Übersicht anzeigen",
                                tint = AmberStreak,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.meisterRank} ▸",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.92f)
                            )
                        }
                    }
                    Surface(
                        color = SignalOrange,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = "Tagesstreak",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.streakDays} T.",
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Bolt,
                            contentDescription = null,
                            tint = SignalOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${profile.xp} XP",
                            fontFamily = JetBrainsMonoFontFamily,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = "Nächster Rang: ${rank.nextThresholdXp} XP",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = SignalOrange,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DailyGoalAndSmartReviewCard(
    profile: UserProfile,
    openWeaknessesCount: Int,
    onStartSmartReview: () -> Unit,
    onStartDailySprint: () -> Unit,
    onEditDailyGoal: () -> Unit
) {
    val goalFraction = (profile.todayMinutes.toFloat() / profile.dailyGoalMinutes.coerceAtLeast(1L).toFloat()).coerceIn(0f, 1f)
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable(onClick = onEditDailyGoal)
                    .testTag("edit_daily_goal_button")
            ) {
                Box(
                    modifier = Modifier.size(74.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val stroke = 8.dp.toPx()
                        drawArc(
                            color = trackColor,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round),
                            topLeft = Offset(stroke / 2, stroke / 2),
                            size = Size(size.width - stroke, size.height - stroke)
                        )
                        drawArc(
                            color = EmeraldMastery,
                            startAngle = -90f,
                            sweepAngle = 360f * goalFraction,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round),
                            topLeft = Offset(stroke / 2, stroke / 2),
                            size = Size(size.width - stroke, size.height - stroke)
                        )
                    }
                    Text(
                        text = "${profile.todayMinutes}/${profile.dailyGoalMinutes}m",
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Tagesziel bearbeiten",
                        tint = SkyCognitive,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Ziel",
                        style = MaterialTheme.typography.labelSmall,
                        color = SkyCognitive
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = if (openWeaknessesCount > 0) "„Falsch gedacht“ bereit" else "Tages-Sprint aktiv",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (openWeaknessesCount > 0) {
                        "$openWeaknessesCount falsch beantwortete Fragen warten auf Wiederholung."
                    } else {
                        "Halte deinen ${profile.streakDays}-Tage-Streak mit einem 45s Kennzahlen-Sprint!"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = if (openWeaknessesCount > 0) onStartSmartReview else onStartDailySprint,
                    modifier = Modifier.testTag("dashboard_primary_action_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (openWeaknessesCount > 0) SignalOrange else SkyCognitive
                    )
                ) {
                    Icon(
                        imageVector = if (openWeaknessesCount > 0) Icons.Filled.Refresh else Icons.Filled.Bolt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (openWeaknessesCount > 0) "Fehler wiederholen" else "Tages-Sprint starten")
                }
            }
        }
    }
}

@Composable
private fun MasteryRadarOverviewCard(
    bqMastery: Float,
    hqMastery: Float,
    formulaRatio: Float,
    examAttempts: List<ExamAttempt>
) {
    val avgExamPoints = if (examAttempts.isEmpty()) 0f else (examAttempts.map { it.achievedPoints }.average().toFloat() / 100f)
    val passRate = if (examAttempts.isEmpty()) null else ((examAttempts.count { it.passed } * 100) / examAttempts.size)
    val latestAttempt = examAttempts.firstOrNull()
    val readinessScore = (((bqMastery + hqMastery + formulaRatio + avgExamPoints) / 4f) * 100f).toInt().coerceIn(0, 100)
    val (readinessLabel, readinessColor) = when {
        readinessScore >= 75 -> "Prüfungsreif ($readinessScore %)" to EmeraldMastery
        readinessScore >= 45 -> "Auf Kurs ($readinessScore %)" to SkyCognitive
        else -> "Grundlagen aufbauen ($readinessScore %)" to AmberStreak
    }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                Text(
                    text = "Statistik & Kompetenzprofil",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = readinessColor.copy(alpha = 0.16f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = readinessLabel,
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                        color = readinessColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            MasteryBarRow(label = "BQ (Grundlegende Qualifikationen)", fraction = bqMastery, color = SkyCognitive)
            MasteryBarRow(label = "HQ (Handlungsspezifisch)", fraction = hqMastery, color = SignalOrange)
            MasteryBarRow(label = "Formelsammlung & Dojo", fraction = formulaRatio, color = EmeraldMastery)
            MasteryBarRow(
                label = if (examAttempts.isEmpty()) "Prüfungs-Ø (noch kein Versuch)" else "Prüfungs-Ø (${examAttempts.size} Versuche)",
                fraction = avgExamPoints,
                color = AmberStreak
            )
            if (passRate != null && latestAttempt != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = EmeraldMastery.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Bestehensquote", style = MaterialTheme.typography.labelSmall)
                            Text(
                                text = "$passRate%",
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.titleMedium,
                                color = EmeraldMastery
                            )
                        }
                    }
                    Surface(
                        color = SkyCognitive.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Letzte IHK-Note", style = MaterialTheme.typography.labelSmall)
                            Text(
                                text = "Note ${latestAttempt.ihkGrade} (${latestAttempt.achievedPoints} P.)",
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.titleMedium,
                                color = SkyCognitive
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MasteryBarRow(label: String, fraction: Float, color: Color) {
    val pct = (fraction * 100).toInt().coerceIn(0, 100)
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "$pct%",
                fontFamily = JetBrainsMonoFontFamily,
                style = MaterialTheme.typography.labelMedium,
                color = color
            )
        }
        LinearProgressIndicator(
            progress = { fraction.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(CircleShape),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun DashboardShortcutCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = accent, modifier = Modifier.size(24.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun computeModuleMastery(list: List<LessonProgress>, moduleCode: String): Float {
    val subset = list.filter { it.moduleCode == moduleCode }
    if (subset.isEmpty()) return 0f
    return (subset.map { it.masteryPercent }.average() / 100.0).toFloat().coerceIn(0f, 1f)
}
