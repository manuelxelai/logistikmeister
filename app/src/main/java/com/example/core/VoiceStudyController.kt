package com.example.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

sealed interface QuizVoiceAction {
    data class SelectOption(val index: Int) : QuizVoiceAction
    data object FlipOrReveal : QuizVoiceAction
    data object RateKnown : QuizVoiceAction
    data object RateUnsure : QuizVoiceAction
    data object SubmitOrNext : QuizVoiceAction
}

class VoiceStudyController(context: Context) {

    private val appContext = context.applicationContext

    private var tts: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var activeSttCallback: ((String) -> Unit)? = null

    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _partialTranscript = MutableStateFlow("")
    val partialTranscript: StateFlow<String> = _partialTranscript.asStateFlow()

    private val _voiceError = MutableStateFlow<String?>(null)
    val voiceError: StateFlow<String?> = _voiceError.asStateFlow()

    val isSpeechRecognitionAvailable: Boolean
        get() = runCatching { SpeechRecognizer.isRecognitionAvailable(appContext) }.getOrDefault(false)

    init {
        runCatching {
            tts = TextToSpeech(appContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val langResult = tts?.setLanguage(Locale.GERMANY)
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.setLanguage(Locale.GERMAN)
                    }
                    tts?.setSpeechRate(1.0f)
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            _isSpeaking.value = true
                        }

                        override fun onDone(utteranceId: String?) {
                            _isSpeaking.value = false
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            _isSpeaking.value = false
                        }

                        override fun onError(utteranceId: String?, errorCode: Int) {
                            _isSpeaking.value = false
                        }
                    })
                    _isTtsReady.value = true
                }
            }
        }
    }

    fun speak(text: String) {
        val clean = text.trim()
        if (clean.isBlank()) return
        _voiceError.value = null
        val engine = tts
        if (engine == null || !_isTtsReady.value) {
            _voiceError.value = "Vorlesefunktion (TTS) wird noch initialisiert oder ist auf diesem Gerät nicht verfügbar."
            return
        }
        _isSpeaking.value = true
        engine.speak(clean, TextToSpeech.QUEUE_FLUSH, null, "xels_utterance_${System.currentTimeMillis()}")
    }

    fun stopSpeaking() {
        runCatching { tts?.stop() }
        _isSpeaking.value = false
    }

    fun startListening(onFinalResult: (String) -> Unit) {
        _voiceError.value = null
        _partialTranscript.value = ""
        if (!isSpeechRecognitionAvailable) {
            _voiceError.value = "Auf diesem Gerät ist kein Spracherkennungsdienst verfügbar."
            return
        }

        stopSpeaking()
        activeSttCallback = onFinalResult

        runCatching {
            if (speechRecognizer == null) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(appContext).apply {
                    setRecognitionListener(createListener())
                }
            }
            val intent = createRecognizerIntent()
            _isListening.value = true
            speechRecognizer?.startListening(intent)
        }.onFailure { err ->
            _isListening.value = false
            _voiceError.value = err.localizedMessage ?: "Spracheingabe konnte nicht gestartet werden."
        }
    }

    fun stopListening() {
        runCatching { speechRecognizer?.stopListening() }
        _isListening.value = false
    }

    fun createRecognizerIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "de-DE")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Jetzt sprechen (Deutsch)…")
        }
    }

    fun clearError() {
        _voiceError.value = null
    }

    fun release() {
        runCatching { tts?.stop() }
        runCatching { tts?.shutdown() }
        tts = null
        runCatching { speechRecognizer?.cancel() }
        runCatching { speechRecognizer?.destroy() }
        speechRecognizer = null
        _isSpeaking.value = false
        _isListening.value = false
    }

    private fun createListener(): RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _isListening.value = true
        }

        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            _isListening.value = false
        }

        override fun onError(error: Int) {
            _isListening.value = false
            _voiceError.value = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Mikrofonaufnahme fehlgeschlagen."
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Mikrofonberechtigung fehlt."
                SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Netzwerkfehler bei Spracherkennung."
                SpeechRecognizer.ERROR_NO_MATCH -> "Keine Sprache erkannt – bitte deutlicher wiederholen."
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Zeitüberschreitung bei der Spracheingabe."
                else -> "Spracherkennung konnte nicht abgeschlossen werden (Code $error)."
            }
        }

        override fun onResults(results: Bundle?) {
            _isListening.value = false
            val best = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                ?.trim()
                .orEmpty()
            _partialTranscript.value = best
            if (best.isNotBlank()) {
                activeSttCallback?.invoke(best)
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val partial = partialResults
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                ?.trim()
                .orEmpty()
            if (partial.isNotBlank()) {
                _partialTranscript.value = partial
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    companion object {
        fun parseQuizVoiceCommand(spokenText: String, options: List<String>): QuizVoiceAction? {
            val normalized = spokenText.lowercase(Locale.GERMANY).trim()
            if (normalized.isBlank()) return null

            if (normalized.contains("aufdecken") || normalized.contains("umdrehen") || normalized.contains("lösung zeigen")) {
                return QuizVoiceAction.FlipOrReveal
            }
            if (normalized.contains("unsicher") || normalized.contains("nicht gewusst") || normalized.contains("wiederholen") || normalized.contains("nochmal")) {
                return QuizVoiceAction.RateUnsure
            }
            if (normalized.contains("gewusst") || Regex("""\bsicher\b""").containsMatchIn(normalized)) {
                return QuizVoiceAction.RateKnown
            }
            if (normalized == "weiter" || normalized == "nächste" || normalized == "nächste frage" || normalized == "prüfen" || normalized == "antwort prüfen") {
                return QuizVoiceAction.SubmitOrNext
            }

            if (options.isNotEmpty()) {
                val explicitTokens = mapOf(
                    0 to listOf("antwort a", "option a", "buchstabe a", "eins", "1", "erste", "erstes", "wahr", "richtig", "stimmt"),
                    1 to listOf("antwort b", "option b", "buchstabe b", "zwei", "2", "zweite", "zweites", "falsch", "stimmt nicht"),
                    2 to listOf("antwort c", "option c", "buchstabe c", "drei", "3", "dritte", "drittes"),
                    3 to listOf("antwort d", "option d", "buchstabe d", "vier", "4", "vierte", "viertes")
                )
                if (normalized == "a") return QuizVoiceAction.SelectOption(0)
                if (normalized == "b" && options.size > 1) return QuizVoiceAction.SelectOption(1)
                if (normalized == "c" && options.size > 2) return QuizVoiceAction.SelectOption(2)
                if (normalized == "d" && options.size > 3) return QuizVoiceAction.SelectOption(3)

                for ((idx, phrases) in explicitTokens) {
                    if (idx < options.size && phrases.any { normalized == it || normalized.contains(it) }) {
                        // Avoid "wahr"/"falsch" false positives on regular 3-option MC questions unless option actually says Wahr/Falsch
                        val isBooleanWord = normalized == "wahr" || normalized == "falsch" || normalized == "richtig" || normalized == "stimmt" || normalized == "stimmt nicht"
                        if (isBooleanWord && options.size > 2) continue
                        return QuizVoiceAction.SelectOption(idx)
                    }
                }

                // Fuzzy word overlap against option texts
                val spokenWords = normalized.split(Regex("[^a-zäöüß0-9]+")).filter { it.length >= 4 }.toSet()
                if (spokenWords.isNotEmpty()) {
                    var bestIdx = -1
                    var bestHits = 0
                    options.forEachIndexed { idx, opt ->
                        val optWords = opt.lowercase(Locale.GERMANY).split(Regex("[^a-zäöüß0-9]+")).filter { it.length >= 4 }.toSet()
                        val hits = spokenWords.intersect(optWords).size
                        if (hits > bestHits) {
                            bestHits = hits
                            bestIdx = idx
                        }
                    }
                    if (bestIdx >= 0 && bestHits >= 1) {
                        return QuizVoiceAction.SelectOption(bestIdx)
                    }
                }
            }
            return null
        }
    }
}
