package com.example.data.curriculum

/**
 * Prüfungsnahe 4-Optionen-Multiple-Choice-Fragen (A–D) mit ausführlichen Gesetzes-, KLR- und Praxisbegründungen
 * im Stil des Online-Prüfungsportals deinePrüfung.de für den Geprüften Logistikmeister (BQ & HQ).
 */
internal object DeinePruefungSyncBank {
    val questions: List<LearningDrillQuestion> = listOf(
        // ==========================================
        // BQ 1: Rechtsbewusstes Handeln (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_bq_recht_1",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Arbeitsrecht · Innerbetrieblicher Schadensausgleich",
            prompt = "Ein Staplerfahrer beschädigt im normalen Schichtbetrieb leicht fahrlässig (kleine Unachtsamkeit ohne Pflichtenverstoß) ein Regalfeld. Wie haftet er nach den Grundsätzen des innerbetrieblichen Schadensausgleichs?",
            options = listOf(
                "A) Er haftet grundsätzlich voll für den gesamten Sachschaden.",
                "B) Er haftet bei leichter Fahrlässigkeit gar nicht (0 %).",
                "C) Er haftet automatisch zu genau 50 % des Neuwerts.",
                "D) Er haftet mit maximal drei Monatsgehältern unabhängig vom Verschulden."
            ),
            correctIndex = 1,
            explanation = "Beim innerbetrieblichen Schadensausgleich gilt: leichte Fahrlässigkeit = keine Arbeitnehmerhaftung (0 %); mittlere Fahrlässigkeit = quotelte/anteilige Haftung nach Einkommen & Versicherbarkeit; grobe Fahrlässigkeit/Vorsatz = i. d. R. volle Haftung."
        ),
        LearningDrillQuestion(
            id = "dp_bq_recht_2",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Arbeitsrecht · Entgeltfortzahlungsgesetz (§ 3 EFZG)",
            prompt = "Ein neu eingestellter Kommissionierer erkrankt unverschuldet genau 14 Tage nach Beginn seines Arbeitsverhältnisses. Wann entsteht sein gesetzlicher Anspruch auf Entgeltfortzahlung durch den Arbeitgeber?",
            options = listOf(
                "A) Sofort ab dem 1. Arbeitstag ohne jede Wartezeit.",
                "B) Erst nach 4-wöchiger ununterbrochener Dauer des Arbeitsverhältnisses (§ 3 Abs. 3 EFZG).",
                "C) Erst nach Ablauf der vollen 6-monatigen Probezeit.",
                "D) Erst nach Zustimmung durch den Betriebsrat."
            ),
            correctIndex = 1,
            explanation = "Nach § 3 Abs. 3 EFZG entsteht der Anspruch auf Entgeltfortzahlung im Krankheitsfall (bis zu 6 Wochen) erst nach vierwöchiger ununterbrochener Beschäftigung. Während der ersten 4 Wochen zahlt bei gesetzlich Versicherten die Krankenkasse Krankengeld."
        ),
        LearningDrillQuestion(
            id = "dp_bq_recht_3",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Umweltrecht · WHG & AwSV im Gefahrstofflager",
            prompt = "Beim Umfüllen von Hydrauliköl im Lager tritt Flüssigkeit aus und droht über den Hofablauf in ein Gewässer zu gelangen. Welches Prinzip verlangt das Wasserhaushaltsgesetz (WHG) für Anlagen mit wassergefährdenden Stoffen?",
            options = listOf(
                "A) Das Besorgnisgrundsatz-Prinzip (Auffangwanne / Rückhaltevermögen, sodass Verunreinigung nicht zu besorgen ist).",
                "B) Eine bloße nachträgliche Meldung am Jahresende reicht aus.",
                "C) Öl darf auf versiegeltem Asphalt beliebig zwischengelagert werden.",
                "D) Wasserrecht greift nur bei Seeschifffahrtshäfen."
            ),
            correctIndex = 0,
            explanation = "§ 62 WHG normiert den Besorgnisgrundsatz: Anlagen zum Lagern, Abfüllen und Umschlagen wassergefährdender Stoffe (AwSV) müssen so beschaffen sein, dass eine nachteilige Veränderung der Wasserbeschaffenheit nicht zu besorgen ist (z. B. dichte Auffangwannen, Bindemittel, Kanalabdeckungen)."
        ),

        // ==========================================
        // BQ 2: Betriebswirtschaftliches Handeln (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_bq_bwl_1",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Kostenrechnung · Kalkulatorische Abschreibung",
            prompt = "Warum berechnet das interne Rechnungswesen (KLR) die kalkulatorische Abschreibung vom Wiederbeschaffungswert statt vom historischen Anschaffungswert?",
            options = listOf(
                "A) Weil das Finanzamt dies in der Steuerbilanz zwingend vorschreibt.",
                "B) Um über die Verkaufspreise Substanzerhaltung zu sichern, falls ein neuer Stapler teurer ist als der alte.",
                "C) Um den ausgewiesenen Jahresüberschuss in der GuV künstlich zu erhöhen.",
                "D) Weil Anschaffungskosten im Lager grundsätzlich steuerfrei sind."
            ),
            correctIndex = 1,
            explanation = "Die KLR zielt auf reale Substanzerhaltung ab: Steigen die Preise für Flurförderzeuge, müssen über die Selbstkosten genügend Mittel für eine gleichwertige Ersatzinvestition erwirtschaftet werden."
        ),
        LearningDrillQuestion(
            id = "dp_bq_bwl_2",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Deckungsbeitragsrechnung · Preisuntergrenze",
            prompt = "Ein Logistikdienstleister hat freie Lkw-Kapazität auf einer Rücktour. Welche Größe bildet kurzfristig die absolute Preisuntergrenze für einen Zusatzauftrag?",
            options = listOf(
                "A) Die gesamten Vollkosten inklusive Abschreibungen und Verwaltungsgemeinkosten.",
                "B) Die variablen Stück- bzw. Zusatzkosten dieses Auftrags (z. B. Diesel, Maut, Zusatzhandling).",
                "C) Ausschließlich die fixen Hallenmieten anteilig.",
                "D) Der Vorjahresumsatz dividiert durch alle Mitarbeiter."
            ),
            correctIndex = 1,
            explanation = "Kurzfristig (bei freien Kapazitäten) deckt jeder Preis oberhalb der variablen Kosten bereits diese Zusatzkosten und liefert einen positiven Deckungsbeitrag I zur Reduzierung der ohnehin anfallenden Fixkosten."
        ),
        LearningDrillQuestion(
            id = "dp_bq_bwl_3",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Kennzahlen · Eigenkapitalrentabilität vs. Gesamtkapitalrentabilität",
            prompt = "Wann tritt in der Investitionsfinanzierung eines automatisierten Kleinteilelagers der positive Leverage-Effekt ein?",
            options = listOf(
                "A) Wenn der Fremdkapitalzinssatz höher ist als die Gesamtkapitalrentabilität.",
                "B) Wenn die Gesamtkapitalrentabilität (ROI) größer ist als der zu zahlende Fremdkapitalzinssatz.",
                "C) Wenn das Lager zu 100 % aus Eigenkapital ohne Kredite finanziert wird.",
                "D) Sobald die Abschreibungsdauer auf 1 Jahr verkürzt wird."
            ),
            correctIndex = 1,
            explanation = "Solange das investierte Kapital im Betrieb eine höhere Rendite (GKR) erwirtschaftet, als die Bank Zinsen (FK-Zins) verlangt, steigert zusätzliches Fremdkapital die Eigenkapitalrentabilität (Hebelwirkung)."
        ),

        // ==========================================
        // BQ 3: MIKP (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_bq_mikp_1",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Projektmanagement · Kritischer Pfad",
            prompt = "Woran erkennen Sie im Netzplan eines WMS-Rollout-Projekts alle Vorgänge, die auf dem kritischen Pfad liegen?",
            options = listOf(
                "A) Sie haben die längsten Kostenpositionen im Budget.",
                "B) Ihr Gesamtpuffer (GP = SAZ − FAZ) und ihr Freier Puffer betragen exakt 0.",
                "C) Sie finden immer am Ende nach der Abnahme statt.",
                "D) Sie besitzen mindestens 5 Tage Pufferzeit."
            ),
            correctIndex = 1,
            explanation = "Auf dem kritischen Pfad gilt GP = 0 (und damit auch FP = 0): Jede Verzögerung eines kritischen Vorgangs verschiebt unmittelbar das gesamte Projektende."
        ),
        LearningDrillQuestion(
            id = "dp_bq_mikp_2",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Problemlösung · Ishikawa-Diagramm (6M)",
            prompt = "Bei gehäuften Fehlkommissionierungen setzen Sie ein Ishikawa-Fischgräten-Diagramm ein. Welche Haupteinflussgrößen bilden klassischerweise die 6M-Kategorien?",
            options = listOf(
                "A) Markt, Marge, Miete, Maut, Menge, Monat.",
                "B) Mensch, Maschine, Material, Methode, Milieu (Umfeld), Messung.",
                "C) Montage, Marketing, Meister, Mitarbeiter, Modul, Medium.",
                "D) Minimum, Maximum, Mittelwert, Median, Modus, Matrix."
            ),
            correctIndex = 1,
            explanation = "Das Ursache-Wirkungs-Diagramm nach Kaoru Ishikawa strukturiert Fehlerursachen systematisch nach Mensch, Maschine, Material, Methode, Milieu (Arbeitsumfeld) und Messung (Prüfmittel)."
        ),
        LearningDrillQuestion(
            id = "dp_bq_mikp_3",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Logistik-IT · ERP, WMS & MFS",
            prompt = "Welche Software-Ebene steuert bei einem automatischen Hochregallager in Echtzeit direkt Regalbediengeräte, Fördertechnikweichen und Lichtschranken (SPS-Anbindung)?",
            options = listOf(
                "A) Das kaufmännische ERP-System (z. B. Rechnungsstellung).",
                "B) Der Materialflussrechner (MFS / MFR) als unterlagerte Steuerungsschicht unter dem WMS.",
                "C) Die Lohnbuchhaltungs-Software.",
                "D) Das CRM-Vertriebsportal."
            ),
            correctIndex = 1,
            explanation = "Schichtenmodell: ERP verwaltet Kundenaufträge & Bestände kaufmännisch; das WMS (LVS) verwaltet Lagerplätze & Kommissionierstrategien; der MFS/MFR übernimmt die zeitkritische Telegramm- und Fördertechniksteuerung bis zur SPS."
        ),

        // ==========================================
        // BQ 4: Zusammenarbeit im Betrieb (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_bq_zib_1",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Teamentwicklung · Phasenmodell nach Tuckman",
            prompt = "Ein neu zusammengestelltes Nachtschicht-Team streitet nach drei Wochen stark über Aufgabenverteilung und informelle Rollen. In welcher Tuckman-Phase befindet sich das Team?",
            options = listOf(
                "A) Performing (Arbeits- und Leistungsphase).",
                "B) Storming (Auseinandersetzungs-/Konfliktphase).",
                "C) Forming (Orientierungsphase).",
                "D) Adjourning (Auflösungsphase)."
            ),
            correctIndex = 1,
            explanation = "Nach Tuckman folgt auf das höfliche Kennenlernen (Forming) das Storming (Rollenkämpfe, Reibung), bevor gemeinsame Spielregeln entstehen (Norming) und das Team hochproduktiv arbeitet (Performing)."
        ),
        LearningDrillQuestion(
            id = "dp_bq_zib_2",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Führungstheorie · Situatives Führen (Hersey/Blanchard)",
            prompt = "Ein Mitarbeiter beherrscht seine Arbeitsabläufe im Versand fachlich sehr sicher, ist aber durch Monotonie demotiviert und unsicher. Welcher Führungsstil passt nach Hersey/Blanchard (Reifegrad R3)?",
            options = listOf(
                "A) Dirigieren (S1): Exakte Einzelanweisungen ohne Mitsprache.",
                "B) Partizipieren / Unterstützen (S3): Geringe Aufgabenlenkung, hohe Beziehungs- und Motivationsunterstützung.",
                "C) Delegieren (S4): Vollständiger Rückzug ohne Gespräche.",
                "D) Strenge Abmahnung ohne Vorgespräch."
            ),
            correctIndex = 1,
            explanation = "Bei hohem fachlichem Können aber geringer Motivation/Bereitschaft (R3) empfiehlt das situative Führen den partizipativen Stil (S3): Einbeziehen in Entscheidungen, Wertschätzung und Motivationsförderung."
        ),
        LearningDrillQuestion(
            id = "dp_bq_zib_3",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Aufbauorganisation · Einlinien- vs. Matrixorganisation",
            prompt = "Was ist der größte organisatorische Nachteil einer Matrixorganisation gegenüber einem klassischen Einliniensystem?",
            options = listOf(
                "A) Es gibt keinerlei Fachabteilungen mehr.",
                "B) Kompetenzkonflikte und Abstimmungsaufwand durch Mehrfachunterstellung (zwei Vorgesetzte: Fach- und Projektlinie).",
                "C) Es dürfen keine Projekte mehr durchgeführt werden.",
                "D) Die Dienstwege sind vollständig starr und langsam."
            ),
            correctIndex = 1,
            explanation = "In der Matrixorganisation greifen z. B. disziplinarischer Logistikleiter und Projektleiter gleichzeitig auf Mitarbeiter zu (Mehrliniensystem) – das steigert Flexibilität, erfordert aber klare Konfliktregeln."
        ),

        // ==========================================
        // BQ 5: NTG (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_bq_ntg_1",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Physik · Gesamtwirkungsgrad",
            prompt = "Ein Elektro-Hubwerk hat einen Motorwirkungsgrad von η₁ = 0,90 und ein nachgeschaltetes Getriebe mit η₂ = 0,80. Wie groß ist der Gesamtwirkungsgrad η_ges?",
            options = listOf(
                "A) 0,85 (85 % als arithmetischer Mittelwert).",
                "B) 0,72 (72 %, da Teilwirkungsgrade multipliziert werden: 0,90 × 0,80).",
                "C) 1,70 (170 % durch Addition).",
                "D) 0,10 (10 % Differenz)."
            ),
            correctIndex = 1,
            explanation = "Bei in Reihe geschalteten Energiewandlern werden die Wirkungsgrade multipliziert: η_ges = η₁ × η₂ = 0,90 × 0,80 = 0,72 (72 %)."
        ),
        LearningDrillQuestion(
            id = "dp_bq_ntg_2",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Elektrotechnik · Leistung an der Ladestation",
            prompt = "Ein Staplerladegerät arbeitet mit U = 400 V und einer Stromaufnahme von I = 16 A (vereinfacht cos φ = 1, Gleichwertbetrachtung). Welche elektrische Leistung P ergibt sich nach P = U × I?",
            options = listOf(
                "A) 25 W",
                "B) 416 W",
                "C) 6.400 W (6,4 kW)",
                "D) 25.000 W"
            ),
            correctIndex = 2,
            explanation = "Elektrische Leistung P = U × I = 400 V × 16 A = 6.400 VA bzw. 6,4 kW."
        ),
        LearningDrillQuestion(
            id = "dp_bq_ntg_3",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Mechanik · Reibungskraft (Gleitreibung)",
            prompt = "Eine Gitterbox drückt mit der Normalkraft F_N = 5.000 N auf einen Hallenboden (Gleitreibungszahl μ = 0,40). Welche horizontale Zugkraft F_R ist zum Überwinden der Reibung nötig?",
            options = listOf(
                "A) 1.250 N",
                "B) 2.000 N (F_R = μ × F_N = 0,40 × 5.000 N)",
                "C) 12.500 N",
                "D) 5.000 N"
            ),
            correctIndex = 1,
            explanation = "Reibungskraft F_R = μ × F_N = 0,40 × 5.000 N = 2.000 N. Je größer der Reibwert μ (z. B. durch Antirutschmatten beim Lkw-Transport), desto höher die erforderliche Verschiebekraft."
        ),

        // ==========================================
        // HQ 1: Logistikprozesse (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_hq_logistik_1",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Supply Chain · Bullwhip-Effekt (Peitscheneffekt)",
            prompt = "Was beschreibt der Bullwhip-Effekt in einer mehrstufigen Lieferkette vom Endkunden über Großhandel und Werk bis zum Vorlieferanten?",
            options = listOf(
                "A) Eine gleichmäßige Reduzierung aller Bestände auf null.",
                "B) Ein Aufschaukeln kleiner Nachfrageschwankungen beim Endkunden zu immer extremeren Bestell- und Bestandsschwankungen stromaufwärts.",
                "C) Die verpflichtende Zollprüfung an EU-Außengrenzen.",
                "D) Ein automatisches Rabattsystem bei Lkw-Vollladungen."
            ),
            correctIndex = 1,
            explanation = "Ursachen des Bullwhip-Effekts sind u. a. zeitverzögerte Informationsweitergabe, Bündelbestellungen (Losgrößen) und Angstbestände. Gegenmittel sind transparente Echtzeit-Abverkaufsdaten (EDI, CPFR, VMI)."
        ),
        LearningDrillQuestion(
            id = "dp_hq_logistik_2",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Kommissioniersysteme · Mann-zur-Ware vs. Ware-zum-Mann",
            prompt = "Welcher wesentliche Vorteil zeichnet ein dynamisches 'Ware-zum-Mann'-Kommissioniersystem (z. B. AutoStore oder AKL-Pickplatz) gegenüber dem klassischen 'Mann-zur-Ware'-Prinzip aus?",
            options = listOf(
                "A) Es entfallen sämtliche Investitionskosten und IT-Schnittstellen.",
                "B) Die unproduktiven Wegzeiten des Kommissionierers sinken drastisch, die Pickleistung und Ergonomie steigen.",
                "C) Es eignet sich ausschließlich für 6-Meter-Langgut-Stahlträger.",
                "D) Artikel müssen nicht mehr im WMS gebucht werden."
            ),
            correctIndex = 1,
            explanation = "Bei statischer Mann-zur-Ware-Kommissionierung machen Wegzeiten oft 50–65 % der Arbeitszeit aus. Ware-zum-Mann eliminiert lange Laufwege und ermöglicht mit Pick-by-Light hohe Pickraten bei geringer Fehlerquote."
        ),
        LearningDrillQuestion(
            id = "dp_hq_logistik_3",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Produktionsversorgung · Milkrun-Prinzip",
            prompt = "Wie optimiert ein innerbetriebliches Milkrun-Konzept (Routenzug) die Versorgung von Montagelinien?",
            options = listOf(
                "A) Jeder Gabelstapler fährt jede einzelne Kleinbox mit 50 % Leerfahrt separat zur Linie.",
                "B) Ein getakteter Routenzug fährt feste Haltestellen nach Fahrplan an, bringt volle KLT nach Kanban und nimmt Leergut direkt mit.",
                "C) Alle Montagelinien werden mit Wochenbeständen direkt auf dem Boden zugestellt.",
                "D) Nur externe Speditionen dürfen innerhalb der Werkshalle anliefern."
            ),
            correctIndex = 1,
            explanation = "Der Milkrun bündelt Kleinmengen auf einer festen Taktstrecke, senkt den Staplerverkehr in Montagegängen (Arbeitssicherheit!) und koppelt Vollgut-Anlieferung mit Leergut-Entsorgung."
        ),

        // ==========================================
        // HQ 2: Betriebliche Org. & Kostenwesen (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_hq_org_1",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Kostenrechnung · Betriebsabrechnungsbogen (BAB)",
            prompt = "Welche zentrale Aufgabe erfüllt der Betriebsabrechnungsbogen (BAB) innerhalb der Vollkostenrechnung im Logistikbetrieb?",
            options = listOf(
                "A) Er berechnet den privaten Einkommensteuersatz der Geschäftsführer.",
                "B) Er verteilt Gemeinkosten auf Kostenstellen (Vor-/Endkostenstellen) und ermittelt Zuschlagssätze für die Kostenträgerrechnung.",
                "C) Er ersetzt den jährlichen Kassensturz im Wareneingang.",
                "D) Er dokumentiert ausschließlich Überstunden für das Arbeitszeitgesetz."
            ),
            correctIndex = 1,
            explanation = "Der BAB bildet die Brücke zwischen Kostenarten- und Kostenträgerrechnung: Gemeinkosten werden mittels Verteilungsschlüsseln (z. B. m², kWh, Köpfe) auf Kostenstellen umgelegt, um Kalkulationssätze zu bilden."
        ),
        LearningDrillQuestion(
            id = "dp_hq_org_2",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Qualitätsmanagement · FMEA-Risikobewertung",
            prompt = "Mit welchen drei Kriterien wird in einer Prozess-FMEA klassischerweise das Risiko eines möglichen Fehlers (z. B. Vertauschen von Gefahrstoff-Etiketten) bewertet?",
            options = listOf(
                "A) Umsatz (U), Gewinn (G), Steuer (S).",
                "B) Bedeutung (B), Auftretenswahrscheinlichkeit (A) und Entdeckungswahrscheinlichkeit (E).",
                "C) Gewicht, Volumen und Stellfläche.",
                "D) Lieferzeit, Skonto und Zahlungsziel."
            ),
            correctIndex = 1,
            explanation = "Die FMEA bewertet Bedeutung der Fehlerfolge (B), Wahrscheinlichkeit des Auftretens (A) und Wahrscheinlichkeit der rechtzeitigen Entdeckung (E) jeweils von 1 bis 10 (klassisch RPZ = B × A × E; nach AIAG-VDA über Aufgabenpriorität AP)."
        ),
        LearningDrillQuestion(
            id = "dp_hq_org_3",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Arbeitsschutz · Arbeitssicherheitsgesetz (ASiG)",
            prompt = "Welche beiden beratenden Experten muss ein Betrieb nach dem Arbeitssicherheitsgesetz (ASiG) schriftlich bestellen, um den Unternehmer im Arbeits- und Gesundheitsschutz zu unterstützen?",
            options = listOf(
                "A) Einen externen Wirtschaftsprüfer und einen Insolvenzverwalter.",
                "B) Betriebsärzte und Fachkräfte für Arbeitssicherheit (SiFa).",
                "C) Ausschließlich zwei Gewerkschaftssekretäre.",
                "D) Den Zollbeauftragten und den Fuhrparkleiter."
            ),
            correctIndex = 1,
            explanation = "Nach §§ 2, 5 ASiG und DGUV Vorschrift 2 beraten Betriebsärzte (arbeitsmedizinisch) und Fachkräfte für Arbeitssicherheit (sicherheitstechnisch) Unternehmen, Logistikmeister und Betriebsrat im ASA (Arbeitsschutzausschuss)."
        ),

        // ==========================================
        // HQ 3: Führung und Personal (3 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "dp_hq_fuehrung_1",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Personalführung · Management by Objectives (MbO)",
            prompt = "Was kennzeichnet das Führungsprinzip 'Management by Objectives' (Führen durch Zielvereinbarung) im Vergleich zum reinen Befehlsstil?",
            options = listOf(
                "A) Der Logistikmeister kontrolliert jeden einzelnen Handgriff minütlich.",
                "B) Vorgesetzter und Mitarbeiter vereinbaren gemeinsam messbare Leistungsziele (z. B. SMART) und der Mitarbeiter gestaltet den Weg relativ eigenverantwortlich.",
                "C) Ziele werden geheim gehalten und erst am Jahresende verkündet.",
                "D) Es greift nur dann ein Vorgesetzter ein, wenn bereits ein Totalschaden vorliegt."
            ),
            correctIndex = 1,
            explanation = "MbO stärkt Eigenverantwortung und Motivation durch gemeinsam vereinbarte, messbare Ziele und regelmäßiges Ziel-Review (im Gegensatz zu MbE – Management by Exception, Eingreifen nur im Ausnahmefall)."
        ),
        LearningDrillQuestion(
            id = "dp_hq_fuehrung_2",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Personalplanung · Netto- vs. Brutto-Personalbedarf",
            prompt = "Für die Kommissionierung errechnen Sie einen Netto-Einsatzbedarf von 20 Vollzeitkräften. Für Urlaub, Krankheit und Weiterbildung planen Sie 15 % Verteil-/Reservezuschlag ein. Wie hoch ist der Brutto-Personalbedarf?",
            options = listOf(
                "A) 17 Mitarbeiter",
                "B) 23 Mitarbeiter (20 Einsatzbedarf + 3 Reservebedarf)",
                "C) 20 Mitarbeiter",
                "D) 35 Mitarbeiter"
            ),
            correctIndex = 1,
            explanation = "Brutto-Personalbedarf = Einsatzbedarf + Reservebedarf = 20 × (1 + 0,15) = 23 Mitarbeiter, um eine durchgängige Schichtbesetzung trotz Abwesenheiten abzusichern."
        ),
        LearningDrillQuestion(
            id = "dp_hq_fuehrung_3",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Arbeitsrecht · Zeugnisanspruch (§ 109 GewO)",
            prompt = "Welche Formulierung in einem qualifizierten Arbeitszeugnis entspricht nach gängiger IHK-/Arbeitsrechtspraxis der Zufriedenheitsstufe 'befriedigend' (Note 3)?",
            options = listOf(
                "A) '...hat die Aufgaben stets zu unserer vollsten Zufriedenheit erledigt.'",
                "B) '...hat die ihm übertragenen Aufgaben stets zu unserer vollen Zufriedenheit erledigt.'",
                "C) '...hat die Aufgaben zu unserer vollen Zufriedenheit erledigt.' (ohne 'stets').",
                "D) '...hat sich bemüht, die Aufgaben im Wesentlichen zu erfüllen.'"
            ),
            correctIndex = 2,
            explanation = "'stets zur vollsten Zufriedenheit' = Note 1; 'stets zur vollen Zufriedenheit' = Note 2; 'zur vollen Zufriedenheit' = Note 3; 'zur Zufriedenheit' = Note 4; 'bemüht' ohne Erfolg = Note 5/6."
        )
    )
}
