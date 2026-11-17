package com.example.core

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun rememberVoiceInputTrigger(
    voiceController: VoiceStudyController,
    onResult: (String) -> Unit,
    onPermissionDenied: (String) -> Unit = {}
): () -> Unit {
    val context = LocalContext.current
    var pendingCallback by remember { mutableStateOf<(String) -> Unit>({}) }
    pendingCallback = onResult

    val fallbackRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val spoken = activityResult.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
                ?.trim()
                .orEmpty()
            if (spoken.isNotBlank()) {
                pendingCallback(spoken)
            }
        }
    }

    val startSpeechOrFallback: () -> Unit = {
        if (voiceController.isSpeechRecognitionAvailable) {
            voiceController.startListening { recognized ->
                pendingCallback(recognized)
            }
        } else {
            runCatching {
                fallbackRecognizerLauncher.launch(voiceController.createRecognizerIntent())
            }.onFailure {
                onPermissionDenied("Auf diesem Gerät steht aktuell kein Sprachdienst zur Verfügung.")
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startSpeechOrFallback()
        } else {
            onPermissionDenied("Mikrofon-Berechtigung verweigert. Du kannst sie in den App-Einstellungen erlauben.")
        }
    }

    return remember(context, voiceController) {
        {
            if (voiceController.isListening.value) {
                voiceController.stopListening()
            } else {
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                if (hasPermission) {
                    startSpeechOrFallback()
                } else {
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        }
    }
}
