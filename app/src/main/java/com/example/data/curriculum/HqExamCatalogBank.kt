package com.example.data.curriculum

internal object HqExamCatalogBank {
    val questions: List<LearningDrillQuestion> = listOf(
        // ==========================================
        // HQ 1: Logistikprozesse (10 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "hq_logistik_q1",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Lean Management · Wertstromanalyse",
            prompt = "In Ihrer Montageversorgung liegen zwischen zwei Stationen Puffer für 4 Tage. Welche Lean-Maßnahme reduziert die Durchlaufzeit am nachhaltigsten?",
            options = listOf(
                "Zusätzliche Gabelstapler für schnelleren Puffertransport anschaffen",
                "Auf ein Pull-System (Kanban-Regelkreis) mit begrenztem Supermarktbestand umstellen",
                "Die Losgröße an der vorgelagerten Maschine verdoppeln"
            ),
            correctIndex = 1,
            explanation = "Größere Lose erhöhen Bestände und Durchlaufzeit (Push). Ein Kanban-Pull-System koppelt Nachschub direkt an den realen Verbrauch."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q2",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Distributionslogistik · Umschlag",
            prompt = "Was ist das Kernprinzip des Cross-Docking im Warenumschlag?",
            options = listOf(
                "Langfristige Einlagerung im Hochregallager zur Spekulation",
                "Bestandsloser Durchlaufumschlag: Ware wird ohne Einlagerung direkt vom Wareneingang auf den Warenausgang umgruppiert",
                "Vollständige Qualitäts-Laborprüfung jeder einzelnen Schraube"
            ),
            correctIndex = 1,
            explanation = "Cross-Docking minimiert Lagerhaltungskosten und Durchlaufzeiten, indem eingehende Lieferungen direkt kommissioniert und verladen werden."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q3",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Intralogistik · FTS / AGV",
            prompt = "Wann lohnt sich der Einsatz eines Fahrerlosen Transportsystems (FTS/AGV) gegenüber klassischen Gabelstaplern im Werkverkehr am ehesten?",
            options = listOf(
                "Bei täglich stark wechselnden Baustellen-Untergründen im Außenbereich",
                "Bei wiederkehrenden, standardisierten Transportrelationen im Mehrschichtbetrieb mit hohen Personalkosten",
                "Für einmalige Sondertransporte pro Quartal"
            ),
            correctIndex = 1,
            explanation = "FTS spielen ihre Wirtschaftlichkeit bei standardisierten, gut planbaren Punkt-zu-Punkt- oder Routenverkehren im 2- oder 3-Schicht-Betrieb durch konstante Verfügbarkeit aus."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q4",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Außenhandel · Incoterms 2020 (DAP vs. DDP)",
            prompt = "Sie exportieren Maschinen per Lkw mit der Incoterms-2020-Klausel DAP (benannter Bestimmungsort beim Kunden). Wann geht das Transportrisiko auf den Käufer über?",
            options = listOf(
                "Bereits bei Bereitstellung ab Werk des Verkäufers (EXW)",
                "Wenn die Ware am Bestimmungsort auf dem ankommenden Lkw entladebereit zur Verfügung gestellt wird (Einfuhrzoll zahlt Käufer)",
                "Erst nachdem der Verkäufer die Maschine im Werk des Käufers abgeladen und montiert hat"
            ),
            correctIndex = 1,
            explanation = "Bei DAP trägt der Verkäufer Kosten und Gefahr bis zur entladebereiten Bereitstellung am Bestimmungsort; Entladung und Einfuhrverzollung übernimmt der Käufer (bei DDP verzollt der Verkäufer auch)."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q5",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Kommissionierung · Ein- vs. Zweistufig",
            prompt = "Im E-Commerce-Lager gehen täglich 2.500 Kleinaufträge mit oft gleichen Schnelldreher-Artikeln ein. Welches Kommissionierverfahren senkt die Wegezeit radikal?",
            options = listOf(
                "Strenge Einzelauftrags-Kommissionierung mit Gabelstapler",
                "Zweistufige Sammelkommissionierung (Batch-Picking) mit anschließender Sortierung nach Kundenaufträgen",
                "Manuelle Listenkommissionierung ohne Scanner"
            ),
            correctIndex = 1,
            explanation = "Bei vielen Kleinteile-Aufträgen bündelt die zweistufige Kommissionierung identische Artikel in einem Sammelgang und verteilt sie anschließend (z. B. Put-to-Light)."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q6",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Lagertechnik · FIFO & Regalsysteme",
            prompt = "Welches Palettenregalsystem erzwingt bauartbedingt das FIFO-Prinzip (First-In, First-Out) für verderbliche oder chargenpflichtige Güter?",
            options = listOf(
                "Einfahrregal ohne Durchfahrt",
                "Paletten-Durchlaufregal (Rollenbahnen mit Schwerkraftgefälle zwischen Beschickungs- und Entnahmeseite)",
                "Blocklagerung direkt an einer Hallenwand"
            ),
            correctIndex = 1,
            explanation = "Durchlaufregale trennen Einlagerungs- und Entnahmeseite und sichern so striktes FIFO; Einfahr- oder Einschubregale arbeiten dagegen meist nach LIFO."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q7",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Lagerkennzahlen · Reichweite",
            prompt = "Der Jahreswareneinsatz beträgt 2.400.000 €, der durchschnittliche Lagerbestand liegt bei 400.000 €. Wie hoch sind Umschlagshäufigkeit und durchschnittliche Lagerdauer (360-Tage-Basis)?",
            options = listOf(
                "UH = 6,0 / Jahr · Lagerdauer = 60 Tage",
                "UH = 4,0 / Jahr · Lagerdauer = 90 Tage",
                "UH = 12,0 / Jahr · Lagerdauer = 30 Tage"
            ),
            correctIndex = 0,
            explanation = "UH = 2.400.000 / 400.000 = 6,0; durchschnittliche Lagerdauer = 360 Tage / 6,0 = 60 Tage."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q8",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Beschaffungslogistik · Milkrun & Konsignation",
            prompt = "Worin besteht der Vorteil eines Konsignationslagers beim produzierenden Abnehmer?",
            options = listOf(
                "Der Abnehmer zahlt alle Teile bereits ein Jahr im Voraus.",
                "Die Ware lagert bereits nahe der Produktion, bleibt aber im Eigentum des Lieferanten bis zur tatsächlichen Entnahme (geringere Kapitalbindung).",
                "Der Lieferant verzichtet auf jegliche Qualitätskontrolle."
            ),
            correctIndex = 1,
            explanation = "Beim Konsignationslager reduziert der Abnehmer seine Kapitalbindung und steigert zugleich die Versorgungssicherheit, da der Eigentumsübergang erst bei Entnahme erfolgt."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q9",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Auto-ID · GS1-128 & NVE (SSCC)",
            prompt = "Wozu dient eine 18-stellige NVE / SSCC (Serial Shipping Container Code) auf dem GS1-128 Transportetikett?",
            options = listOf(
                "Zur weltweit eindeutigen Identifikation einer spezifischen Versandeinheit (z. B. Palette) entlang der gesamten Lieferkette",
                "Als Ersatz für den Gefahrgut-Frachtbrief (ADR)",
                "Zur Berechnung der Lkw-Maut"
            ),
            correctIndex = 0,
            explanation = "Die NVE/SSCC ist das weltweit eindeutige 'Nummernschild' einer logistischen Ladeeinheit und ermöglicht lückenlose Sendungsverfolgung via EDI/DESADV."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q10",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Kommissionierzeit · Weg- vs. Greifzeit",
            prompt = "Welcher Teil der Kommissionierzeit macht in konventionellen 'Mann-zur-Ware'-Lagern typischerweise den größten Zeitanteil aus (> 50 %)?",
            options = listOf(
                "Basiszeit (Belege holen)",
                "Wegezeit (Zurücklegen der Strecken zwischen den Lagerplätzen)",
                "Totzeit beim Drucker"
            ),
            correctIndex = 1,
            explanation = "Bei statischer Bereitstellung entfallen oft 50–65 % auf Wegezeiten; ABC-Zonung (Schnelldreher in den Vorbereich) oder Ware-zur-Mann-Systeme senken genau diesen Block."
        ),

        // ==========================================
        // HQ 2: Betriebliche Organisation & Kostenwesen (10 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "hq_org_q1",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Qualitätsmanagement · FMEA",
            prompt = "Aus welchen drei Faktoren wird die Risikoprioritätszahl (RPZ) bei der klassischen FMEA berechnet?",
            options = listOf(
                "Kosten × Dauer × Häufigkeit",
                "Bedeutung (B) × Auftretenswahrscheinlichkeit (A) × Entdeckungswahrscheinlichkeit (E)",
                "Ausschuss × Nacharbeit × Durchlaufzeit"
            ),
            correctIndex = 1,
            explanation = "RPZ = B × A × E (jeweils Skala 1–10, max. 1000). Ab hohen Einzelwerten (insb. B) oder hoher RPZ sind Abstellmaßnahmen nötig."
        ),
        LearningDrillQuestion(
            id = "hq_org_q2",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Make-or-Buy Entscheidung",
            prompt = "Wann ist bei freien Eigenkapazitäten eine Eigenfertigung (Make) kurzfristig wirtschaftlicher als Fremdbezug (Buy)?",
            options = listOf(
                "Wenn die variablen Eigenkosten je Stück unter dem Fremdbezugspreis liegen",
                "Nur wenn die Gesamtvollkosten höher als der Marktpreis sind",
                "Sobald das Lager zu 100 % ausgelastet ist"
            ),
            correctIndex = 0,
            explanation = "Kurzfristig (bei freien Kapazitäten) sind Fixkosten bereits vorhanden; relevant sind die variablen Stückkosten im Vergleich zum Einkaufspreis."
        ),
        LearningDrillQuestion(
            id = "hq_org_q3",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Qualitätssicherung · Poka-Yoke",
            prompt = "Um Verwechslungen beim Einlegen von Kleinteilen in Versandbehälter technisch unmöglich zu machen, entwickeln Sie asymmetrische Aufnahmen. Wie heißt dieses Lean-Prinzip?",
            options = listOf(
                "Poka-Yoke (technische Vorkehrung zur sofortigen Fehlervermeidung)",
                "Andon-Board (reine Statusanzeige)",
                "Milkrun-Tour"
            ),
            correctIndex = 0,
            explanation = "Poka-Yoke bedeutet 'Fehlhandlung vermeiden': Durch mechanische oder sensorische Zwangsführungen können Flüchtigkeitsfehler gar nicht erst entstehen."
        ),
        LearningDrillQuestion(
            id = "hq_org_q4",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Gefahrgut · ADR Klassen & 1000-Punkte-Regel",
            prompt = "Auf einem Lkw sollen gemäß ADR-Kleinmengenregelung (1.1.3.6) 200 kg eines Gefahrguts der Beförderungskategorie 2 (Faktor 3) und 300 kg der Kategorie 3 (Faktor 1) geladen werden. Ist die Freigrenze eingehalten?",
            options = listOf(
                "Nein, 500 kg überschreiten immer jede Grenze.",
                "Ja: 200 × 3 + 300 × 1 = 900 Punkte (≤ 1.000 Punkte; somit ohne orangefarbene Warntafeln zulässig).",
                "Nur wenn ein Gefahrgutbeauftragter im Lkw mitfährt."
            ),
            correctIndex = 1,
            explanation = "Nach ADR 1.1.3.6 werden Mengen mit Kategoriefaktor (Kat. 1=50, Kat. 2=3, Kat. 3=1) multipliziert. Bis einschließlich 1.000 Punkte greifen Erleichterungen (u. a. keine Warntafelpflicht)."
        ),
        LearningDrillQuestion(
            id = "hq_org_q5",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Arbeitsschutz · § 22 SGB VII / DGUV V1",
            prompt = "Ab wie vielen regelmäßig Beschäftigten in einem Unternehmen muss gemäß § 22 SGB VII mindestens ein Sicherheitsbeauftragter bestellt werden — und welche Befugnis hat er?",
            options = listOf(
                "Ab 50 Beschäftigten; er darf Bußgelder gegen Kollegen verhängen.",
                "Ab mehr als 20 Beschäftigten; er unterstützt ehrenamtlich/beratend ohne Weisungs- oder Disziplinarbefugnis.",
                "Ab 5 Beschäftigten; er trägt die alleinige Unternehmerverantwortung."
            ),
            correctIndex = 1,
            explanation = "Ab > 20 Beschäftigten ist ein Sicherheitsbeauftragter Pflicht (§ 22 SGB VII). Er wirkt als Multiplikator vor Ort ohne Weisungsbefugnis."
        ),
        LearningDrillQuestion(
            id = "hq_org_q6",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "KLR · Gesamtkostenfunktion & Preisuntergrenze",
            prompt = "Ein Logistikzentrum hat 615.000 € monatliche Fixkosten und 660.000 € variable Gesamtkosten bei 60.000 Kommissionen. Wie lauten Kostenfunktion K(x) und kurzfristige Preisuntergrenze?",
            options = listOf(
                "K(x) = 615.000 + 11x · Preisuntergrenze = 11,00 €/Stk.",
                "K(x) = 660.000 + 10,25x · Preisuntergrenze = 21,25 €/Stk.",
                "K(x) = 615.000 + 21,25x · Preisuntergrenze = 10,25 €/Stk."
            ),
            correctIndex = 0,
            explanation = "kv = 660.000 € / 60.000 Stk. = 11,00 €/Stk. Damit gilt K(x) = 615.000 + 11x; die kurzfristige Preisuntergrenze entspricht kv = 11,00 €."
        ),
        LearningDrillQuestion(
            id = "hq_org_q7",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Kostenrechnung · Maschinenstundensatz",
            prompt = "Welche kalkulatorischen Kostenpositionen fließen typischerweise in den Maschinenstundensatz eines Schmalgangstaplers ein?",
            options = listOf(
                "Ausschließlich die reinen Stromkosten je Schicht",
                "Kalkulatorische Abschreibung (vom Wiederbeschaffungswert), kalk. Zinsen, Raum-, Energie- und Instandhaltungskosten dividiert durch Laufstunden",
                "Nur der Bruttolohn des Fahrers"
            ),
            correctIndex = 1,
            explanation = "Der Maschinenstundensatz erfasst alle maschinenabhängigen Fix- und variablen Gemeinkosten (Abschreibung auf WBW, Zinsen, Raum, Energie, Wartung) je produktiver Laufstunde."
        ),
        LearningDrillQuestion(
            id = "hq_org_q8",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Qualitätsmanagement · 8D-Report & PDCA",
            prompt = "Ein Großkunde reklamiert eine Falschlieferung. Welche Sofortmaßnahme steht im 8D-Report noch vor der eigentlichen Ursachenanalyse (D4) an Position D3?",
            options = listOf(
                "Schadensbegrenzende Sofortmaßnahme (Containment Action), z. B. Sperrung betroffener Lagerchargen und Ersatzlieferung",
                "Jahresabschlussprüfung durch das Controlling",
                "Kündigung des gesamten Schichtteams"
            ),
            correctIndex = 0,
            explanation = "Im 8D-Report schützt D3 den Kunden sofort durch Eindämmung/Sperrung fehlerhafter Bestände, bevor in D4–D6 die Grundursache abgestellt wird."
        ),
        LearningDrillQuestion(
            id = "hq_org_q9",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Prozesskostenrechnung · lmi vs. lmn",
            prompt = "Wodurch unterscheidet sich ein 'leistungsmengeninduzierter (lmi)' Prozess von einem 'leistungsmengenneutralen (lmn)' Prozess im Lager?",
            options = listOf(
                "lmi-Prozesse hängen direkt vom Auftragsvolumen ab (z. B. Wareneingang prüfen); lmn-Prozesse fallen unabhängig von der Menge an (z. B. Abteilung leiten).",
                "lmn-Prozesse verursachen grundsätzlich nur Einzelkosten.",
                "lmi-Prozesse gibt es nur in der Buchhaltung."
            ),
            correctIndex = 0,
            explanation = "Die Prozesskostenrechnung ordnet lmi-Kosten über Kostentreiber (z. B. Anzahl Paletten/Aufträge) verursachungsgerecht zu und legt lmn-Kosten prozentual um."
        ),
        LearningDrillQuestion(
            id = "hq_org_q10",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Gewässerschutz · AwSV Auffangwannen",
            prompt = "Wie groß muss das Rückhaltevolumen einer Auffangwanne unter Gefahrstoff-Fässern (ohne besondere Ausnahmen) mindestens sein?",
            options = listOf(
                "10 % des gesamten Lagervolumens, mindestens jedoch der Inhalt des größten Einzelgebindes",
                "Exakt 5 Liter je Regalmeter",
                "100 % des größten Gebindes genügen nur im Freien"
            ),
            correctIndex = 0,
            explanation = "Nach AwSV/TRGS 510 gilt als Grundregel: mindestens 10 % der Gesamtlagermenge, mindestens jedoch 100 % des größten einzelnen Behälters (in Wasserschutzgebieten 100 %)."
        ),

        // ==========================================
        // HQ 3: Führung und Personal (8 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "hq_fuehrung_q1",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "4-Stufen-Unterweisung · Kommissionierterminal",
            prompt = "Sie weisen einen neuen Mitarbeiter in die Bedienung des Kommissionierterminals ein. Was folgt in Stufe 2 der klassischen 4-Stufen-Methode?",
            options = listOf(
                "Der Mitarbeiter arbeitet sofort unbeaufsichtigt eine komplette Schicht allein.",
                "Vormachen und Erklären (Warum und Wie jedes Arbeitsschritts zeigen).",
                "Schriftlichen Abschlusstest ohne Praxisübung schreiben lassen."
            ),
            correctIndex = 1,
            explanation = "Die 4-Stufen-Methode: 1. Vorbereiten/Motivieren, 2. Vormachen & Erklären, 3. Nachmachen lassen & korrigieren, 4. Selbstständiges Üben & Kontrollieren."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q2",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Situatives Führen (Hersey/Blanchard)",
            prompt = "Welcher Führungsstil passt zu einem hoch motivierten, aber fachlich noch unerfahrenen Mitarbeiter (Reifegrad R2)?",
            options = listOf(
                "Delegieren (wenig Aufgabe, wenig Beziehung)",
                "Anweisen / Trainieren (klare Vorgaben plus unterstützendes Feedback)",
                "Laissez-faire ohne Rückmeldung"
            ),
            correctIndex = 1,
            explanation = "Bei hoher Motivation aber noch geringer Fachkompetenz kombiniert 'Selling/Coaching/Trainieren' klare Struktur mit Ermutigung."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q3",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Personalwesen · Zeugnissprache (§ 109 GewO)",
            prompt = "Welche Leistungsbeurteilung in einem qualifizierten Arbeitszeugnis entspricht nach gängiger Arbeitsrechtspraxis der Schulnote 'Befriedigend (Note 3)'?",
            options = listOf(
                "\"stets zu unserer vollsten Zufriedenheit\"",
                "\"zu unserer vollen Zufriedenheit\"",
                "\"stets zu unserer vollen Zufriedenheit\""
            ),
            correctIndex = 1,
            explanation = "'stets zu unserer vollsten' = Note 1; 'stets zu unserer vollen' = Note 2; 'zu unserer vollen Zufriedenheit' (ohne 'stets') = Note 3; 'zu unserer Zufriedenheit' = Note 4."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q4",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Personalbedarfsplanung",
            prompt = "Für den Warenausgang werden täglich 12 Arbeitsplätze (Einsatzbedarf) besetzt; durchschnittliche Ausfallquote (Urlaub, Krankheit, Schulung) beträgt 20 %. Wie hoch ist der Brutto-Personalbedarf?",
            options = listOf(
                "14,4 ≈ 15 Mitarbeiter",
                "12,2 ≈ 13 Mitarbeiter",
                "10 Mitarbeiter"
            ),
            correctIndex = 0,
            explanation = "Bruttopersonalbedarf = Einsatzbedarf × (1 + Verteil-/Ausfallzeitfaktor) = 12 × 1,20 = 14,4 -> aufgerundet 15 Mitarbeiter (inkl. Reservebedarf)."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q5",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Delegation · Führungsgrundsätze",
            prompt = "Welche Führungsaufgabe darf der Logistikmeister grundsätzlich NICHT an Schichtmitarbeiter delegieren?",
            options = listOf(
                "Bestandszählung von C-Artikeln im Kleinteilelager",
                "Disziplinarverantwortung (z. B. Abmahnungen), Gesamtverantwortung und verbindliche Zielvereinbarungen",
                "Erstellen der täglichen Verladecheckliste"
            ),
            correctIndex = 1,
            explanation = "Man kann Handlungsverantwortung für Aufgaben übertragen; die Führungs-, Auswahl-, Kontroll- und Gesamtverantwortung bleibt stets beim Meister."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q6",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Personalentwicklung · Arbeitsstrukturierung",
            prompt = "Ein Kommissionierer übernimmt zusätzlich zur Entnahme auch die eigenverantwortliche Qualitätsprüfung und Reklamationsfreigabe (vertikale Erweiterung). Wie heißt diese Maßnahme?",
            options = listOf(
                "Job Enlargement (Aufgabenvergrößerung auf gleicher Ebene)",
                "Job Enrichment (Aufgabenbereicherung mit höherer Verantwortung)",
                "Job Rotation (systematischer Arbeitsplatzwechsel)"
            ),
            correctIndex = 1,
            explanation = "Job Rotation = Arbeitsplatzwechsel; Job Enlargement = horizontale Erweiterung gleichartiger Tätigkeiten; Job Enrichment = vertikale Bereicherung mit Entscheidungsbefugnis."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q7",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Zielvereinbarung · SMART-Prinzip",
            prompt = "Wie lautet eine prüfungstaugliche SMART-Zielvereinbarung für die Reduktion von Pickfehlern im Kleinteilelager?",
            options = listOf(
                "Wir wollen im nächsten Jahr einfach deutlich sorgfältiger arbeiten.",
                "Die Pickfehlerquote im KTL wird bis zum 31.12. durch Doppel-Scan von derzeit 1,8 % auf unter 0,6 % gesenkt.",
                "Jeder Fehler wird ab sofort mit Gehaltskürzung geahndet."
            ),
            correctIndex = 1,
            explanation = "SMART steht für Spezifisch, Messbar (1,8 % -> 0,6 %), Akzeptiert/Attraktiv, Realistisch und Terminiert (bis 31.12.)."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q8",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Mitarbeiterbeurteilung · Wahrnehmungsfehler",
            prompt = "Was versteht man bei einer Mitarbeiterbeurteilung unter dem sogenannten 'Halo-Effekt' (Überstrahlungsfehler)?",
            options = listOf(
                "Eine einzelne stark auffällige Eigenschaft (z. B. Pünktlichkeit oder ein großer Fehler) überstrahlt das gesamte Urteil über alle anderen Kriterien.",
                "Der Vorgesetzte bewertet grundsätzlich nur die letzten 14 Tage vor dem Gespräch (Nikolaus-Effekt).",
                "Alle Mitarbeiter erhalten exakt die mittlere Punktzahl (Tendenz zur Mitte)."
            ),
            correctIndex = 0,
            explanation = "Beurteilungsfehler (Halo, Nikolaus-Effekt, Tendenz zur Mitte, Strenge/Milde) lassen sich durch strukturierte Anforderungsprofile und Verhaltensbeobachtung im ganzen Jahr vermeiden."
        ),

        // ==========================================
        // SA: Situationsaufgabe & Fachgespräch (6 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "sa_q1",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "SIHK-Fachgespräch · Zeitbudget",
            prompt = "Wie viel Vorbereitungszeit steht Ihnen vor dem mündlichen SIHK-Fachgespräch regulär für die Gliederung Ihrer Präsentation zur Verfügung?",
            options = listOf(
                "10 Minuten ohne Hilfsmittel",
                "30 Minuten Vorbereitungszeit auf eine handlungsorientierte Aufgabenstellung",
                "120 Minuten Gruppenarbeit"
            ),
            correctIndex = 1,
            explanation = "In der Regel haben Prüflinge 30 Minuten Vorbereitungszeit (z. B. Flipchart/Moderationskarten), gefolgt von ca. 10 Min. Präsentation und 20 Min. Fachgespräch."
        ),
        LearningDrillQuestion(
            id = "sa_q2",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "SIHK-Präsentation · Roter Faden",
            prompt = "Welche Reihenfolge überzeugt den SIHK-Prüfungsausschuss in der 10-minütigen Fachgesprächs-Präsentation am meisten?",
            options = listOf(
                "Ohne Gliederung sofort technische Details der Regale aufzählen",
                "1. Ausgangslage & Problem -> 2. SMART-Ziele -> 3. Lösungsalternativen inkl. KLR -> 4. Empfehlung & Maßnahmenplan (Personal/Arbeitsschutz)",
                "Nur gesetzliche Paragrafen vorlesen"
            ),
            correctIndex = 1,
            explanation = "Der Ausschuss bewertet neben Fachwissen vor allem den praxisnahen roten Faden eines Logistikmeisters: Problem -> Ziel -> wirtschaftlicher Vergleich -> konkrete Umsetzung mit dem Team."
        ),
        LearningDrillQuestion(
            id = "sa_q3",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "IHK-Notenschlüssel (100 Punkte)",
            prompt = "Ab welcher Punktzahl ist eine schriftliche Situationsaufgabe oder das mündliche Fachgespräch nach bundeseinheitlichem IHK-Schlüssel bestanden (Note 4), und ab wann gibt es Note 2?",
            options = listOf(
                "Bestanden ab 50 Punkten (Note 4) · Note 2 ab 81 Punkten",
                "Bestanden ab 60 Punkten (Note 4) · Note 2 ab 85 Punkten",
                "Bestanden ab 45 Punkten (Note 4) · Note 2 ab 75 Punkten"
            ),
            correctIndex = 0,
            explanation = "Offizieller IHK-Schlüssel: 92–100 = 1; 81–91 = 2; 67–80 = 3; 50–66 = 4 (bestanden); 30–49 = 5; 0–29 = 6."
        ),
        LearningDrillQuestion(
            id = "sa_q4",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Prüfungs-Operatoren richtig deuten",
            prompt = "In einer HQ-Situationsaufgabe lautet der Operator 'Beurteilen Sie die Einführung von FTS'. Was erwartet der Korrektor zusätzlich zu reinen Fakten?",
            options = listOf(
                "Nur eine Stichwortliste mit 3 Fachbegriffen",
                "Eine abwägende Gegenüberstellung von Vor-/Nachteilen inkl. Kriterien (Kosten, Sicherheit, Personal) und einem begründeten Gesamturteil",
                "Ausschließlich eine Zeichnung der Lagerhalle"
            ),
            correctIndex = 1,
            explanation = "'Nennen' = kurze Stichworte; 'Erläutern/Beschreiben' = Zusammenhänge erklären; 'Beurteilen/Entscheiden' = abwägende Bewertung und klares Fazit."
        ),
        LearningDrillQuestion(
            id = "sa_q5",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Ausschuss-Rückfrage · Widerstand im Team",
            prompt = "Der Prüfungsausschuss hakt nach: 'Was machen Sie, wenn erfahrene Staplerfahrer das neue WMS ablehnen und verweigern?' Wie antworten Sie meistergerecht?",
            options = listOf(
                "Ich stelle allen sofort eine fristlose Kündigung in Aussicht.",
                "Frühzeitig einbinden (Key-User benennen), Ängste vor Überforderung ernst nehmen, praxisnah schulen und im Zweifel klare Verbindlichkeit über Zielgespräche herstellen.",
                "Ich schaffe das WMS auf eigene Faust wieder ab."
            ),
            correctIndex = 1,
            explanation = "Im Change-Management kombiniert der Meister Partizipation, Qualifizierung und klare Führung; Datenschutz-/Leistungskontroll-Themen (BetrVG § 87 Abs. 1 Nr. 6) werden vorab mit dem BR geklärt."
        ),
        LearningDrillQuestion(
            id = "sa_q6",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Situationsaufgabe · Hilfsmittel & Teilpunkte",
            prompt = "Sie bemerken bei einer umfangreichen KLR-Rechenaufgabe in der Prüfung Zeitdruck. Welche Strategie rettet wertvolle IHK-Punkte?",
            options = listOf(
                "Nur das geschätzte Endergebnis ohne Zwischenschritte hinschreiben",
                "Formel, gegebene Werte (mit Einheiten) und klaren Rechenansatz sauber notieren, damit auch bei Flüchtigkeitsfehlern volle Teilpunkte vergeben werden können",
                "Die Aufgabe komplett durchstreichen"
            ),
            correctIndex = 1,
            explanation = "IHK-Korrektoren vergeben gezielt Teilpunkte für korrekten Ansatz, richtige Formelwahl und saubere Einheitenumrechnung — auch bei einem Folgefehler."
        )
    )
}
