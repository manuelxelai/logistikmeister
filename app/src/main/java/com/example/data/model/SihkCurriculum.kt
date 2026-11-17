package com.example.data.model

data class CurriculumModule(
    val id: String,
    val code: String,
    val title: String,
    val subtitle: String,
    val topics: List<CurriculumTopic>
)

data class CurriculumTopic(
    val id: String,
    val moduleCode: String,
    val numberLabel: String,
    val title: String,
    val summary: String,
    val keyConcepts: List<String>,
    val lessonIds: List<String>,
    val examDurationLabel: String = "90 Min. IHK-Klausur"
)

object SihkCurriculum {
    val modules: List<CurriculumModule> = listOf(
        CurriculumModule(
            id = "mod_bq",
            code = "BQ",
            title = "Grundlegende Qualifikationen",
            subtitle = "Fünf Prüfungsfächer nach LogMstrV (je 90 Min. Klausur · Stand 2025/2026)",
            topics = listOf(
                CurriculumTopic(
                    id = "bq_recht",
                    moduleCode = "BQ",
                    numberLabel = "BQ 1",
                    title = "Rechtsbewusstes Handeln",
                    summary = "Arbeitsrecht, BetrVG (§ 87 Telematik), NachwG-Textform, HGB, ArbSchG & Umweltrecht",
                    keyConcepts = listOf("BetrVG § 87 (inkl. Telematik)", "NachwG / KSchG & ArbZG", "ArbSchG & KrWG"),
                    lessonIds = listOf("bq_recht_01", "bq_recht_02"),
                    examDurationLabel = "90 Min. Klausur"
                ),
                CurriculumTopic(
                    id = "bq_bwl",
                    moduleCode = "BQ",
                    numberLabel = "BQ 2",
                    title = "Betriebswirtschaftliches Handeln",
                    summary = "KLR, BAB, Servicegrad vs. Kapitalbindung, Nutzwertanalyse & Investition",
                    keyConcepts = listOf("BAB & Break-Even", "Servicegrad & Bestand", "ROI & Nutzwertanalyse"),
                    lessonIds = listOf("bq_bwl_01", "bq_bwl_02"),
                    examDurationLabel = "90 Min. Klausur"
                ),
                CurriculumTopic(
                    id = "bq_mikp",
                    moduleCode = "BQ",
                    numberLabel = "BQ 3",
                    title = "Methoden der Information, Kommunikation & Planung",
                    summary = "Netzplantechnik, ABC/XYZ, ERP/WMS, RFID/IoT, Ishikawa & Streuungsmaße",
                    keyConcepts = listOf("Netzplan & ABC/XYZ", "ERP vs. WMS & RFID", "Ishikawa & Pareto"),
                    lessonIds = listOf("bq_mikp_01", "bq_mikp_02"),
                    examDurationLabel = "90 Min. Klausur"
                ),
                CurriculumTopic(
                    id = "bq_zib",
                    moduleCode = "BQ",
                    numberLabel = "BQ 4",
                    title = "Zusammenarbeit im Betrieb",
                    summary = "Mitarbeiterführung, Konfliktlösung, psychische Gefährdungsbeurteilung & BEM",
                    keyConcepts = listOf("Tuckman & Schulz v. Thun", "Psych. Belastung ArbSchG", "BEM § 167 SGB IX"),
                    lessonIds = listOf("bq_zib_01", "bq_zib_02"),
                    examDurationLabel = "90 Min. Klausur"
                ),
                CurriculumTopic(
                    id = "bq_ntg",
                    moduleCode = "BQ",
                    numberLabel = "BQ 5",
                    title = "Naturwissenschaftliche & technische Gesetzmäßigkeiten",
                    summary = "VDI 2700 Ladungssicherung, Hebelgesetz, Hydraulik, Li-Ion vs. Blei-Säure & Energie",
                    keyConcepts = listOf("VDI 2700 & Reibung", "Li-Ion-Batterien & Energie", "TRGS 510 & Hydraulik"),
                    lessonIds = listOf("bq_ntg_01", "bq_ntg_02"),
                    examDurationLabel = "90 Min. Klausur"
                )
            )
        ),
        CurriculumModule(
            id = "mod_hq",
            code = "HQ",
            title = "Handlungsspezifische Qualifikationen",
            subtitle = "Zwei schriftliche Situationsaufgaben (je 240 Min.) & mündliches Fachgespräch",
            topics = listOf(
                CurriculumTopic(
                    id = "hq_logistik",
                    moduleCode = "HQ",
                    numberLabel = "HQ 1",
                    title = "Handlungsbereich Logistikprozesse",
                    summary = "AMR/FTS, Multi-Shuttle, Kanban, Cross-Docking, Green Logistics, Bullwhip & Incoterms",
                    keyConcepts = listOf("AMR, FTS & Multi-Shuttle", "Kanban, VSM & Bullwhip", "Green Logistics & Incoterms"),
                    lessonIds = listOf("hq_logistik_01", "hq_logistik_02"),
                    examDurationLabel = "240 Min. Situationsaufgabe 1"
                ),
                CurriculumTopic(
                    id = "hq_org_kosten",
                    moduleCode = "HQ",
                    numberLabel = "HQ 2",
                    title = "Betriebliche Organisation & Kostenwesen",
                    summary = "Prozesskosten, TCO, DIN EN 15635 Regalprüfung, FEM 4.004, VerpackG, ADR & FMEA",
                    keyConcepts = listOf("DIN EN 15635 & FEM 4.004", "Prozesskosten & TCO", "FMEA, 8D & ADR 1.1.3.6"),
                    lessonIds = listOf("hq_org_01", "hq_org_02"),
                    examDurationLabel = "240 Min. Situationsaufgabe 2"
                ),
                CurriculumTopic(
                    id = "hq_fuehrung",
                    moduleCode = "HQ",
                    numberLabel = "HQ 3",
                    title = "Führung und Personal",
                    summary = "Situatives Führen, Onboarding, § 12 ArbSchG Unterweisung, Abmahnung & Change-Management",
                    keyConcepts = listOf("Onboarding & 4-Stufen", "§ 12 ArbSchG & Abmahnung", "SMART & Beurteilung"),
                    lessonIds = listOf("hq_fuehrung_01", "hq_fuehrung_02"),
                    examDurationLabel = "30m Vorb. + 30m Fachgespräch"
                )
            )
        ),
        CurriculumModule(
            id = "mod_sa",
            code = "SA",
            title = "Situationsaufgabe & Fachgespräch",
            subtitle = "Prüfungstaktik, 100-Punkte-Schlüssel, AdA-Nachweis & Ergänzungsprüfung",
            topics = listOf(
                CurriculumTopic(
                    id = "sa_fachgespraech",
                    moduleCode = "SA",
                    numberLabel = "SIHK",
                    title = "Praxisfall & Mündliches Fachgespräch",
                    summary = "Operatoren, Ergänzungsprüfung (2:1-Regel), Präsentationsgliederung & Ausschuss-Dialog",
                    keyConcepts = listOf("Ergänzungsprüfung 2:1", "Operatoren & Zeitbudget", "5-Punkte-Präsentation"),
                    lessonIds = listOf("sa_fachgespraech_01", "sa_fachgespraech_02"),
                    examDurationLabel = "LogMstrV · 100-P.-Schlüssel"
                )
            )
        )
    )

    fun calculateIhkGrade(points: Int): Int = when {
        points >= 92 -> 1
        points >= 81 -> 2
        points >= 67 -> 3
        points >= 50 -> 4
        points >= 30 -> 5
        else -> 6
    }

    /**
     * Gibt bei Note 5 (30–49 Punkte) die mindestens erforderlichen Punkte in der
     * mündlichen Ergänzungsprüfung zurück ((2 * schriftl + mündl) / 3 >= 50), sonst null.
     */
    fun supplementaryOralTarget(writtenPoints: Int): Int? {
        if (writtenPoints !in 30..49) return null
        return (150 - 2 * writtenPoints).coerceIn(50, 100)
    }
}
