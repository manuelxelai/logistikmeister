package com.example.ui.exam

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.core.VoiceStudyController
import com.example.core.rememberVoiceInputTrigger
import com.example.data.ai.ExamEvaluationResult
import com.example.data.curriculum.ExaminerTipsCatalog
import com.example.data.model.ExamAttempt
import com.example.data.model.SihkCurriculum
import com.example.ui.theme.AmberStreak
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.Navy950
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SkyCognitive
import java.util.Locale

@Composable
fun ExamSimulatorScreen(
    examViewModel: ExamViewModel,
    examAttempts: List<ExamAttempt>,
    onStartMcExam: () -> Unit = {},
    voiceController: VoiceStudyController? = null,
    modifier: Modifier = Modifier
) {
    val writtenState by examViewModel.writtenState.collectAsStateWithLifecycle()
    val oralState by examViewModel.oralState.collectAsStateWithLifecycle()
    var activeTab by remember { mutableIntStateOf(0) } // 0=Schriftlich, 1=Fachgespräch, 2=Historie
    var showGradeScaleDialog by remember { mutableStateOf(false) }

    DisposableEffect(voiceController) {
        onDispose {
            voiceController?.stopSpeaking()
            voiceController?.stopListening()
        }
    }

    if (writtenState is WrittenExamState.Active) {
        BackHandler { examViewModel.resetWrittenExam() }
    } else if (oralState is OralExamState.Active) {
        BackHandler { examViewModel.resetOralExam() }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 640.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SIHK-Prüfungssimulator",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { showGradeScaleDialog = true },
                        modifier = Modifier.testTag("exam_grade_scale_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "IHK-Notenschlüssel",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Noten")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Situationsaufgaben nach 100-Punkte-IHK-Schlüssel & mündliches Fachgespräch.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3),
                        icon = { Icon(Icons.Filled.Assignment, contentDescription = null) }
                    ) {
                        Text("Schriftlich")
                    }
                    SegmentedButton(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3),
                        icon = { Icon(Icons.Filled.RecordVoiceOver, contentDescription = null) }
                    ) {
                        Text("Ausschuss")
                    }
                    SegmentedButton(
                        selected = activeTab == 2,
                        onClick = { activeTab = 2 },
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3),
                        icon = { Icon(Icons.Filled.History, contentDescription = null) }
                    ) {
                        Text("Historie")
                    }
                }
            }

            when (activeTab) {
                0 -> item {
                    WrittenExamSection(
                        state = writtenState,
                        voiceController = voiceController,
                        onStart = examViewModel::startWrittenExam,
                        onStartMcExam = onStartMcExam,
                        onAnswerChange = examViewModel::updateWrittenAnswer,
                        onSubmit = examViewModel::submitWrittenExam,
                        onReset = examViewModel::resetWrittenExam
                    )
                }

                1 -> item {
                    OralCommitteeSection(
                        state = oralState,
                        voiceController = voiceController,
                        onStart = examViewModel::startOralExam,
                        onTogglePrepTimer = examViewModel::toggleOralPrepTimer,
                        onSendMessage = examViewModel::sendOralMessage,
                        onFinishAndGrade = examViewModel::finishAndGradeOralExam,
                        onReset = examViewModel::resetOralExam
                    )
                }

                else -> {
                    if (examAttempts.isEmpty()) {
                        item {
                            Card(shape = MaterialTheme.shapes.large) {
                                Text(
                                    text = "Noch keine abgeschlossenen Prüfungssimulationen gespeichert. Starte eine schriftliche Situationsaufgabe oder ein Fachgespräch!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(18.dp)
                                )
                            }
                        }
                    } else {
                        items(examAttempts, key = { it.id.ifBlank { it.title + it.achievedPoints } }) { attempt ->
                            ExamHistoryCard(attempt = attempt)
                        }
                    }
                }
            }
        }
    }

    if (showGradeScaleDialog) {
        val scale = listOf(
            Triple("92 – 100 P.", "Note 1 (sehr gut)", EmeraldMastery),
            Triple("81 – 91 P.", "Note 2 (gut)", EmeraldMastery),
            Triple("67 – 80 P.", "Note 3 (befriedigend)", SkyCognitive),
            Triple("50 – 66 P.", "Note 4 (ausreichend)", AmberStreak),
            Triple("30 – 49 P.", "Note 5 (mangelhaft)", ErrorCrimson),
            Triple("0 – 29 P.", "Note 6 (ungenügend)", ErrorCrimson)
        )
        AlertDialog(
            onDismissRequest = { showGradeScaleDialog = false },
            title = { Text("IHK 100-Punkte-Notenschlüssel") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    scale.forEach { (pts, label, color) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = pts,
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.labelLarge,
                                color = color
                            )
                            Text(text = label, style = MaterialTheme.typography.bodyMedium)
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
private fun WrittenExamSection(
    state: WrittenExamState,
    voiceController: VoiceStudyController?,
    onStart: (String, Int) -> Unit,
    onStartMcExam: () -> Unit,
    onAnswerChange: (Int, String) -> Unit,
    onSubmit: () -> Unit,
    onReset: () -> Unit
) {
    var selectedMode by remember { mutableStateOf("HQ_LOGISTIK") }
    var selectedMinutes by remember { mutableIntStateOf(45) }
    var showOperators by remember { mutableStateOf(false) }
    var activeDictationQuestion by remember { mutableStateOf<Int?>(null) }
    var voiceStatus by remember { mutableStateOf<String?>(null) }
    val currentState by rememberUpdatedState(state)
    val isSpeaking by (voiceController?.isSpeaking?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val isListening by (voiceController?.isListening?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val voiceError by (voiceController?.voiceError?.collectAsStateWithLifecycle() ?: remember { mutableStateOf<String?>(null) })

    val writtenDictationTrigger = voiceController?.let { controller ->
        rememberVoiceInputTrigger(
            voiceController = controller,
            onResult = { spoken ->
                val active = currentState as? WrittenExamState.Active
                val qNum = activeDictationQuestion
                if (active != null && qNum != null) {
                    val prev = active.answers[qNum].orEmpty().trimEnd()
                    val combined = if (prev.isBlank()) spoken else "$prev $spoken"
                    onAnswerChange(qNum, combined)
                    voiceStatus = "Diktat in Aufgabe $qNum übernommen"
                }
            },
            onPermissionDenied = { msg -> voiceStatus = msg }
        )
    }

    val examModes = remember {
        listOf(
            Triple("HQ_LOGISTIK", "HQ 1 · Logistikprozesse", "240m Original"),
            Triple("HQ_ORG_KOSTEN", "HQ 2 · Org. & Kosten", "240m Original"),
            Triple("BQ", "BQ · Klausur-Mix", "90m Original"),
            Triple("BQ_RECHT", "BQ 1 · Recht", "90m Original"),
            Triple("BQ_BWL", "BQ 2 · BWL/KLR", "90m Original"),
            Triple("BQ_MIKP", "BQ 3 · MIKP", "90m Original"),
            Triple("BQ_ZIB", "BQ 4 · ZIB", "90m Original"),
            Triple("BQ_NTG", "BQ 5 · NTG", "90m Original")
        )
    }

    when (state) {
        is WrittenExamState.Idle -> {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "IHK-Prüfung nach LogMstrV wählen", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "Realistische Situationsaufgaben (HQ je 240 Min., BQ je 90 Min.) inkl. Operatoren und 100-Punkte-Erwartungshorizont.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            examModes.forEach { (code, label, _) ->
                                FilterChip(
                                    selected = selectedMode == code,
                                    onClick = {
                                        selectedMode = code
                                        selectedMinutes = if (code.startsWith("HQ")) 90 else 45
                                    },
                                    label = { Text(label) }
                                )
                            }
                        }
                        Text("Bearbeitungszeit:", style = MaterialTheme.typography.labelLarge)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val timePresets = if (selectedMode.startsWith("HQ")) {
                                listOf(15 to "15m Kurz", 45 to "45m Kompakt", 90 to "90m Probe", 240 to "240m Original")
                            } else {
                                listOf(15 to "15m Kurz", 45 to "45m Kompakt", 90 to "90m Original")
                            }
                            timePresets.forEach { (mins, label) ->
                                FilterChip(
                                    selected = selectedMinutes == mins,
                                    onClick = { selectedMinutes = mins },
                                    label = { Text(label) }
                                )
                            }
                        }
                        Button(
                            onClick = { onStart(selectedMode, selectedMinutes) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_written_exam_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
                        ) {
                            Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SIHK-Prüfung generieren & starten")
                        }
                    }
                }

                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "BQ/HQ-Prüfungs-Check (25 Fragen · 30 Min.)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Strukturierter Wissencheck über alle Logistikmeister-Handlungsfelder (BQ, HQ & Fachgespräch) ohne Zwischenauflösung mit direkter IHK-Note (1–6).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = onStartMcExam,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("exam_screen_mc_button")
                        ) {
                            Text("BQ/HQ-Prüfungs-Check starten")
                        }
                    }
                }
            }
        }

        is WrittenExamState.Generating, is WrittenExamState.Evaluating -> {
            Card(shape = MaterialTheme.shapes.large) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(color = SignalOrange, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        if (state is WrittenExamState.Generating) "SIHK-Situationsaufgabe wird erstellt…" else "Erwartungshorizont wird nach IHK-Schlüssel bewertet…"
                    )
                }
            }
        }

        is WrittenExamState.Active -> {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    color = if (state.secondsRemaining < 180) ErrorCrimson else MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${state.scenario.examMode} · ${state.scenario.title}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Timer, contentDescription = "Restzeit", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = formatSeconds(state.secondsRemaining),
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                val writtenBanner = when {
                    isListening -> "Diktat aktiv für Aufgabe ${activeDictationQuestion ?: ""}… Bitte jetzt sprechen."
                    !voiceError.isNullOrBlank() -> voiceError
                    !voiceStatus.isNullOrBlank() -> voiceStatus
                    else -> null
                }
                writtenBanner?.let { msg ->
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

                Card(shape = MaterialTheme.shapes.large) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.scenario.companyContext,
                                style = MaterialTheme.typography.labelLarge,
                                color = SkyCognitive,
                                modifier = Modifier.weight(1f)
                            )
                            if (voiceController != null) {
                                IconButton(
                                    onClick = {
                                        if (isSpeaking) {
                                            voiceController.stopSpeaking()
                                        } else {
                                            voiceController.speak(
                                                "${state.scenario.companyContext}. Ausgangssituation: ${state.scenario.ausgangssituation}"
                                            )
                                        }
                                    },
                                    modifier = Modifier.testTag("written_scenario_tts_button")
                                ) {
                                    Icon(
                                        imageVector = if (isSpeaking) Icons.Filled.StopCircle else Icons.Filled.VolumeUp,
                                        contentDescription = if (isSpeaking) "Vorlesen stoppen" else "Ausgangssituation vorlesen",
                                        tint = if (isSpeaking) SignalOrange else SkyCognitive
                                    )
                                }
                            }
                        }
                        Text(
                            text = state.scenario.ausgangssituation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showOperators = !showOperators }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "IHK-Operatoren-Kompass (Tipp für volle Punktzahl)",
                                style = MaterialTheme.typography.labelLarge,
                                color = SkyCognitive
                            )
                            Icon(
                                imageVector = if (showOperators) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = "Operatoren ein-/ausblenden"
                            )
                        }
                        AnimatedVisibility(visible = showOperators) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                ExaminerTipsCatalog.operators.forEach { op ->
                                    Text(
                                        text = "• ${op.operator}: ${op.expectation}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }

                state.scenario.subQuestions.forEach { q ->
                    Card(shape = MaterialTheme.shapes.medium) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Aufgabe ${q.number} (${q.maxPoints} Punkte)",
                                    fontFamily = JetBrainsMonoFontFamily,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = SignalOrange,
                                    modifier = Modifier.weight(1f)
                                )
                                if (voiceController != null) {
                                    IconButton(
                                        onClick = {
                                            if (isSpeaking) {
                                                voiceController.stopSpeaking()
                                            } else {
                                                voiceController.speak("Aufgabe ${q.number}, ${q.maxPoints} Punkte: ${q.prompt}")
                                            }
                                        },
                                        modifier = Modifier.testTag("written_question_tts_${q.number}")
                                    ) {
                                        Icon(
                                            imageVector = if (isSpeaking) Icons.Filled.StopCircle else Icons.Filled.VolumeUp,
                                            contentDescription = "Aufgabe ${q.number} vorlesen",
                                            tint = if (isSpeaking) SignalOrange else SkyCognitive
                                        )
                                    }
                                }
                            }
                            Text(text = q.prompt, style = MaterialTheme.typography.bodyLarge)
                            OutlinedTextField(
                                value = state.answers[q.number].orEmpty(),
                                onValueChange = { onAnswerChange(q.number, it) },
                                label = { Text("Deine Ausarbeitung zu Aufgabe ${q.number}") },
                                trailingIcon = if (writtenDictationTrigger != null) {
                                    {
                                        IconButton(
                                            onClick = {
                                                activeDictationQuestion = q.number
                                                writtenDictationTrigger()
                                            },
                                            modifier = Modifier.testTag("written_dictate_${q.number}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Mic,
                                                contentDescription = "Antwort zu Aufgabe ${q.number} diktieren",
                                                tint = if (isListening && activeDictationQuestion == q.number) {
                                                    ErrorCrimson
                                                } else {
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                                }
                                            )
                                        }
                                    }
                                } else {
                                    null
                                },
                                minLines = 3,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onReset, modifier = Modifier.weight(1f)) {
                        Text("Abbrechen")
                    }
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .weight(2f)
                            .testTag("submit_written_exam_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldMastery)
                    ) {
                        Text("Abgeben & bewerten")
                    }
                }
            }
        }

        is WrittenExamState.Completed -> {
            ExamResultCard(
                title = state.scenario.title,
                result = state.result,
                xpAwarded = state.xpAwarded,
                onDone = onReset
            )
        }

        is WrittenExamState.Error -> {
            Card(shape = MaterialTheme.shapes.large) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = state.message, color = ErrorCrimson, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = onReset) { Text("Zurück") }
                }
            }
        }
    }
}

@Composable
private fun OralCommitteeSection(
    state: OralExamState,
    voiceController: VoiceStudyController?,
    onStart: (String) -> Unit,
    onTogglePrepTimer: () -> Unit,
    onSendMessage: (String) -> Unit,
    onFinishAndGrade: () -> Unit,
    onReset: () -> Unit
) {
    val presetTopics = remember {
        listOf(
            "Einführung von AMR / Shuttle-Systemen & Team-Onboarding im Lager",
            "Schichtkonflikt, Telematik & Mehrarbeit (§ 87 BetrVG) im Warenausgang",
            "Investitionsentscheidung (TCO): Neuer Schmalgangstapler vs. Outsourcing"
        )
    }
    var customTopic by remember { mutableStateOf(presetTopics.first()) }
    var chatInput by remember { mutableStateOf("") }
    val currentInput by rememberUpdatedState(chatInput)
    var voiceFeedback by remember { mutableStateOf<String?>(null) }
    val isSpeaking by (voiceController?.isSpeaking?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val isListening by (voiceController?.isListening?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val voiceError by (voiceController?.voiceError?.collectAsStateWithLifecycle() ?: remember { mutableStateOf<String?>(null) })

    val oralMicTrigger = voiceController?.let { controller ->
        rememberVoiceInputTrigger(
            voiceController = controller,
            onResult = { spoken ->
                val base = currentInput.trimEnd()
                chatInput = if (base.isBlank()) spoken else "$base $spoken"
                voiceFeedback = "Spracheingabe ins Antwortfeld übernommen"
            },
            onPermissionDenied = { msg -> voiceFeedback = msg }
        )
    }

    when (state) {
        is OralExamState.Idle -> {
            Card(shape = MaterialTheme.shapes.large) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(155.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_sihk_committee),
                            contentDescription = "SIHK Prüfungsausschuss",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(listOf(Color.Transparent, Navy950.copy(alpha = 0.86f)))
                                )
                        )
                        Text(
                            text = "KI-Prüfungsausschuss · Mündliches Fachgespräch",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Thema wählen oder eingeben:", style = MaterialTheme.typography.labelLarge)
                        presetTopics.forEach { t ->
                            FilterChip(
                                selected = customTopic == t,
                                onClick = { customTopic = t },
                                label = { Text(t) }
                            )
                        }
                        OutlinedTextField(
                            value = customTopic,
                            onValueChange = { customTopic = it },
                            label = { Text("Thema des Fachgesprächs") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = { onStart(customTopic) },
                            enabled = customTopic.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_oral_exam_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
                        ) {
                            Text("Fachgespräch starten")
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        is OralExamState.Active -> {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("30-Min. Vorbereitungs-Timer", style = MaterialTheme.typography.labelMedium)
                            Text(
                                text = formatSeconds(state.prepSecondsRemaining),
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        OutlinedButton(onClick = onTogglePrepTimer) {
                            Text(if (state.isPrepTimerRunning) "Pause" else "Start")
                        }
                    }
                }

                OralPresentationChecklistCard(topic = state.topic)

                state.messages.forEach { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isCandidate) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (msg.isCandidate) SignalOrange else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 2.dp,
                            modifier = Modifier.widthIn(max = 500.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (msg.isCandidate) "Du (Prüfling)" else "SIHK-Ausschuss",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (msg.isCandidate) Color.White.copy(alpha = 0.85f) else SkyCognitive
                                    )
                                    if (!msg.isCandidate && voiceController != null) {
                                        IconButton(
                                            onClick = {
                                                if (isSpeaking) {
                                                    voiceController.stopSpeaking()
                                                } else {
                                                    voiceController.speak(msg.text)
                                                }
                                            },
                                            modifier = Modifier.testTag("oral_msg_tts_button")
                                        ) {
                                            Icon(
                                                imageVector = if (isSpeaking) Icons.Filled.StopCircle else Icons.Filled.VolumeUp,
                                                contentDescription = if (isSpeaking) "Vorlesen stoppen" else "Ausschussfrage vorlesen",
                                                tint = if (isSpeaking) SignalOrange else SkyCognitive
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (msg.isCandidate) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                state.streamingReply?.let { partial ->
                    if (partial.isNotEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 2.dp
                        ) {
                            Text(
                                text = partial,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                val oralVoiceBanner = when {
                    isListening -> "Höre zu… Bitte sprich deine Präsentation oder Antwort."
                    !voiceError.isNullOrBlank() -> voiceError
                    !voiceFeedback.isNullOrBlank() -> voiceFeedback
                    else -> null
                }
                oralVoiceBanner?.let { msg ->
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = chatInput,
                        onValueChange = { chatInput = it },
                        label = { Text("Deine Präsentation / Antwort…") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("oral_chat_input")
                    )
                    if (oralMicTrigger != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = oralMicTrigger,
                            enabled = !state.isSending,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isListening) ErrorCrimson else SkyCognitive.copy(alpha = 0.14f)
                                )
                                .testTag("oral_mic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "Antwort per Mikrofon einsprechen",
                                tint = if (isListening) Color.White else SkyCognitive
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            onSendMessage(chatInput)
                            chatInput = ""
                            voiceFeedback = null
                        },
                        enabled = !state.isSending && chatInput.isNotBlank(),
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SignalOrange)
                            .testTag("send_oral_message_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Antwort senden",
                            tint = Color.White
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onReset, modifier = Modifier.weight(1f)) {
                        Text("Abbrechen")
                    }
                    Button(
                        onClick = onFinishAndGrade,
                        enabled = state.messages.size >= 2 && !state.isSending,
                        modifier = Modifier
                            .weight(2f)
                            .testTag("grade_oral_exam_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldMastery)
                    ) {
                        Text("Gespräch beenden & benoten")
                    }
                }
            }
        }

        is OralExamState.Evaluating -> {
            Card(shape = MaterialTheme.shapes.large) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(color = EmeraldMastery, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Ausschuss berät über deine Gesamtnote…")
                }
            }
        }

        is OralExamState.Completed -> {
            ExamResultCard(
                title = "Fachgespräch: ${state.topic}",
                result = state.result,
                xpAwarded = state.xpAwarded,
                onDone = onReset
            )
        }

        is OralExamState.Error -> {
            Card(shape = MaterialTheme.shapes.large) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = state.message, color = ErrorCrimson, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = onReset) { Text("Zurück") }
                }
            }
        }
    }
}

@Composable
private fun ExamResultCard(
    title: String,
    result: ExamEvaluationResult,
    xpAwarded: Long,
    onDone: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Surface(
                color = if (result.passed) EmeraldMastery.copy(alpha = 0.14f) else ErrorCrimson.copy(alpha = 0.14f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "${result.totalPoints} / 100 Punkte · IHK-Note ${result.ihkGrade} (${if (result.passed) "Bestanden" else "Nicht bestanden"})",
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (result.passed) EmeraldMastery else ErrorCrimson
                    )
                    Text(
                        text = "+$xpAwarded XP gespeichert",
                        style = MaterialTheme.typography.labelMedium,
                        color = SignalOrange
                    )
                }
            }
            SihkCurriculum.supplementaryOralTarget(result.totalPoints)?.let { neededOral ->
                Surface(
                    color = AmberStreak.copy(alpha = 0.16f),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hinweis Mündliche Ergänzungsprüfung (2:1-Regel): Mit ${result.totalPoints} P. (Note 5) wären in der Ergänzungsprüfung mind. $neededOral P. zum Bestehen nötig.",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            Text(text = result.summary, style = MaterialTheme.typography.bodyMedium)
            result.questionScores.forEach { qs ->
                Text("• $qs", style = MaterialTheme.typography.bodyMedium)
            }
            Text("Stärken: ${result.strengths}", style = MaterialTheme.typography.bodyMedium, color = EmeraldMastery)
            Text("Optimierungspotenzial: ${result.improvements}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
                Text("Fertig")
            }
        }
    }
}

@Composable
private fun ExamHistoryCard(attempt: ExamAttempt) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (attempt.passed) EmeraldMastery.copy(alpha = 0.15f) else ErrorCrimson.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${attempt.examMode} · ${attempt.achievedPoints} P. (Note ${attempt.ihkGrade})",
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (attempt.passed) EmeraldMastery else ErrorCrimson,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "${attempt.durationMinutes} Min.",
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(text = attempt.title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = attempt.feedbackSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OralPresentationChecklistCard(topic: String) {
    var expanded by remember(topic) { mutableStateOf(false) }
    val steps = remember {
        listOf(
            "1. Ausgangslage & Problemstellung präzisieren",
            "2. Zielsetzung nach SMART-Kriterien formulieren",
            "3. Alternativen & KLR / Wirtschaftlichkeit abwägen",
            "4. Empfehlung & konkreter Umsetzungsplan",
            "5. Personal, BetrVG (§ 87) & Arbeitsschutz einbinden"
        )
    }
    val checkedStates = remember(topic) {
        mutableStateListOf(false, false, false, false, false)
    }
    val completedCount = checkedStates.count { it }

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("oral_checklist_card")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Checklist,
                        contentDescription = null,
                        tint = SignalOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "5-Punkte-Präsentations-Checkliste ($completedCount/5)",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Einklappen" else "Ausklappen"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    steps.forEachIndexed { idx, label ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { checkedStates[idx] = !checkedStates[idx] }
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkedStates[idx],
                                onCheckedChange = { checkedStates[idx] = it },
                                colors = CheckboxDefaults.colors(checkedColor = EmeraldMastery)
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatSeconds(totalSeconds: Int): String {
    val safe = totalSeconds.coerceAtLeast(0)
    val mins = safe / 60
    val secs = safe % 60
    return String.format(Locale.GERMANY, "%02d:%02d", mins, secs)
}
