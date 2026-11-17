package com.example.data.curriculum

internal object BqExamCatalogBank {
    val questions: List<LearningDrillQuestion> = listOf(
        // ==========================================
        // BQ 1: Rechtsbewusstes Handeln (8 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "bq_recht_q1",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "BetrVG § 87 · Schichtübergabe Lager Süd",
            prompt = "Schichtleiter, wegen Auftragsspitze muss unser Spätschicht-Team am Samstag 4 Stunden Überstunden fahren. Dürfen Sie das einfach anordnen?",
            options = listOf(
                "Ja, kraft Direktionsrecht (§ 106 GewO) darf ich Überstunden allein anordnen.",
                "Nein, nach § 87 Abs. 1 Nr. 3 BetrVG hat der Betriebsrat bei vorübergehender Verlängerung der Arbeitszeit zwingend mitzubestimmen.",
                "Nur wenn die Mitarbeiter mündlich zustimmen, entfällt jede Beteiligung."
            ),
            correctIndex = 1,
            explanation = "§ 87 Abs. 1 Nr. 3 BetrVG regelt die erzwingbare Mitbestimmung des Betriebsrats bei vorübergehender Verkürzung oder Verlängerung der betriebsüblichen Arbeitszeit."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q2",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Arbeitsschutzgesetz (§ 5 ArbSchG)",
            prompt = "Wann muss eine Gefährdungsbeurteilung (§ 5 ArbSchG) im Wareneingang zwingend aktualisiert werden?",
            options = listOf(
                "Nur nach behördlicher Anordnung durch die Berufsgenossenschaft",
                "Bei Einführung neuer Arbeitsmittel, neuer Gefahrstoffe oder nach Arbeitsunfällen",
                "Ausschließlich alle 5 Jahre durch den Betriebsarzt"
            ),
            correctIndex = 1,
            explanation = "Nach § 5 ArbSchG und BetrSichV ist die Gefährdungsbeurteilung anlassbezogen bei neuen Arbeitsmitteln, Verfahren oder nach Beinahe-/Arbeitsunfällen fortzuschreiben."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q3",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Handelsrecht · §§ 49, 54 HGB",
            prompt = "Welches Rechtsgeschäft darf ein Prokurist (§ 49 HGB) im Gegensatz zur Handlungsvollmacht NICHT ohne besondere Befugnis durchführen?",
            options = listOf(
                "Einstellung neuer Lagerfachkräfte und Kauf eines neuen Gabelstaplers",
                "Veräußerung oder Belastung von Betriebsgrundstücken sowie Aufnahme neuer Gesellschafter",
                "Abschluss von Rahmenverträgen mit Speditionen"
            ),
            correctIndex = 1,
            explanation = "Nach § 49 Abs. 2 HGB ermächtigt die Prokura nicht zur Veräußerung oder Belastung von Grundstücken (Immobilienklausel) und nicht zu Grundlagengeschäften."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q4",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Kündigungsschutzgesetz (KSchG)",
            prompt = "Welche vier gesetzlichen Kriterien müssen bei einer betriebsbedingten Kündigung im Rahmen der Sozialauswahl (§ 1 Abs. 3 KSchG) zwingend berücksichtigt werden?",
            options = listOf(
                "Betriebszugehörigkeit, Lebensalter, Unterhaltspflichten und Schwerbehinderung",
                "Fehlzeitenquote, Teamfähigkeit, Gehaltsklasse und Entfernung zum Wohnort",
                "Ausbildungsabschluss, Überstundenkonto, Familienstand und Alter der Kinder"
            ),
            correctIndex = 0,
            explanation = "Gemäß § 1 Abs. 3 KSchG sind genau vier soziale Gesichtspunkte abzuwägen: Dauer der Betriebszugehörigkeit, Lebensalter, Unterhaltspflichten und Schwerbehinderung des Arbeitnehmers."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q5",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Arbeitszeitgesetz (§§ 3, 5 ArbZG)",
            prompt = "Welche tägliche Höchstarbeitszeit und welche ununterbrochene Ruhezeit nach Schichtende schreibt das ArbZG grundsätzlich vor?",
            options = listOf(
                "Max. 8 Stunden (ausnahmsweise 10 Stunden bei Ausgleich innerhalb von 6 Monaten) und mind. 11 Stunden Ruhezeit",
                "Max. 12 Stunden täglich und mind. 8 Stunden Ruhezeit zwischen zwei Schichten",
                "Max. 9 Stunden täglich ohne Ausgleichspflicht und mind. 9 Stunden Ruhezeit"
            ),
            correctIndex = 0,
            explanation = "§ 3 ArbZG erlaubt regulär 8 h (verlängerbar auf bis zu 10 h bei 6-Monats-Ausgleich); § 5 Abs. 1 ArbZG schreibt mind. 11 Stunden ununterbrochene Ruhezeit vor."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q6",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "BGB / HGB · Wareneingang & Mängelrüge",
            prompt = "Bei einer B2B-Lieferung eines Großhändlers fällt erst nach 10 Tagen auf, dass 20 Kartons offensichtlich äußerlich durchnässt angeliefert wurden. Wie ist die Rechtslage nach § 377 HGB?",
            options = listOf(
                "Der Lieferant haftet volle 2 Jahre ohne jede Rügefrist.",
                "Offene Mängel müssen beim beiderseitigen Handelskauf unverzüglich gerügt werden; sonst gilt die Ware als genehmigt.",
                "Mängelansprüche verjähren im Handelsrecht bereits nach 48 Stunden."
            ),
            correctIndex = 1,
            explanation = "Nach § 377 HGB (Untersuchungs- und Rügepflicht) gilt die Ware bei einem beiderseitigen Handelskauf als genehmigt, wenn erkennbare Mängel nicht unverzüglich nach Ablieferung angezeigt werden."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q7",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Umweltrecht · KrWG & WHG/AwSV",
            prompt = "Welche 5-stufige Abfallhierarchie gilt verbindlich nach § 6 Kreislaufwirtschaftsgesetz (KrWG) im Betrieb?",
            options = listOf(
                "Beseitigung -> Verbrennung -> Recycling -> Wiederverwendung -> Vermeidung",
                "Vermeidung -> Vorbereitung zur Wiederverwendung -> Recycling -> sonstige (z. B. energetische) Verwertung -> Beseitigung",
                "Sortierung -> Zwischenlagerung -> Deponierung -> Export"
            ),
            correctIndex = 1,
            explanation = "Oberstes Gebot des § 6 KrWG ist die Abfallvermeidung, gefolgt von Wiederverwendung, Recycling, sonstiger Verwertung und erst zuletzt der Beseitigung."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q8",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Sozialversicherungsrecht · 5 Säulen",
            prompt = "Welcher Zweig der deutschen Sozialversicherung wird allein vom Arbeitgeber über Umlagen an die Berufsgenossenschaft finanziert?",
            options = listOf(
                "Gesetzliche Krankenversicherung (GKV)",
                "Gesetzliche Unfallversicherung (GUV / Berufsgenossenschaft)",
                "Arbeitslosenversicherung (ALV)"
            ),
            correctIndex = 1,
            explanation = "Während KV, RV, PV und AV grundsätzlich paritätisch (bzw. mit Zusatzbeitrag) geteilt werden, trägt der Arbeitgeber die Beiträge zur gesetzlichen Unfallversicherung zu 100 % allein."
        ),

        // ==========================================
        // BQ 2: Betriebswirtschaftliches Handeln (8 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "bq_bwl_q1",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Kosten- und Leistungsrechnung · Break-Even",
            prompt = "Ein Logistikdienstleister erzielt 120 € Erlös je Palette bei 75 € variablen Kosten und 90.000 € Fixkosten/Monat. Wo liegt der Break-Even-Punkt?",
            options = listOf(
                "1.200 Paletten",
                "2.000 Paletten",
                "750 Paletten"
            ),
            correctIndex = 1,
            explanation = "Deckungsbeitrag db = 120 € − 75 € = 45 €. Break-Even = 90.000 € / 45 € = 2.000 Paletten."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q2",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "BAB & Zuschlagskalkulation",
            prompt = "Welche Kostenart wird im Betriebsabrechnungsbogen (BAB) über Schlüsselgrößen (z. B. m² oder kWh) auf Kostenstellen verteilt?",
            options = listOf(
                "Fertigungseinzelkosten",
                "Kostenstellengemeinkosten",
                "Sondereinzelkosten des Vertriebs"
            ),
            correctIndex = 1,
            explanation = "Einzelkosten sind Kostenträgern direkt zurechenbar; Kostenstellengemeinkosten werden über Verteilungsschlüssel (Fläche, Köpfe, kWh) umgelegt."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q3",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Investitionsrechnung · Statische Amortisation",
            prompt = "Ein neuer Elektrostapler kostet 48.000 € (Restwert 0 €) und erzielt jährlich 10.000 € Gewinn nach Abschreibung plus 6.000 € lineare Abschreibung. Wie hoch ist die Amortisationsdauer?",
            options = listOf(
                "4,8 Jahre",
                "3,0 Jahre",
                "8,0 Jahre"
            ),
            correctIndex = 1,
            explanation = "Rückfluss (Cashflow) = Gewinn + Abschreibung = 10.000 € + 6.000 € = 16.000 €/Jahr. Amortisationsdauer = 48.000 € / 16.000 € = 3,0 Jahre."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q4",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Teilkostenrechnung · Preisuntergrenze",
            prompt = "Wann darf ein Logistikunternehmen einen Zusatzauftrag bei freien Kapazitäten unterhalb der Selbstkosten, aber oberhalb der variablen Stückkosten annehmen?",
            options = listOf(
                "Nie, jeder Auftrag unter Vollkosten führt sofort zur Insolvenz.",
                "Kurzfristig ja, weil jeder positive Deckungsbeitrag (Preis > kv) hilft, die ohnehin vorhandenen Fixkosten zu decken.",
                "Nur wenn die Fixkosten pro Stück ebenfalls sinken."
            ),
            correctIndex = 1,
            explanation = "Die variablen Stückkosten (kv) bilden die kurzfristige Preisuntergrenze. Jeder Euro darüber leistet einen positiven Deckungsbeitrag zur Fixkostendeckung."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q5",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Controlling · Return on Investment (ROI)",
            prompt = "Ein Lagerstandort erzielt 180.000 € Gewinn bei 3.000.000 € Umsatz und einem Gesamtkapital von 1.500.000 €. Wie hoch ist der ROI?",
            options = listOf(
                "6,0 %",
                "12,0 %",
                "20,0 %"
            ),
            correctIndex = 1,
            explanation = "Umsatzrentabilität = 180.000 / 3.000.000 = 6 %; Kapitalumschlag = 3.000.000 / 1.500.000 = 2. ROI = 6 % × 2 = 12 % (oder direkt Gewinn / Gesamtkapital)."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q6",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Bilanzierung & Liquidität",
            prompt = "Was besagt die 'Goldene Bilanzregel' im engeren Sinne für die Finanzierung eines neuen Hochregallagers?",
            options = listOf(
                "Langfristiges Anlagevermögen sollte langfristig (durch Eigenkapital + langfristiges Fremdkapital) finanziert sein.",
                "Kurzfristige Lieferantenkredite sollten stets höher als das Eigenkapital sein.",
                "Das Umlaufvermögen darf 10 % der Bilanzsumme nicht überschreiten."
            ),
            correctIndex = 0,
            explanation = "Fristenkongruenz: Langfristig gebundenes Anlagevermögen (Hallen, Regale, Fördertechnik) muss durch langfristig verfügbares Kapital gedeckt sein (Anlagendeckungsgrad ≥ 100 %)."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q7",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Bezugskalkulation · Einkauf",
            prompt = "Listeneinkaufspreis 1.000 €, 10 % Rabatt, 2 % Skonto und 38 € Bezugskosten (Fracht). Wie hoch ist der Einstandspreis?",
            options = listOf(
                "880,00 €",
                "920,00 €",
                "918,00 €"
            ),
            correctIndex = 1,
            explanation = "Zieleinkaufspreis = 1.000 × 0,90 = 900 €; Bareinkaufspreis = 900 × 0,98 = 882 €; Einstandspreis = 882 € + 38 € Bezugskosten = 920,00 €."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q8",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Wirtschaftlichkeit & Produktivität",
            prompt = "Worin unterscheidet sich die Kennzahl 'Produktivität' von der 'Wirtschaftlichkeit'?",
            options = listOf(
                "Produktivität = Output-Menge / Input-Menge (mengenmäßig); Wirtschaftlichkeit = Ertrag / Aufwand (wertmäßig in €).",
                "Beide Kennzahlen bezeichnen exakt dasselbe Verhältnis in Euro.",
                "Produktivität misst nur den Gewinn vor Steuern."
            ),
            correctIndex = 0,
            explanation = "Produktivität betrachtet Mengengrößen (z. B. Picks je Arbeitsstunde), Wirtschaftlichkeit bewertet in Geldeinheiten (Leistung / Kosten ≥ 1)."
        ),

        // ==========================================
        // BQ 3: MIKP (8 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "bq_mikp_q1",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Materialwirtschaft · ABC/XYZ-Analyse",
            prompt = "Welche Artikelklasse eignet sich optimal für Just-in-Time (JIT) bzw. direkte Verbrauchssynchronisation?",
            options = listOf(
                "AX-Güter (hoher Wertanteil, konstanter Verbrauch)",
                "CZ-Güter (geringer Wertanteil, sporadischer Verbrauch)",
                "BZ-Güter (mittlerer Wert, stark schwankend)"
            ),
            correctIndex = 0,
            explanation = "AX-Artikel kombinieren hohe Kapitalbindung (A) mit hoher Vorhersagegenauigkeit (X) — ideal für JIT/JIS und geringe Sicherheitsbestände."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q2",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Projektmanagement · Netzplantechnik",
            prompt = "Was kennzeichnet einen Vorgang auf dem kritischen Pfad im Netzplan?",
            options = listOf(
                "Er besitzt den größten freien Puffer (FP)",
                "Gesamtpuffer (GP) = 0; jede Verzögerung verschiebt das Projektende",
                "Er darf erst nach Projektabschluss dokumentiert werden"
            ),
            correctIndex = 1,
            explanation = "Auf dem kritischen Pfad gilt GP = 0 (und damit auch FP = 0). Jede Terminüberschreitung verlängert unmittelbar die Gesamtdauer."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q3",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "KVP-Workshop · Ursachenanalyse",
            prompt = "Im Versand häufen sich Transportschäden. Sie möchten im Team alle möglichen Fehlerquellen nach Mensch, Maschine, Material, Methode, Mitwelt und Messung strukturieren. Welches Tool nutzen Sie?",
            options = listOf(
                "Ishikawa-Diagramm (Fischgräten- / Ursache-Wirkungs-Diagramm)",
                "ABC-Analyse nach Jahresverbrauchswert",
                "Gantt-Balkendiagramm für die Schichtplanung"
            ),
            correctIndex = 0,
            explanation = "Das Ishikawa-Diagramm ordnet potenzielle Ursachen eines Qualitätsproblems systematisch den 6M-Kategorien (Mensch, Maschine, Material, Methode, Mitwelt, Messung) zu."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q4",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Qualitätsanalyse · Pareto-Prinzip",
            prompt = "Was zeigt ein Pareto-Diagramm (80/20-Regel) bei der Auswertung von Kommissionierfehlern?",
            options = listOf(
                "Dass ca. 80 % der Fehler auf wenige (ca. 20 %) Hauptursachen zurückgehen, die priorisiert behoben werden sollten.",
                "Dass exakt 80 % aller Mitarbeiter geschult werden müssen.",
                "Dass alle Fehler gleich häufig auftreten."
            ),
            correctIndex = 0,
            explanation = "Das Pareto-Diagramm sortiert Fehlerursachen absteigend nach Häufigkeit/Kosten und bildet eine Summenkurve, um die 'vital few' (wesentlichen Ursachen) sofort zu erkennen."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q5",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Strategische Planung · SWOT-Analyse",
            prompt = "Welche Kombination betrachtet die SWOT-Analyse?",
            options = listOf(
                "Interne Stärken/Schwächen (Strengths/Weaknesses) und externe Chancen/Risiken (Opportunities/Threats)",
                "Nur interne Maschinenkapazitäten und Lagerflächen",
                "Ausschließlich Wettbewerberpreise der letzten 5 Jahre"
            ),
            correctIndex = 0,
            explanation = "SWOT verbindet die interne Unternehmensanalyse (Stärken/Schwächen) mit der externen Umweltanalyse (Chancen/Risiken) zur Ableitung von Strategien."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q6",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Kreativitätstechnik · Methode 6-3-5",
            prompt = "Wie läuft die schriftliche Kreativitätstechnik 'Methode 6-3-5' im Meister-Workshop ab?",
            options = listOf(
                "6 Teilnehmer schreiben jeweils 3 Ideen auf und reichen das Blatt 5-mal weiter zum Ergänzen.",
                "6 Teams diskutieren 3 Stunden über 5 Maßnahmen ohne Papier.",
                "5 Vorgesetzte bewerten 63 Ideen eines externen Beraters."
            ),
            correctIndex = 0,
            explanation = "Methode 6-3-5: 6 Personen, je 3 Ideen pro Runde, 5-maliges Weiterreichen ergibt systematisch bis zu 108 Ideen ohne Dominanz einzelner Vielredner."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q7",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Zeitmanagement · Eisenhower-Matrix",
            prompt = "Wie sollten Aufgaben in der Eisenhower-Matrix behandelt werden, die 'wichtig, aber nicht dringend' sind (B-Aufgaben, z. B. präventive Wartungsplanung)?",
            options = listOf(
                "Sofort im Papierkorb entsorgen",
                "Selbst terminieren und fest im Kalender einplanen, bevor sie zu Krisen werden",
                "Ohne Kontrolle an Praktikanten abgeben"
            ),
            correctIndex = 1,
            explanation = "Eisenhower-Regel: A (wichtig + dringend) sofort selbst erledigen; B (wichtig + nicht dringend) fest terminieren; C (dringend + unwichtig) delegieren; D (unwichtig + nicht dringend) streichen."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q8",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Logistik-IT · ERP vs. WMS (LVS)",
            prompt = "Worin liegt die typische Aufgabenteilung zwischen ERP-System (z. B. SAP) und Warehouse Management System (WMS/LVS)?",
            options = listOf(
                "Das ERP führt kaufmännische Bestellungen, Kundenaufträge und Buchhaltung; das WMS steuert Lagerplätze, Ein-/Auslagerungsstrategien und Fördertechnik in Echtzeit.",
                "Das WMS übernimmt die Lohnabrechnung aller Lkw-Fahrer.",
                "Beide Systeme ersetzen vollständig jede physische Inventur."
            ),
            correctIndex = 0,
            explanation = "ERP verantwortet betriebswirtschaftliche Ebene und Gesamtbestände; das WMS optimiert operativ Lagerplätze (Chaotische Lagerung), Kommissionierwege und Stapler-Leitsysteme."
        ),

        // ==========================================
        // BQ 4: Zusammenarbeit im Betrieb (8 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "bq_zib_q1",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Mitarbeitergespräch · Kommissionierfehler",
            prompt = "Ein erfahrener Kommissionierer macht seit zwei Wochen auffällig viele Pickfehler. Wie eröffnen Sie als Logistikmeister das Kritikgespräch professionell?",
            options = listOf(
                "Vor der gesamten Frühschicht eine sofortige Abmahnung androhen.",
                "Unter vier Augen starten, konkrete Fakten schildern (Ich-Botschaft) und gemeinsam nach Ursachen wie Scan-Technik oder Ergonomie fragen.",
                "Die Fehlerquote stillschweigend dem Teamleiter anlasten."
            ),
            correctIndex = 1,
            explanation = "Konstruktive Kritik erfolgt zeitnah, unter vier Augen, sachlich anhand konkreter Beobachtungen mit aktivem Zuhören (Ursachenforschung statt Vorwurf)."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q2",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Gruppendynamik nach Tuckman",
            prompt = "In welcher Tuckman-Phase treten typischerweise Rollenkonflikte und Machtkämpfe innerhalb eines neu zusammengestellten Projektteams auf?",
            options = listOf(
                "Forming",
                "Storming",
                "Performing"
            ),
            correctIndex = 1,
            explanation = "Im 'Storming' prallen unterschiedliche Erwartungen und Rollenansprüche aufeinander, bevor im 'Norming' verbindliche Spielregeln entstehen."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q3",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Kommunikation · Schulz von Thun",
            prompt = "Der Meister sagt zum Staplerfahrer: 'An Rampe 4 stehen noch drei ungesicherte Paletten!' Der Fahrer antwortet gereizt: 'Ich arbeite doch schon so schnell ich kann!' Auf welchem Ohr hat der Fahrer primär gehört?",
            options = listOf(
                "Auf dem reinen Sachohr",
                "Auf dem Beziehungs- bzw. Appellohr (Vorwurf / Arbeitsdruck)",
                "Auf dem Selbstoffenbarungsohr"
            ),
            correctIndex = 1,
            explanation = "Im 4-Seiten-Modell (Sachinhalt, Selbstoffenbarung, Beziehung, Appell) führen Störungen meist über ein zu stark aktiviertes Beziehungs- oder Appellohr ('Du bist zu langsam')."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q4",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Motivation · Herzberg Zwei-Faktoren-Theorie",
            prompt = "Was bewirken laut Herzberg sogenannte 'Hygienefaktoren' (z. B. Gehalt, Pausenraum, Arbeitssicherheit) im Gegensatz zu 'Motivatoren'?",
            options = listOf(
                "Ihr Fehlen erzeugt Unzufriedenheit; ihr Vorhandensein verhindert Unzufriedenheit, motiviert aber langfristig nicht allein.",
                "Sie steigern automatisch die Leistungsbereitschaft um 50 %.",
                "Sie sind identisch mit Anerkennung und Verantwortung."
            ),
            correctIndex = 0,
            explanation = "Nach Herzberg beseitigen Hygienefaktoren nur Unzufriedenheit (Basis); echte Arbeitsmotivation entsteht durch Motivatoren wie Verantwortung, Anerkennung und Entwicklung."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q5",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Bedürfnispyramide nach Maslow",
            prompt = "In welcher Reihenfolge von unten nach oben ordnet Maslow die fünf menschlichen Bedürfnis-Stufen?",
            options = listOf(
                "Physiologische Bedürfnisse -> Sicherheit -> Soziale Zugehörigkeit -> Wertschätzung -> Selbstverwirklichung",
                "Wertschätzung -> Sicherheit -> Physiologische Bedürfnisse -> Selbstverwirklichung -> Soziales",
                "Selbstverwirklichung -> Wertschätzung -> Sicherheit -> Physiologie -> Soziales"
            ),
            correctIndex = 0,
            explanation = "Die ersten vier Stufen (Physiologie, Sicherheit, Soziales, Wertschätzung) sind Defizitbedürfnisse; die Spitze bildet das Wachstumsbedürfnis Selbstverwirklichung."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q6",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Konfliktmanagement · Deeskalation",
            prompt = "Zwischen Früh- und Spätschicht eskaliert der Streit um unaufgeräumte Übergabeflächen. Was ist der erste konstruktive Schritt im Moderationsgespräch nach Glasl?",
            options = listOf(
                "Beide Parteien getrennt halten und einseitige Strafen verhängen.",
                "Beide Sichtweisen wertfrei anhören, gemeinsame Interessen (sichere Übergabe) herausarbeiten und verbindliche 5S-Übergabestandards vereinbaren.",
                "Den Konflikt ignorieren, bis er von selbst verschwindet."
            ),
            correctIndex = 1,
            explanation = "Win-Win-Konfliktlösung trennt Mensch und Problem (Harvard-Konzept), macht wechselseitige Belastungen transparent und führt zu klaren, messbaren Übergaberegeln."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q7",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Führungsstile nach Kurt Lewin",
            prompt = "Worin liegt das Hauptrisiko des 'Laissez-faire'-Führungsstils in einem sicherheitskritischen Umschlaglager?",
            options = listOf(
                "Mitarbeiter erhalten zu viele klare Zielvorgaben.",
                "Fehlende Orientierung, unklare Zuständigkeiten und erhöhte Gefahr von Sicherheitsverstößen sowie informellen Machtkämpfen.",
                "Zu hohe Entscheidungsgeschwindigkeit in Notfällen."
            ),
            correctIndex = 1,
            explanation = "Beim Laissez-faire-Stil hält sich die Führungskraft fast vollständig zurück; im Lagerbetrieb führt dies leicht zu Koordinationsverlust und Sicherheitsrisiken."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q8",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Gruppenarbeit & KVP",
            prompt = "Welche Gefahr bezeichnet der Begriff 'Groupthink' (Gruppendenken) in einem KVP-Qualitätszirkel?",
            options = listOf(
                "Übermäßiges Streben nach Einmütigkeit unterdrückt kritische Einwände und verhindert bessere Alternativen.",
                "Das Team arbeitet zu schnell und erzielt zu viele Einsparungen.",
                "Die Gruppenmitglieder nutzen zu viele Kennzahlen."
            ),
            correctIndex = 0,
            explanation = "Um 'Groupthink' vorzubeugen, sollte der Logistikmeister bewusst Gegenargumente einholen, offene Fragen stellen und kritische Stimmen schützen."
        ),

        // ==========================================
        // BQ 5: NTG (8 Fragen)
        // ==========================================
        LearningDrillQuestion(
            id = "bq_ntg_q1",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Ladungssicherung · VDI 2700 / EN 12195-1",
            prompt = "Welcher Massenanteil der Ladung muss gemäß VDI 2700 / EN 12195-1 in Fahrtrichtung (nach vorn) gesichert werden?",
            options = listOf(
                "50 % der Gewichtskraft (0,5 × F_G)",
                "80 % der Gewichtskraft (0,8 × F_G)",
                "30 % der Gewichtskraft (0,3 × F_G)"
            ),
            correctIndex = 1,
            explanation = "In Fahrtrichtung (Bremsung) wirkt eine Massenkraft von 0,8 × F_G (80 %), seitlich und nach hinten jeweils 0,5 × F_G (50 %)."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q2",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Gefahrstoffe · TRGS 510",
            prompt = "Worauf weist die Sicherheitsunterweisung bei gemeinsamer Lagerung von brennbaren Flüssigkeiten und Oxidationsmitteln (TRGS 510) hin?",
            options = listOf(
                "Zusammenlagerungsverbot beachten und bauliche/räumliche Trennung einhalten",
                "Beide Stoffe dürfen im selben Auffangbecken gemischt gelagert werden",
                "TRGS 510 gilt nur für Transport auf öffentlichen Straßen (ADR)"
            ),
            correctIndex = 0,
            explanation = "Die TRGS 510 regelt die Lagerung von Gefahrstoffen in ortsbeweglichen Behältern inkl. strenger Zusammenlagerungsverbote nach Lagerklassen."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q3",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Mechanik · Hebelgesetz & Gabelstapler",
            prompt = "Ein Gabelstapler hat eine Nenntragfähigkeit von 2.000 kg bei 500 mm Lastschwerpunktabstand (LSP). Was passiert, wenn eine 2.000-kg-Palette mit 800 mm LSP aufgenommen wird?",
            options = listOf(
                "Der Stapler bleibt unverändert standsicher.",
                "Das Lastmoment steigt über das zulässige Gegenmoment — der Stapler droht nach vorn über die Vorderachse zu kippen.",
                "Die Hubgeschwindigkeit verdoppelt sich automatisch."
            ),
            correctIndex = 1,
            explanation = "Nach dem Hebelgesetz (M = F × l) vergrößert ein höherer Lastschwerpunktabstand den Hebelarm zur Vorderachse (Drehpunkt), weshalb das Traglastdiagramm drastisch abnimmt."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q4",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Reibung · Antirutschmatten (ARM)",
            prompt = "Durch den Einsatz von Antirutschmatten erhöht sich der Gleitreibbeiwert μ von 0,20 (Holz auf Siebdruck) auf 0,60. Welche Konsequenz hat das beim Niederzurren nach VDI 2700?",
            options = listOf(
                "Es werden deutlich mehr Zurrgurte benötigt.",
                "Die erforderliche Vorspannkraft und Anzahl der Zurrgurte sinken drastisch, da 60 % statt 20 % der Gewichtskraft durch Reibung gehalten werden.",
                "Der Reibbeiwert spielt beim Niederzurren keine Rolle."
            ),
            correctIndex = 1,
            explanation = "Beim Niederzurren wirkt die Reibkraft F_R = μ · (F_G + F_V) der Beschleunigungskraft entgegen. Höheres μ reduziert die nötige Zurrkraft massiv."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q5",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Elektrotechnik · Staplerladestation",
            prompt = "Ein Schnellladegerät arbeitet mit 400 V und zieht dabei einen Strom von 16 A (cos φ = 1). Welche elektrische Leistung P nimmt es auf?",
            options = listOf(
                "2,5 kW",
                "6,4 kW",
                "640 W"
            ),
            correctIndex = 1,
            explanation = "P = U × I = 400 V × 16 A = 6.400 W = 6,4 kW (bei einphasigem/Gleichstrom-Ersatzwert; bei 3~-Drehstrom gilt zusätzlich × √3)."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q6",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Hydraulik · Pascalsches Gesetz",
            prompt = "In einer Stapler-Hubhydraulik herrscht ein Druck von p = 150 bar (= 15 N/mm²) auf einer Kolbenfläche von A = 2.000 mm². Welche Hubkraft F erzeugt der Zylinder?",
            options = listOf(
                "3.000 N (ca. 300 kg)",
                "30.000 N (ca. 3.000 kg)",
                "300.000 N (ca. 30 t)"
            ),
            correctIndex = 1,
            explanation = "F = p × A = 15 N/mm² × 2.000 mm² = 30.000 N = 30 kN (entspricht ca. 3.058 kg Masse bei g = 9,81 m/s²)."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q7",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Wärmelehre · Kranbahn / Hallenbau",
            prompt = "Warum besitzen lange stählerne Kranbahnen und Förderstrecken in unbeheizten Logistikhallen Dehnungsfugen?",
            options = listOf(
                "Um elektrische Ströme abzuleiten",
                "Um die thermische Längenausdehnung Δl = l0 · α · ΔT zwischen Winter und Sommer spannungsfrei aufzunehmen",
                "Um das Eigengewicht der Stahlträger zu halbieren"
            ),
            correctIndex = 1,
            explanation = "Bei Temperaturänderungen ΔT dehnt sich Stahl (α ≈ 12·10⁻⁶ 1/K) aus oder zieht sich zusammen; ohne Fugen entstehen enorme Zwängungskräfte."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q8",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Chemie & Werkstoffe · Korrosion",
            prompt = "Warum sollte eine Batterie-Ladestation für Blei-Säure-Staplerbatterien gut belüftet sein und einen Neutralisations- bzw. Augenspülplatz haben?",
            options = listOf(
                "Beim Laden entsteht hochexplosives Knallgas (Wasserstoff) sowie Gefahr durch ätzende Schwefelsäure (pH < 2).",
                "Batterien geben beim Laden ausschließlich harmlosen Wasserdampf ab.",
                "Um den pH-Wert der Luft auf 14 anzuheben."
            ),
            correctIndex = 0,
            explanation = "Beim Ausgasen entsteht Wasserstoff (Explosionsgefahr, Zündquellen vermeiden!) und das Elektrolyt ist verdünnte Schwefelsäure (stark sauer, Augenschutz Pflicht)."
        )
    )
}
