package com.example.data.model

enum class DrillMode(val label: String) {
    PRAXIS_DIALOG("Praxis-Dialog"),
    KENNZAHLEN_SPRINT("Kennzahlen-Sprint"),
    PRUEFUNGS_CHECK("IHK-Check")
}

enum class ExamSection(val code: String, val title: String) {
    BQ("BQ", "Grundlegende Qualifikationen"),
    HQ("HQ", "Handlungsspezifische Qualifikationen"),
    SA("SA", "Situationsaufgabe & Fachgespräch");

    companion object {
        fun fromCode(code: String): ExamSection {
            return entries.firstOrNull { it.code.equals(code.trim(), ignoreCase = true) } ?: BQ
        }
    }
}

enum class QuestionDifficulty(val label: String, val weight: Int) {
    GRUNDLAGEN("Grundlagen", 1),
    PRAXIS("Praxistransfer", 2),
    PRUEFUNG("IHK-Prüfungsniveau", 3);

    companion object {
        fun fromMode(mode: DrillMode): QuestionDifficulty = when (mode) {
            DrillMode.KENNZAHLEN_SPRINT -> GRUNDLAGEN
            DrillMode.PRAXIS_DIALOG -> PRAXIS
            DrillMode.PRUEFUNGS_CHECK -> PRUEFUNG
        }
    }
}

enum class QuestionCategory(
    val id: String,
    val displayName: String,
    val shortLabel: String,
    val badgeCode: String,
    val section: ExamSection,
    val topicIds: List<String>,
    val description: String
) {
    LOGISTIKPROZESSE(
        id = "logistikprozesse",
        displayName = "Logistikprozesse",
        shortLabel = "Logistikprozesse",
        badgeCode = "HQ 1",
        section = ExamSection.HQ,
        topicIds = listOf("hq_logistik"),
        description = "JIT/JIS, Kanban, Wertstromanalyse, Intralogistik, Kommissionierung & Incoterms"
    ),
    FUEHRUNG_UND_ZUSAMMENARBEIT(
        id = "fuehrung_und_zusammenarbeit",
        displayName = "Führung und Zusammenarbeit",
        shortLabel = "Führung & Team",
        badgeCode = "BQ 4 · HQ 3",
        section = ExamSection.HQ,
        topicIds = listOf("bq_zib", "hq_fuehrung"),
        description = "Mitarbeiterführung, Gruppendynamik, Konfliktmanagement, Unterweisung & Personalentwicklung"
    ),
    BETRIEBSWIRTSCHAFTLICHES_HANDELN(
        id = "betriebswirtschaftliches_handeln",
        displayName = "Betriebswirtschaftliches Handeln",
        shortLabel = "BWL & KLR",
        badgeCode = "BQ 2",
        section = ExamSection.BQ,
        topicIds = listOf("bq_bwl"),
        description = "Kosten- und Leistungsrechnung, BAB, Deckungsbeitrag, Lagerkennzahlen & Investition"
    ),
    ORGANISATION_UND_KOSTENWESEN(
        id = "organisation_und_kostenwesen",
        displayName = "Betriebliche Organisation & Kostenwesen",
        shortLabel = "Org. & Kosten",
        badgeCode = "HQ 2",
        section = ExamSection.HQ,
        topicIds = listOf("hq_org_kosten"),
        description = "Prozesskosten, Make-or-Buy, FMEA, Regalprüfung (DIN EN 15635) & PDCA"
    ),
    RECHTSBEWUSSTES_HANDELN(
        id = "rechtsbewusstes_handeln",
        displayName = "Rechtsbewusstes Handeln",
        shortLabel = "Recht & BetrVG",
        badgeCode = "BQ 1",
        section = ExamSection.BQ,
        topicIds = listOf("bq_recht"),
        description = "Arbeitsrecht, BetrVG (§§ 87, 102), HGB, ArbSchG & Arbeitsschutz"
    ),
    INFORMATION_KOMMUNIKATION_PLANUNG(
        id = "information_kommunikation_planung",
        displayName = "Information, Kommunikation & Planung",
        shortLabel = "MIKP & Planung",
        badgeCode = "BQ 3",
        section = ExamSection.BQ,
        topicIds = listOf("bq_mikp"),
        description = "Netzplantechnik, ABC/XYZ-Analyse, Ishikawa, Pareto & Statistik"
    ),
    NATURWISSENSCHAFT_TECHNIK(
        id = "naturwissenschaft_technik",
        displayName = "Naturwissenschaftliche & techn. Grundlagen",
        shortLabel = "NTG & Technik",
        badgeCode = "BQ 5",
        section = ExamSection.BQ,
        topicIds = listOf("bq_ntg"),
        description = "Ladungssicherung (VDI 2700), Mechanik, Elektrotechnik & Gefahrstofflagerung"
    ),
    FACHGESPRAECH_PRAXISFALL(
        id = "fachgespraech_praxisfall",
        displayName = "Situationsaufgabe & Fachgespräch",
        shortLabel = "Fachgespräch",
        badgeCode = "SIHK",
        section = ExamSection.SA,
        topicIds = listOf("sa_fachgespraech"),
        description = "30-Min.-Vorbereitung, Präsentationsgliederung & SIHK-Prüfungsausschuss"
    );

    companion object {
        fun fromTopicId(topicId: String): QuestionCategory {
            return entries.firstOrNull { topicId in it.topicIds }
                ?: entries.firstOrNull { it.id.equals(topicId, ignoreCase = true) }
                ?: LOGISTIKPROZESSE
        }
    }
}

data class ExamQuestion(
    val id: String,
    val topicId: String,
    val moduleCode: String,
    val mode: DrillMode,
    val contextBanner: String,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val xpValue: Long = 25L,
    val category: QuestionCategory = QuestionCategory.fromTopicId(topicId),
    val section: ExamSection = ExamSection.fromCode(moduleCode),
    val difficulty: QuestionDifficulty = QuestionDifficulty.fromMode(mode)
) {
    fun matchesCategory(target: QuestionCategory): Boolean {
        return category == target || topicId in target.topicIds
    }
}

data class CategoryBreakdown(
    val category: QuestionCategory,
    val correct: Int,
    val total: Int
)
