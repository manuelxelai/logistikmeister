package com.example.ui.learn

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.core.QuizVoiceAction
import com.example.core.VoiceStudyController
import com.example.core.rememberVoiceInputTrigger
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SkyCognitive

@Composable
fun FlashcardPlayerView(
    session: LessonSessionState,
    bookmarkedIds: Set<String>,
    onToggleBookmark: (String) -> Unit,
    onFlipCard: () -> Unit,
    onRateCard: (Boolean) -> Unit,
    onClose: () -> Unit,
    voiceController: VoiceStudyController? = null,
    modifier: Modifier = Modifier
) {
    val q = session.currentQuestion
    val isSpeaking by (voiceController?.isSpeaking?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val isListening by (voiceController?.isListening?.collectAsStateWithLifecycle() ?: remember { mutableStateOf(false) })
    val voiceError by (voiceController?.voiceError?.collectAsStateWithLifecycle() ?: remember { mutableStateOf<String?>(null) })
    var voiceFeedback by remember { mutableStateOf<String?>(null) }

    DisposableEffect(voiceController) {
        onDispose {
            voiceController?.stopSpeaking()
            voiceController?.stopListening()
        }
    }

    val voiceTrigger = voiceController?.let { controller ->
        rememberVoiceInputTrigger(
            voiceController = controller,
            onResult = { spoken ->
                voiceFeedback = "Erkannt: „$spoken“"
                when (VoiceStudyController.parseQuizVoiceCommand(spoken, emptyList())) {
                    QuizVoiceAction.FlipOrReveal, QuizVoiceAction.SubmitOrNext -> {
                        if (!session.isFlashcardFlipped) onFlipCard()
                    }
                    QuizVoiceAction.RateKnown -> {
                        if (session.isFlashcardFlipped) onRateCard(true)
                    }
                    QuizVoiceAction.RateUnsure -> {
                        if (session.isFlashcardFlipped) onRateCard(false)
                    }
                    else -> {
                        voiceFeedback = "Befehl „$spoken“ – sage „Aufdecken“, „Gewusst“ oder „Unsicher“."
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
                    modifier = Modifier.testTag("close_flashcard_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück zum Lernpfad"
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = session.title, style = MaterialTheme.typography.titleMedium)
                    if (!session.isFinished && q != null) {
                        Text(
                            text = "Lernkarte ${session.currentIndex + 1} von ${session.questions.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                if (!session.isFinished && q != null) {
                    if (voiceController != null) {
                        IconButton(
                            onClick = {
                                if (isSpeaking) {
                                    voiceController.stopSpeaking()
                                } else {
                                    val textToSpeak = if (session.isFlashcardFlipped) {
                                        "Musterlösung: ${q.options[q.correctIndex]}. Erläuterung: ${q.explanation}"
                                    } else {
                                        "Karteikarte: ${q.prompt}"
                                    }
                                    voiceController.speak(textToSpeak)
                                }
                            },
                            modifier = Modifier.testTag("flashcard_tts_button")
                        ) {
                            Icon(
                                imageVector = if (isSpeaking) Icons.Filled.StopCircle else Icons.Filled.VolumeUp,
                                contentDescription = if (isSpeaking) "Vorlesen stoppen" else "Karte vorlesen",
                                tint = if (isSpeaking) SignalOrange else SkyCognitive
                            )
                        }
                    }
                    if (voiceTrigger != null) {
                        IconButton(
                            onClick = voiceTrigger,
                            modifier = Modifier.testTag("flashcard_mic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Mic,
                                contentDescription = "Sprachbefehl (Aufdecken, Gewusst, Unsicher)",
                                tint = if (isListening) ErrorCrimson else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    val isBookmarked = bookmarkedIds.contains(q.id)
                    IconButton(
                        onClick = { onToggleBookmark(q.id) },
                        modifier = Modifier.testTag("flashcard_bookmark_button")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = if (isBookmarked) "Von Merkliste entfernen" else "Auf Merkliste setzen",
                            tint = if (isBookmarked) SignalOrange else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (session.isFinished || q == null) {
                FlashcardSummaryView(session = session, onClose = onClose)
                return@Column
            }

            val progress = (session.currentIndex + 1).toFloat() / session.questions.size.coerceAtLeast(1).toFloat()
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = SkyCognitive
            )

            val bannerMsg = when {
                isListening -> "Höre zu… Sage z. B. „Aufdecken“, „Gewusst“ oder „Unsicher“"
                !voiceError.isNullOrBlank() -> voiceError
                !voiceFeedback.isNullOrBlank() -> voiceFeedback
                else -> null
            }
            bannerMsg?.let { msg ->
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

            Surface(
                color = SkyCognitive.copy(alpha = 0.14f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "${q.category.displayName} · ${q.contextBanner}",
                    style = MaterialTheme.typography.labelLarge,
                    color = SkyCognitive,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AnimatedContent(
                    targetState = session.isFlashcardFlipped,
                    label = "flashcard_flip"
                ) { flipped ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 240.dp)
                            .clickable(onClick = onFlipCard)
                            .testTag("flashcard_surface"),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = if (flipped) {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = BorderStroke(
                            1.5.dp,
                            if (flipped) EmeraldMastery.copy(alpha = 0.6f) else SkyCognitive.copy(alpha = 0.45f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (flipped) "MUSTERLÖSUNG & BEGRÜNDUNG" else "PRÜFUNGSFRAGE",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (flipped) EmeraldMastery else SkyCognitive
                                )
                                Icon(
                                    imageVector = Icons.Filled.Flip,
                                    contentDescription = "Karte wenden",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            if (!flipped) {
                                Text(
                                    text = q.prompt,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tippe auf die Karte oder sage „Aufdecken“, um die IHK-Antwort anzuzeigen.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Surface(
                                    color = EmeraldMastery.copy(alpha = 0.14f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = q.options[q.correctIndex],
                                        style = MaterialTheme.typography.titleMedium,
                                        color = EmeraldMastery,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                                Text(
                                    text = q.explanation,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            if (!session.isFlashcardFlipped) {
                Button(
                    onClick = onFlipCard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("reveal_flashcard_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SkyCognitive)
                ) {
                    Icon(Icons.Filled.Flip, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Antwort aufdecken")
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onRateCard(false) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("flashcard_unsure_button"),
                        border = BorderStroke(1.dp, ErrorCrimson)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            tint = ErrorCrimson,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Unsicher", color = ErrorCrimson)
                    }
                    Button(
                        onClick = { onRateCard(true) },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("flashcard_known_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldMastery)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gewusst")
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardSummaryView(
    session: LessonSessionState,
    onClose: () -> Unit
) {
    val total = session.questions.size.coerceAtLeast(1)
    val pct = (session.correctCount * 100) / total
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldMastery,
                    modifier = Modifier.size(48.dp)
                )
                Text("Karteikarten-Runde beendet", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "${session.correctCount} von ${session.questions.size} gewusst ($pct %)",
                    style = MaterialTheme.typography.titleMedium,
                    color = EmeraldMastery
                )
                Text(
                    text = "+${session.earnedXp} XP · Im 5-Fächer-Lernplan (Leitner) aktualisiert; unsichere Karten landen in „Falsch gedacht“.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                session.statusMessage?.let { msg ->
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodySmall,
                        color = SignalOrange
                    )
                }
            }
        }
        Button(
            onClick = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Zurück zur Übersicht")
        }
    }
}
