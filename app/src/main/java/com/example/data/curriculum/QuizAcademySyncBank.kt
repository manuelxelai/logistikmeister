package com.example.data.curriculum

/**
 * Kompakte Wahr/Falsch-Checks & Microlearning-Karten im Stil etablierter IHK-Lernkarten-Apps
 * (eine Frage, eine prägnante Kernaussage) über alle BQ- und HQ-Handlungsbereiche des Geprüften Logistikmeisters.
 */
internal object QuizAcademySyncBank {
    val questions: List<LearningDrillQuestion> = listOf(
        LearningDrillQuestion(
            id = "qa_wf_recht_1",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Wahr / Falsch · § 102 BetrVG",
            prompt = "Aussage: Eine ordentliche Kündigung ohne vorherige Anhörung des Betriebsrats (§ 102 BetrVG) ist zwar bußgeldbewehrt, aber zivilrechtlich trotzdem voll wirksam.",
            options = listOf(
                "Wahr – fehlende Anhörung berührt die Wirksamkeit nicht.",
                "Falsch – nach § 102 Abs. 1 Satz 3 BetrVG ist jede ohne vorherige Betriebsratsanhörung ausgesprochene Kündigung unwirksam."
            ),
            correctIndex = 1,
            explanation = "§ 102 Abs. 1 BetrVG ist zwingendes Recht: Fehlt die Anhörung oder erfolgt sie erst nach Zugang der Kündigung, ist die Kündigung unheilbar nichtig/unwirksam."
        ),
        LearningDrillQuestion(
            id = "qa_wf_recht_2",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Wahr / Falsch · Prokura vs. Handlungsvollmacht",
            prompt = "Aussage: Ein Handlungsbevollmächtigter (§ 54 HGB) darf im Gegensatz zum Prokuristen branchenübliche Geschäfte abschließen, aber ohne besondere Befugnis keine Darlehen aufnehmen oder Wechsel eingehen.",
            options = listOf(
                "Wahr – genau das grenzt § 54 Abs. 2 HGB von der umfassenderen Prokura ab.",
                "Falsch – § 54 HGB umfasst grundsätzlich alle Bankkredite."
            ),
            correctIndex = 0,
            explanation = "Nach § 54 Abs. 2 HGB braucht der Handlungsbevollmächtigte neben Grundstücks- und Prozessführung auch für Darlehensaufnahmen und Wechselzeichnungen eine besondere Vollmacht."
        ),
        LearningDrillQuestion(
            id = "qa_wf_bwl_1",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Wahr / Falsch · Ökonomisches Prinzip",
            prompt = "Aussage: Wenn im Warenausgang mit einem fest vorgegebenen Tagesbudget von 1.200 € eine möglichst hohe Anzahl an Kundenpaletten versendet werden soll, handelt man nach dem Minimalprinzip.",
            options = listOf(
                "Wahr – festes Budget bedeutet immer Minimalprinzip.",
                "Falsch – bei gegebenem Input (Budget) den maximalen Output zu erzielen, ist das Maximalprinzip."
            ),
            correctIndex = 1,
            explanation = "Maximalprinzip: fester Mitteleinsatz -> maximaler Erfolg. Minimalprinzip: festes Ziel (z. B. 300 Paletten) -> minimaler Einsatz."
        ),
        LearningDrillQuestion(
            id = "qa_wf_bwl_2",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Wahr / Falsch · Optimale Bestellmenge (Andler)",
            prompt = "Aussage: Nach der klassischen Andler-Formel liegt die kostenminimale Bestellmenge genau dort, wo sich jährliche Bestellfixkosten und jährliche Lagerhaltungskosten je Periode exakt entsprechen.",
            options = listOf(
                "Wahr – im Minimum der Gesamtkostenkurve gilt Bestellkosten = Lagerhaltungskosten.",
                "Falsch – die Bestellkosten sind am Optimum immer doppelt so hoch."
            ),
            correctIndex = 0,
            explanation = "Im Schnittpunkt der fallenden Bestellkostenkurve und steigenden Lagerhaltungskostenkurve erreichen die Gesamtkosten nach Andler ihr Minimum."
        ),
        LearningDrillQuestion(
            id = "qa_wf_mikp_1",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Wahr / Falsch · ABC/XYZ-Matrix",
            prompt = "Aussage: CZ-Artikel (geringer Wertanteil, sehr unregelmäßiger Verbrauch) werden am wirtschaftlichsten über streng synchronisierte Just-in-Time-Einzellieferungen beschafft.",
            options = listOf(
                "Wahr – CZ passt am besten zu Just-in-Time.",
                "Falsch – AX-Teile passen zu JIT; für CZ lohnt oft eine Vorrats- bzw. Sammelbestellung oder vereinfachte Abwicklung (C-Teile-Management)."
            ),
            correctIndex = 1,
            explanation = "JIT rechnet sich bei hohen Kapitalbindungswerten und stetigem Verbrauch (AX), nicht bei billigen unplanbaren Kleinteilen."
        ),
        LearningDrillQuestion(
            id = "qa_wf_mikp_2",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Wahr / Falsch · Netzplantechnik (FAZ/FEZ)",
            prompt = "Aussage: Der freie Puffer (FP) eines Vorgangs gibt an, um wie viel Zeit er verschoben werden darf, ohne dass der frühestmögliche Beginn (FAZ) irgendeines Nachfolgers gefährdet wird.",
            options = listOf(
                "Wahr – FP = min(FAZ_Nachfolger) − FEZ_Vorgang.",
                "Falsch – das beschreibt den gesamten Projekt-Endtermin."
            ),
            correctIndex = 0,
            explanation = "Der freie Puffer lässt den FAZ aller Nachfolger unangetastet; der Gesamtpuffer (GP = SAZ − FAZ) betrachtet dagegen den Projekt-Endtermin."
        ),
        LearningDrillQuestion(
            id = "qa_wf_zib_1",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Wahr / Falsch · Kommunikation",
            prompt = "Aussage: In der Konfliktmoderation helfen sogenannte 'Du-Botschaften' ('Sie machen das immer falsch!') besonders gut, um Abwehrhaltungen beim Mitarbeiter sofort abzubauen.",
            options = listOf(
                "Wahr – direkte Schuldzuweisungen klären Fronten schnell.",
                "Falsch – Sachliche Ich-Botschaften auf Basis beobachteter Fakten vermeiden Eskalation und erhalten das Beziehungs-Ohr."
            ),
            correctIndex = 1,
            explanation = "Du-Botschaften mit Verallgemeinerungen ('immer/nie') lösen sofort Rechtfertigung aus; Ich-Botschaften trennen Beobachtung, Wirkung und Wunsch."
        ),
        LearningDrillQuestion(
            id = "qa_wf_ntg_1",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Wahr / Falsch · Kraft vs. Masse",
            prompt = "Aussage: Eine Palette mit 1.000 kg Masse übt auf der Ladefläche bei Erdbeschleunigung (g ≈ 9,81 m/s²) rund 9,81 kN Gewichtskraft aus (in VDI-2700-Überschlag oft 1.000 daN).",
            options = listOf(
                "Wahr – F_G = m × g = 1.000 kg × 9,81 m/s² = 9.810 N ≈ 9,81 kN ≈ 981 daN.",
                "Falsch – 1.000 kg entsprechen exakt 100 N."
            ),
            correctIndex = 0,
            explanation = "1 daN ≈ 10 N entspricht ca. 1 kg Gewichtskraft – eine extrem hilfreiche Eselsbrücke für Ladungssicherungs-Berechnungen!"
        ),
        LearningDrillQuestion(
            id = "qa_wf_logistik_1",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Wahr / Falsch · JIT vs. JIS",
            prompt = "Aussage: Just-in-Sequence (JIS) geht über Just-in-Time (JIT) hinaus, weil die angelieferten Module bereits exakt in der späteren Montagereihenfolge (z. B. nach Fahrzeug-FIN) sequenziert auf dem Lkw bzw. Gestell stehen.",
            options = listOf(
                "Wahr – JIS ergänzt den richtigen Zeitpunkt (JIT) um die exakte Produktionsreihenfolge.",
                "Falsch – JIS bedeutet lediglich besonders langsame Seefracht."
            ),
            correctIndex = 0,
            explanation = "Bei JIS (z. B. Autositze oder Stoßfänger in der Automobilindustrie) spart sich das Abnehmerwerk Sortierung und Umpacken am Band."
        ),
        LearningDrillQuestion(
            id = "qa_wf_logistik_2",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Wahr / Falsch · Incoterms 2020 EXW vs. DDP",
            prompt = "Aussage: Unter den 11 Incoterms-2020-Klauseln trägt bei EXW (Ab Werk) der Verkäufer die maximalen Transport- und Zollpflichten, während bei DDP der Käufer ab Werk abholt.",
            options = listOf(
                "Wahr – EXW steht für maximale Verkäuferverantwortung.",
                "Falsch – es gilt genau umgekehrt: EXW ist die Minimalklausel für den Verkäufer, DDP (Geliefert verzollt) die Maximalklausel."
            ),
            correctIndex = 1,
            explanation = "Eselsbrücke: EXW = Minimalpflicht Verkäufer (Bereitstellung auf eigenem Hof); DDP = Maximalpflicht Verkäufer inkl. Transport, Ausfuhr- und Einfuhrverzollung bis zum Käufer."
        ),
        LearningDrillQuestion(
            id = "qa_wf_logistik_3",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Wahr / Falsch · Chaotische Lagerhaltung",
            prompt = "Aussage: Bei der dynamischen (chaotischen) Lagerplatzvergabe über ein WMS muss ein leerer Platz nicht fest einem einzigen Artikel reserviert bleiben, wodurch sich der Raumnutzungsgrad deutlich erhöht.",
            options = listOf(
                "Wahr – freie Plätze werden nach Systemregeln (ABC-Zonen, Gewicht, Sperrgut) flexibel belegt.",
                "Falsch – chaotische Lagerung heißt, dass Artikel ohne System und ohne Buchung abgelegt werden."
            ),
            correctIndex = 0,
            explanation = "Chaotisch meint 'frei wählbarer Lagerplatz mit exakter WMS-Bestandsbuchung' – hohe Flächenauslastung und kurze Wege bei Zonenbildung!"
        ),
        LearningDrillQuestion(
            id = "qa_wf_org_1",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Wahr / Falsch · DIN EN 15635 Einstufung Rot",
            prompt = "Aussage: Wird bei der Regalprüfung nach DIN EN 15635 ein Ständer-Schaden als 'Rot' (akutes Gefährdungsrisiko) eingestuft, genügt ein Vermerk zur Reparatur innerhalb von 6 Wochen bei laufendem Betrieb.",
            options = listOf(
                "Wahr – bis zur jährlichen Prüfung kann normal weiter eingelagert werden.",
                "Falsch – bei Schadenstufe 'Rot' muss der betroffene Regalbereich sofort gesperrt und unverzüglich vollständig entladen werden!"
            ),
            correctIndex = 1,
            explanation = "Grün = beobachten; Orange = baldmöglichst entlasten & binnen max. 4 Wochen reparieren; Rot = akute Einsturzgefahr -> SOFORT sperren & entladen!"
        ),
        LearningDrillQuestion(
            id = "qa_wf_org_2",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Wahr / Falsch · Poka-Yoke & KVP",
            prompt = "Aussage: Der PDCA-Zyklus (Plan-Do-Check-Act / Deming-Kreis) im KVP ist ein einmaliger Projekt-Durchlauf, der nach Erreichen des Ziels komplett eingestellt wird.",
            options = listOf(
                "Wahr – nach Act endet der Prozess endgültig.",
                "Falsch – PDCA beschreibt einen fortlaufenden Regelkreis zur ständigen Verbesserung und Standardisierung."
            ),
            correctIndex = 1,
            explanation = "Nach erfolgreicher Überprüfung (Check) wird der verbesserte Standard (Act) verankert und startet erneut auf höherem Niveau."
        ),
        LearningDrillQuestion(
            id = "qa_wf_fuehrung_1",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Wahr / Falsch · Mitarbeitermotivation",
            prompt = "Aussage: Nach Herzbergs Zwei-Faktoren-Theorie verhindert eine saubere Umkleide und pünktliche Gehaltszahlung (Hygienefaktoren) zwar Unzufriedenheit, erzeugt aber langfristig keine echte Leistungsbegeisterung.",
            options = listOf(
                "Wahr – echte Motivation erfordert Motivatoren wie Verantwortung, Anerkennung und Entwicklungsmöglichkeiten.",
                "Falsch – Hygienefaktoren sind identisch mit Intrinsischer Motivation."
            ),
            correctIndex = 0,
            explanation = "Hygienefaktoren sichern den neutralen Ausgangszustand ('Nicht-Unzufriedenheit'); erst Motivatoren treiben Eigeninitiative an."
        ),
        LearningDrillQuestion(
            id = "qa_wf_sa_1",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Wahr / Falsch · IHK-Prüfungs-Operatoren",
            prompt = "Aussage: Laut IHK-Korrekturrichtlinien erhält man für die reine Auflistung von 5 Stichworten die volle Punktzahl, auch wenn der Operator 'Beurteilen Sie begründet...' lautet.",
            options = listOf(
                "Wahr – Stichpunkte bringen immer 100 % der Punkte.",
                "Falsch – bei 'Beurteilen' fordert der Erwartungshorizont eine kriteriengeleitete Abwägung (Vor-/Nachteile) plus ein begründetes Fazit."
            ),
            correctIndex = 1,
            explanation = "Operatoren bestimmen die Anforderungstiefe: Anforderungsbereich I ('Nennen'), II ('Erläutern/Berechnen'), III ('Beurteilen/Entscheiden')."
        )
    )
}
