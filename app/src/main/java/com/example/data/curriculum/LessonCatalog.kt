package com.example.data.curriculum

import com.example.data.model.ExamQuestion
import com.example.data.model.QuestionCategory

typealias DrillMode = com.example.data.model.DrillMode
typealias LearningDrillQuestion = ExamQuestion

object LessonCatalog {

    val questions: List<ExamQuestion> =
        BqExamCatalogBank.questions +
            HqExamCatalogBank.questions +
            Logistik2026UpdateBank.questions +
            QuizAcademySyncBank.questions +
            DeinePruefungSyncBank.questions

    fun questionsForTopic(topicId: String): List<ExamQuestion> {
        return questions.filter { it.topicId == topicId }
    }

    fun questionsForCategory(category: QuestionCategory): List<ExamQuestion> {
        return questions.filter { it.matchesCategory(category) }
    }

    fun categoryQuestionCounts(): Map<QuestionCategory, Int> {
        return QuestionCategory.entries.associateWith { cat ->
            questionsForCategory(cat).size
        }
    }

    fun buildQuickTestQuestions(
        count: Int = 10,
        moduleCodeFilter: String? = null,
        categoryFilter: QuestionCategory? = null
    ): List<ExamQuestion> {
        val pool = when {
            categoryFilter != null -> questionsForCategory(categoryFilter)
            moduleCodeFilter.isNullOrBlank() || moduleCodeFilter == "ALLE" -> questions
            else -> questions.filter { it.moduleCode == moduleCodeFilter }
        }
        return pool.shuffled().take(count.coerceAtLeast(1))
    }

    fun buildMcExamQuestions(
        count: Int = 25,
        moduleCodeFilter: String? = null,
        topicIdFilter: String? = null
    ): List<ExamQuestion> {
        val pool = when {
            !topicIdFilter.isNullOrBlank() -> questionsForTopic(topicIdFilter)
            moduleCodeFilter.isNullOrBlank() || moduleCodeFilter == "ALLE" -> questions
            else -> questions.filter { it.moduleCode == moduleCodeFilter }
        }.ifEmpty { questions }
        return pool.shuffled().take(count.coerceIn(1, pool.size))
    }

    fun findByKey(questionKey: String): ExamQuestion? {
        return questions.find { it.id == questionKey }
    }
}

data class ExaminerTip(
    val id: String,
    val badge: String,
    val title: String,
    val summary: String,
    val bulletPoints: List<String>
)

data class IhkOperatorEntry(
    val operator: String,
    val expectation: String,
    val tip: String
)

data class KeyNormEntry(
    val norm: String,
    val scope: String
)

object ExaminerTipsCatalog {
    val operators: List<IhkOperatorEntry> = listOf(
        IhkOperatorEntry(
            operator = "Nennen / Aufzählen",
            expectation = "Kurze, präzise Stichpunkte ohne lange Erklärung.",
            tip = "Nicht mehr Punkte nennen als gefordert (oft werden nur die ersten X bewertet!)."
        ),
        IhkOperatorEntry(
            operator = "Beschreiben / Darstellen",
            expectation = "Ablauf, Merkmale oder Sachverhalt strukturiert im Zusammenhang schildern.",
            tip = "Immer auf die konkrete Lagersituation (Modellbetrieb) beziehen."
        ),
        IhkOperatorEntry(
            operator = "Erläutern / Begründen",
            expectation = "Zusammenhang verständlich mit Ursache-Wirkungs-Kette und Praxisbeispiel erklären.",
            tip = "Konsequenzen für Kosten, Durchlaufzeit oder Sicherheit klar benennen."
        ),
        IhkOperatorEntry(
            operator = "Berechnen / Ermitteln",
            expectation = "Formel, eingesetzte Werte mit Einheiten, Ergebnis und kurzer Interpretation.",
            tip = "Nie nur eine Zahl hinschreiben — der Lösungsansatz bringt Teilpunkte!"
        ),
        IhkOperatorEntry(
            operator = "Beurteilen / Entscheiden",
            expectation = "Pro & Contra mit Kriterien abwägen und ein klares fachliches Fazit treffen.",
            tip = "Wirtschaftlichkeit (KLR), Technik/Sicherheit und Personal (BetrVG) verknüpfen."
        )
    )

    val keyNorms: List<KeyNormEntry> = listOf(
        KeyNormEntry("BetrVG § 87 Abs. 1", "Mitbestimmung: Arbeitszeit (Nr. 3), Überwachung/Telematik (Nr. 6), Arbeitsschutz (Nr. 7)"),
        KeyNormEntry("ArbSchG §§ 3, 5, 12", "Grundpflichten, Gefährdungsbeurteilung (inkl. psych. Belastung), jährl. Unterweisung"),
        KeyNormEntry("DIN EN 15635", "Regalinspektion: wöchentliche Sichtkontrolle + jährl. Experte (Grün / Orange / Rot)"),
        KeyNormEntry("VDI 2700 / EN 12195", "Ladungssicherung: 0,8 × F_G nach vorn, 0,5 × F_G seitlich/hinten; Reibwert μ"),
        KeyNormEntry("DGUV V68 / FEM 4.004", "Jährliche Prüfung kraftbetriebener Flurförderzeuge + tägliche Einsatzkontrolle"),
        KeyNormEntry("ADR 1.1.3.6 / TRGS 510", "1.000-Punkte-Regel Gefahrguttransport & Zusammenlagerungsverbote im Lager")
    )

    val tips: List<ExaminerTip> = listOf(
        ExaminerTip(
            id = "tip_bq",
            badge = "BQ · 90m",
            title = "Klausurtaktik für die Grundlegenden Qualifikationen",
            summary = "In den fünf BQ-Fächern (je 90 Min.) entscheidet Zeitmanagement kombiniert mit sauberem Formel- und Gesetzesbezug.",
            bulletPoints = listOf(
                "Punkte-zu-Minuten-Regel: Bei 90 Minuten und 100 Punkten niemals länger als ca. 0,85 Min. pro Punkt festhängen.",
                "Operatoren genau lesen: 'Nennen' = Stichpunkte; 'Erläutern/Begründen' = Zusammenhang + Praxisbeispiel aus dem Lager!",
                "Immer Rechtsgrundlagen (z. B. § 87 BetrVG, § 5 ArbSchG, § 102 BetrVG, § 49 HGB, NachwG) konkret benennen."
            )
        ),
        ExaminerTip(
            id = "tip_hq",
            badge = "HQ · 240m",
            title = "Handlungsspezifische Situationsaufgaben meistern",
            summary = "Zwei schriftliche Klausuren à 240 Min. — beziehen Sie jede Lösung ausdrücklich auf die Ausgangssituation des Modellbetriebs.",
            bulletPoints = listOf(
                "Allgemeine Lehrbuch-Antworten ohne Bezug zu den angegebenen Betriebskennzahlen kosten in HQ wertvolle Punkte.",
                "Bei Logistik-Rechnungen immer: Formel -> eingesetzte Werte mit Einheiten -> Ergebnis -> logistische Interpretation.",
                "Bei Maßnahmen stets Arbeitssicherheit (DIN EN 15635, DGUV), Qualität (FMEA, PDCA) und Wirtschaftlichkeit (TCO, KLR) verknüpfen."
            )
        ),
        ExaminerTip(
            id = "tip_ergaenzung",
            badge = "30–49 P.",
            title = "Mündliche Ergänzungsprüfung (2:1-Rettungsanker)",
            summary = "Bei Note 5 (30–49 Punkte) in genau einem BQ-Fach oder einer schriftl. HQ-Situationsaufgabe besteht die Chance zur Ergänzungsprüfung.",
            bulletPoints = listOf(
                "Formel nach LogMstrV: (2 × schriftl. Punkte + 1 × mündl. Punkte) / 3 ≥ 50 Punkte.",
                "Beispiel: Mit 40 Punkten schriftlich genügen 70 Punkte in der ca. 15-minütigen Ergänzungsprüfung.",
                "Voraussetzung: Schriftlich dürfen keine < 30 Punkte (Note 6) vorliegen und max. ein Fach betroffen sein."
            )
        ),
        ExaminerTip(
            id = "tip_prep",
            badge = "30 Min.",
            title = "30-Minuten-Vorbereitung fürs Fachgespräch",
            summary = "Nutzen Sie die Vorbereitungszeit für ein klares, lesbares Visualisierungskonzept.",
            bulletPoints = listOf(
                "Maximal 2–3 übersichtliche Flipchart- oder Moderationskarten-Blätter erstellen (große Schrift, max. 6 Stichpunkte je Blatt).",
                "Roter Faden: 1. Ausgangslage -> 2. SMART-Ziel -> 3. Alternativen & KLR -> 4. Empfehlung -> 5. Personal/BetrVG/Arbeitsschutz.",
                "Die letzten 3 Minuten ausschließlich für einen ruhigen Gedankendurchlauf des Einstiegs nutzen."
            )
        ),
        ExaminerTip(
            id = "tip_oral",
            badge = "Ausschuss",
            title = "Präsentation & Fachgespräch beim SIHK-Ausschuss",
            summary = "Der Prüfungsausschuss sucht eine verantwortungsbewusste Führungskraft für die Logistik auf Augenhöhe.",
            bulletPoints = listOf(
                "Blickkontakt zu allen Ausschussmitgliedern halten — auch zu Arbeitnehmer- und Arbeitgebervertretern.",
                "Bei kritischen Rückfragen erst ruhig durchatmen, Frage kurz aufgreifen und strukturiert aus Logistikmeister-Sicht begründen.",
                "Bei Rechen-Nachfragen Einheiten (Stunden vs. Sekunden, kN vs. daN, 360 Zinstage) laut mitdenken."
            )
        )
    )
}
