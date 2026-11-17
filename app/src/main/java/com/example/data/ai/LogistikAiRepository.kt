package com.example.data.ai

import android.util.Log
import com.example.data.formulas.LogisticsFormula
import com.example.data.model.SihkCurriculum
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

data class DojoProblem(
    val formulaId: String,
    val title: String,
    val scenario: String,
    val givenValues: List<String>,
    val question: String,
    val expectedNumber: Double,
    val unit: String,
    val hint: String,
    val solutionSteps: List<String>
)

data class DojoEvaluation(
    val isCorrect: Boolean,
    val feedback: String,
    val correctedSteps: List<String>
)

data class ExamSubQuestion(
    val number: Int,
    val prompt: String,
    val maxPoints: Int
)

data class GeneratedExamScenario(
    val examMode: String,
    val title: String,
    val companyContext: String,
    val ausgangssituation: String,
    val subQuestions: List<ExamSubQuestion>
)

data class ExamEvaluationResult(
    val totalPoints: Int,
    val ihkGrade: Int,
    val passed: Boolean,
    val questionScores: List<String>,
    val strengths: String,
    val improvements: String,
    val summary: String
)

class LogistikAiRepository {

    private val generalModel by lazy {
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            systemInstruction = content {
                text(
                    "Du bist ein erfahrener Dozent und IHK/SIHK-Prüfer für Geprüfte Logistikmeister (Schwerpunkt Südwestfalen / IHK Hagen). " +
                        "Antworte präzise, praxisnah, fachlich korrekt auf Deutsch nach aktuell gültigen Rechts-, KLR- und Logistikstandards."
                )
            }
        )
    }

    private val dojoProblemModel by lazy {
        val schema = Schema.obj(
            properties = mapOf(
                "title" to Schema.string("Kurzer Praxistitel der Rechenaufgabe"),
                "scenario" to Schema.string("Realistische Ausgangssituation in einem Industrie- oder Logistikbetrieb"),
                "givenValues" to Schema.array(
                    items = Schema.string(),
                    description = "Liste der gegebenen Kennzahlen mit Einheiten"
                ),
                "question" to Schema.string("Konkrete Rechenfrage mit geforderter Zieleinheit"),
                "expectedNumber" to Schema.double("Exaktes numerisches Endergebnis (auf 2 Nachkommastellen gerundet)"),
                "unit" to Schema.string("Einheit des Endergebnisses (z. B. Stück, Tage, EUR, %, t/h)"),
                "hint" to Schema.string("Kurzer Lösungs-Tipp ohne das Ergebnis zu verraten"),
                "solutionSteps" to Schema.array(
                    items = Schema.string(),
                    description = "Schritt-für-Schritt-Lösungsweg nach IHK-Standard"
                )
            )
        )
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = schema
            }
        )
    }

    private val dojoEvalModel by lazy {
        val schema = Schema.obj(
            properties = mapOf(
                "isCorrect" to Schema.boolean("true wenn Ergebnis innerhalb einer praxisüblichen Rundungstoleranz von +/- 2% liegt"),
                "feedback" to Schema.string("Präzises Prüfer-Feedback zum Rechenweg und Ergebnis auf Deutsch"),
                "correctedSteps" to Schema.array(
                    items = Schema.string(),
                    description = "Korrigierter oder bestätigter Rechenweg in Einzelschritten"
                )
            )
        )
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = schema
            }
        )
    }

    private val examGeneratorModel by lazy {
        val schema = Schema.obj(
            properties = mapOf(
                "title" to Schema.string("Titel der IHK-Situationsaufgabe"),
                "companyContext" to Schema.string("Kurze Betriebsbeschreibung aus der Industrie/Logistik"),
                "ausgangssituation" to Schema.string("Realistische Ausgangssituation mit konkreten Zahlen und Problemstellung (ca. 6-8 Sätze)"),
                "subQuestions" to Schema.array(
                    items = Schema.obj(
                        properties = mapOf(
                            "number" to Schema.integer("Aufgabennummer 1..3"),
                            "prompt" to Schema.string("Handlungsorientierte Prüfungsfrage inkl. Operator (z. B. Berechnen Sie, Erläutern Sie, Beurteilen Sie)"),
                            "maxPoints" to Schema.integer("Maximalpunktzahl, Summe über alle Aufgaben genau 100")
                        )
                    ),
                    description = "Genau 3 aufeinander abgestimmte Teilaufgaben mit insgesamt 100 Punkten"
                )
            )
        )
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = schema
            }
        )
    }

    private val examEvalModel by lazy {
        val schema = Schema.obj(
            properties = mapOf(
                "totalPoints" to Schema.integer("Gesamtpunktzahl 0 bis 100 nach IHK-Schlüssel"),
                "questionScores" to Schema.array(
                    items = Schema.string(),
                    description = "Punkte und Kurzbegründung pro Teilaufgabe, z. B. 'Aufgabe 1 (28/35 P.): ...'"
                ),
                "strengths" to Schema.string("Konkrete Stärken der Ausarbeitung"),
                "improvements" to Schema.string("Fehlende Fachbegriffe, Rechtsnormen oder Rechenschritte laut Erwartungshorizont"),
                "summary" to Schema.string("Kompaktes Gesamturteil im Stil eines SIHK-Prüfers")
            )
        )
        Firebase.ai.generativeModel(
            modelName = MODEL_NAME,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = schema
            }
        )
    }

    suspend fun generateDojoProblem(formula: LogisticsFormula): DojoProblem {
        val prompt = """
            Erstelle eine neue, realistische IHK-Rechenaufgabe für Geprüfte Logistikmeister zur Formel:
            Titel: ${formula.title} (${formula.category})
            Formel: ${formula.formulaNotation}
            Zieleinheit: ${formula.unitHint}
            Wähle praxisnahe, gut rechenbare Zahlen (z. B. aus Maschinenbau, Automotive-Zulieferer oder Zentrallager).
            Achte streng darauf, dass expectedNumber exakt zur Formel und den givenValues passt.
        """.trimIndent()

        val jsonText = executeWithTimeout {
            dojoProblemModel.generateContent(prompt).text
                ?: throw IllegalStateException("KI hat keine Aufgabenstruktur zurückgegeben.")
        }
        val json = JSONObject(jsonText)
        val givenArr = json.getJSONArray("givenValues")
        val stepsArr = json.getJSONArray("solutionSteps")
        return DojoProblem(
            formulaId = formula.id,
            title = json.getString("title"),
            scenario = json.getString("scenario"),
            givenValues = (0 until givenArr.length()).map { givenArr.getString(it) },
            question = json.getString("question"),
            expectedNumber = json.getDouble("expectedNumber"),
            unit = json.getString("unit"),
            hint = json.getString("hint"),
            solutionSteps = (0 until stepsArr.length()).map { stepsArr.getString(it) }
        )
    }

    suspend fun evaluateDojoSolution(
        problem: DojoProblem,
        userNumberInput: String,
        userStepsInput: String
    ): DojoEvaluation {
        val prompt = """
            Prüfe die Schülerlösung für diese Logistikmeister-Rechenaufgabe:
            Aufgabe: ${problem.title}
            Szenario: ${problem.scenario}
            Gegeben: ${problem.givenValues.joinToString("; ")}
            Frage: ${problem.question}
            Erwartetes Ergebnis: ${problem.expectedNumber} ${problem.unit}
            Muster-Rechenweg: ${problem.solutionSteps.joinToString(" | ")}

            Antwort des Lernenden:
            Endergebnis-Eingabe: $userNumberInput
            Notierter Rechenweg: ${userStepsInput.ifBlank { "(kein Rechenweg notiert)" }}
        """.trimIndent()

        val jsonText = executeWithTimeout {
            dojoEvalModel.generateContent(prompt).text
                ?: throw IllegalStateException("KI-Bewertung war leer.")
        }
        val json = JSONObject(jsonText)
        val stepsArr = json.getJSONArray("correctedSteps")
        return DojoEvaluation(
            isCorrect = json.getBoolean("isCorrect"),
            feedback = json.getString("feedback"),
            correctedSteps = (0 until stepsArr.length()).map { stepsArr.getString(it) }
        )
    }

    suspend fun generateSituationsaufgabe(examMode: String): GeneratedExamScenario {
        val focus = when (examMode) {
            "HQ_LOGISTIK" ->
                "HQ 1. schriftliche Situationsaufgabe: Handlungsbereich Logistikprozesse (AMR/FTS, Multi-Shuttle, Kanban/VSM, Green Logistics, Incoterms 2020, Bullwhip-Effekt — integrativ mit KLR & Personal, Stil SIHK Hagen)"
            "HQ_ORG_KOSTEN" ->
                "HQ 2. schriftliche Situationsaufgabe: Betriebliche Organisation & Kostenwesen (Prozesskostenrechnung lmi/lmn, TCO, DIN EN 15635 Regalprüfung, FEM 4.004, FMEA/8D, VDI 2700 Ladungssicherung, ADR/VerpackG)"
            "BQ_RECHT" ->
                "BQ 1 Rechtsbewusstes Handeln (BetrVG § 87 inkl. Telematik, KSchG § 1, NachwG Textform, ArbSchG §§ 5/12, HGB § 377)"
            "BQ_BWL" ->
                "BQ 2 Betriebswirtschaftliches Handeln (BAB, Zuschlagskalkulation, Deckungsbeitrag, Break-Even, ROI, Servicegrad vs. Kapitalbindung, Nutzwertanalyse)"
            "BQ_MIKP" ->
                "BQ 3 Information, Kommunikation & Planung (Netzplantechnik kritischer Pfad, ABC/XYZ-Analyse, ERP vs. WMS, RFID, Ishikawa/Pareto)"
            "BQ_ZIB" ->
                "BQ 4 Zusammenarbeit im Betrieb (Tuckman, Konfliktmoderation, psychische Gefährdungsbeurteilung ArbSchG, BEM § 167 SGB IX, Führungsstile)"
            "BQ_NTG" ->
                "BQ 5 Naturwissenschaftliche & technische Gesetzmäßigkeiten (Ladungssicherung VDI 2700 Reibwert μ, Hebelgesetz Stapler, Hydraulik, Li-Ion vs. Blei-Säure, Energieeffizienz)"
            "BQ" ->
                "Grundlegende Qualifikationen (Praxis-Mix aus Rechtsbewusstem Handeln, BWL/KLR, MIKP, Zusammenarbeit im Betrieb und NTG nach LogMstrV)"
            else ->
                "Handlungsspezifische Qualifikationen (Mix aus Logistikprozessen, Organisation/Kostenwesen und Mitarbeiterführung — Stil SIHK Hagen)"
        }
        val prompt = """
            Generiere eine hochaktuelle, authentische IHK-Prüfungssimulation ($examMode, Prüfungsstand 2025/2026 nach LogMstrV):
            Schwerpunkt: $focus
            Regionale Färbung: Mittelständisches Industrie- oder Logistikunternehmen (z. B. Südwestfalen / Ruhrgebiet).
            Verwende konsequent klassische IHK-Operatoren (z. B. Nennen Sie, Erläutern Sie, Berechnen Sie nachvollziehbar, Beurteilen Sie begründet).
            Erstelle genau 3 aufeinander abgestimmte Teilaufgaben zur Ausgangssituation, deren Maximalpunkte exakt 100 ergeben (z. B. 35, 35, 30).
        """.trimIndent()

        val jsonText = executeWithTimeout {
            examGeneratorModel.generateContent(prompt).text
                ?: throw IllegalStateException("KI konnte keine Situationsaufgabe erzeugen.")
        }
        val json = JSONObject(jsonText)
        val qArr = json.getJSONArray("subQuestions")
        val subQuestions = (0 until qArr.length()).map { i ->
            val obj = qArr.getJSONObject(i)
            ExamSubQuestion(
                number = obj.optInt("number", i + 1),
                prompt = obj.getString("prompt"),
                maxPoints = obj.optInt("maxPoints", 33)
            )
        }
        return GeneratedExamScenario(
            examMode = examMode,
            title = json.getString("title"),
            companyContext = json.getString("companyContext"),
            ausgangssituation = json.getString("ausgangssituation"),
            subQuestions = subQuestions
        )
    }

    suspend fun evaluateExamAnswers(
        scenario: GeneratedExamScenario,
        answers: Map<Int, String>
    ): ExamEvaluationResult {
        val formattedQuestions = scenario.subQuestions.joinToString("\n\n") { q ->
            val ans = answers[q.number]?.trim().orEmpty().ifBlank { "(Keine Antwort abgegeben)" }
            "Aufgabe ${q.number} (max. ${q.maxPoints} Punkte): ${q.prompt}\nAntwort des Prüflings: $ans"
        }
        val prompt = """
            Bewerte diese schriftliche IHK-Situationsaufgabe streng nach offiziellem 100-Punkte-Erwartungshorizont.
            Titel: ${scenario.title}
            Ausgangssituation: ${scenario.ausgangssituation}

            $formattedQuestions
        """.trimIndent()

        val jsonText = executeWithTimeout {
            examEvalModel.generateContent(prompt).text
                ?: throw IllegalStateException("KI-Prüfungsauswertung war leer.")
        }
        val json = JSONObject(jsonText)
        val points = json.getInt("totalPoints").coerceIn(0, 100)
        val scoreArr = json.getJSONArray("questionScores")
        return ExamEvaluationResult(
            totalPoints = points,
            ihkGrade = SihkCurriculum.calculateIhkGrade(points),
            passed = points >= 50,
            questionScores = (0 until scoreArr.length()).map { scoreArr.getString(it) },
            strengths = json.getString("strengths"),
            improvements = json.getString("improvements"),
            summary = json.getString("summary")
        )
    }

    fun streamCommitteeReply(
        topicContext: String,
        chatHistory: List<Content>,
        userInput: String
    ): Flow<String> {
        val systemTurn = content("user") {
            text(
                "Wir simulieren jetzt ein mündliches SIHK-Fachgespräch (Geprüfter Logistikmeister) zum Thema: '$topicContext'. " +
                    "Du verkörperst den dreiköpfigen Prüfungsausschuss (1. Vorsitzender/Logistik, 2. KLR/Organisation, 3. Personal/Arbeitsrecht). " +
                    "Reagiere direkt auf meine Aussagen mit kurzem fachlichen Feedback und stelle maximal 1–2 gezielte, praxisnahe Rückfragen."
            )
        }
        val ackTurn = content("model") {
            text(
                "Guten Tag! Der Prüfungsausschuss der SIHK begrüßt Sie zum Fachgespräch zum Thema '$topicContext'. " +
                    "Bitte stellen Sie zunächst Ihren Lösungsansatz und die wichtigsten Kennzahlen bzw. Führungsschritte vor."
            )
        }
        val chat = generalModel.startChat(
            history = listOf(systemTurn, ackTurn) + chatHistory
        )
        return chat.sendMessageStream(userInput).map { chunk -> chunk.text.orEmpty() }
    }

    suspend fun evaluateOralExam(
        topicContext: String,
        transcript: String
    ): ExamEvaluationResult {
        val prompt = """
            Bewerte dieses mündliche SIHK-Fachgespräch zum Thema "$topicContext" nach dem 100-Punkte-IHK-Schlüssel (Präsentation/Struktur 30 P., Fachkompetenz Logistik & KLR 40 P., Führung & Recht 30 P.):

            Gesprächsprotokoll:
            $transcript
        """.trimIndent()

        val jsonText = executeWithTimeout {
            examEvalModel.generateContent(prompt).text
                ?: throw IllegalStateException("KI-Bewertung des Fachgesprächs war leer.")
        }
        val json = JSONObject(jsonText)
        val points = json.getInt("totalPoints").coerceIn(0, 100)
        val scoreArr = json.getJSONArray("questionScores")
        return ExamEvaluationResult(
            totalPoints = points,
            ihkGrade = SihkCurriculum.calculateIhkGrade(points),
            passed = points >= 50,
            questionScores = (0 until scoreArr.length()).map { scoreArr.getString(it) },
            strengths = json.getString("strengths"),
            improvements = json.getString("improvements"),
            summary = json.getString("summary")
        )
    }

    private suspend fun <T> executeWithTimeout(block: suspend () -> T): T {
        return try {
            withTimeout(AI_TIMEOUT_MS) { block() }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Firebase AI call timed out after ${AI_TIMEOUT_MS}ms", e)
            throw IllegalStateException("Die KI-Antwort hat zu lange gedauert (Timeout nach 60s). Bitte erneut versuchen.", e)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Firebase AI call failed", e)
            throw e
        }
    }

    private companion object {
        const val TAG = "LogistikAiRepo"
        const val MODEL_NAME = "gemini-3.8-flash"
        const val AI_TIMEOUT_MS = 60_000L
    }
}
