package com.example.data.curriculum

/**
 * Aktuelle Ergänzungsfragen für Geprüfte Logistikmeister (Stand 2025/2026 nach LogMstrV):
 * Digitalisierung (AMR, WMS, IoT/RFID), Nachhaltigkeit (Green Logistics, Li-Ion, VerpackG),
 * Regal- & FFZ-Prüfung (DIN EN 15635, FEM 4.004), aktuelles Arbeitsrecht (§ 87 BetrVG Telematik,
 * NachwG Textform, BEM, psychische Gefährdungsbeurteilung) sowie IHK-Prüfungstaktik.
 */
internal object Logistik2026UpdateBank {
    val questions: List<LearningDrillQuestion> = listOf(
        // BQ 1: Aktuelles Arbeits- & Datenschutzrecht
        LearningDrillQuestion(
            id = "bq_recht_q9",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Digitalisierung & BetrVG § 87 Abs. 1 Nr. 6",
            prompt = "Im Zentrallager soll ein neues Stapler-Telematiksystem eingeführt werden, das neben Aufprallstößen auch exakte Fahrzeiten je Fahrer-PIN aufzeichnet. Was ist rechtlich zwingend nötig?",
            options = listOf(
                "Nichts, weil die Sicherheit im Betrieb allein Sache der Geschäftsführung ist.",
                "Zwingende Mitbestimmung des Betriebsrats nach § 87 Abs. 1 Nr. 6 BetrVG (technische Einrichtung zur Verhaltens-/Leistungsüberwachung) plus DSGVO-Prüfung.",
                "Nur ein Aushang am Schwarzen Brett einen Tag vor Inbetriebnahme."
            ),
            correctIndex = 1,
            explanation = "Jede technische Einrichtung, die objektiv geeignet ist, Verhalten oder Leistung zu überwachen (WMS-Tracking, Stapler-Telematik), unterliegt § 87 Abs. 1 Nr. 6 BetrVG sowie Art. 6 DSGVO / § 26 BDSG."
        ),
        LearningDrillQuestion(
            id = "bq_recht_q10",
            topicId = "bq_recht",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Nachweisgesetz (NachwG) · Stand BEG IV",
            prompt = "Welche Formvorschrift gilt seit dem Bürokratieentlastungsgesetz IV (2025) grundsätzlich für die Abfassung wesentlicher Arbeitsvertragsbedingungen nach dem Nachweisgesetz?",
            options = listOf(
                "Zulässig ist nun auch die Textform (z. B. elektronisches Dokument/PDF per E-Mail mit Empfangsnachweis), sofern kein Verlangen auf Schriftform gestellt wird und kein Branchen-Sonderfall greift.",
                "Arbeitsverträge dürfen nur noch notariell beglaubigt abgeschlossen werden.",
                "Mündliche Vereinbarungen ersetzen jede Dokumentation komplett."
            ),
            correctIndex = 0,
            explanation = "Seit 2025 erlaubt § 2 NachwG grundsätzlich die elektronische Textform (lesbar, speicherbar, zugänglich mit Empfangsnachweis), außer in speziellen Branchen (§ 2a SchwarzArbG, z. B. Speditions-/Logistikgewerbe beachten!) oder wenn der Arbeitnehmer ausdrücklich Schriftform verlangt."
        ),

        // BQ 2: Aktuelle BWL & Controlling
        LearningDrillQuestion(
            id = "bq_bwl_q9",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Zielkonflikt · Lieferbereitschaft vs. Kapitalbindung",
            prompt = "Warum führt eine Erhöhung des Lieferbereitschaftsgrads (Servicegrad) von 95 % auf 99,5 % zu überproportional steigenden Lagerkosten?",
            options = listOf(
                "Weil die Fixkosten ab 95 % auf Null sinken",
                "Weil der nötige Sicherheitsbestand für seltene Bedarfsspitzen exponentiell anwächst und damit Kapitalbindungs-, Lager- und Verschrottungskosten steil ansteigen.",
                "Weil Lieferanten ab 95 % höhere Rabatte gewähren."
            ),
            correctIndex = 1,
            explanation = "In der Lagerwirtschaft zeigt die Servicegradkurve einen exponentiellen Anstieg des Sicherheitsbestands (z-Faktor der Normalverteilung) oberhalb von ~95–98 %."
        ),
        LearningDrillQuestion(
            id = "bq_bwl_q10",
            topicId = "bq_bwl",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Investitionsentscheidung · Nutzwertanalyse",
            prompt = "Sie vergleichen zwei Lagerautomatisierungs-Konzepte mit der Nutzwertanalyse (Scoring-Modell). Worauf müssen Sie bei den Gewichtungsfaktoren der Kriterien zwingend achten?",
            options = listOf(
                "Jedes Kriterium bekommt automatisch 100 %.",
                "Die Summe aller Gewichtungsfaktoren über sämtliche Kriterien muss 100 % (bzw. 1,0) ergeben; dann werden Gewicht × Punktzahl je Alternative aufsummiert.",
                "Nur der Kaufpreis wird bewertet; weiche Kriterien sind verboten."
            ),
            correctIndex = 1,
            explanation = "Die Nutzwertanalyse bewertet auch qualitative/ökologische Faktoren (Ergonomie, Erweiterbarkeit, Energie). Gewichtungen summieren sich auf 100 %, Punkteskalen (z. B. 1–10) bleiben einheitlich."
        ),

        // BQ 3: MIKP & Smart Logistics
        LearningDrillQuestion(
            id = "bq_mikp_q9",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Auto-ID & Digitalisierung · RFID vs. Barcode",
            prompt = "Beim Wareneingang sollen komplette Mischpaletten mit 40 Kartons gleichzeitig ohne Sichtkontakt erfasst werden. Welches Identifikationssystem löst diese Anforderung?",
            options = listOf(
                "Klassischer 1D-EAN-13-Barcode mit Handscanner",
                "UHF-RFID-Transponder mit Gate-Reader (Pulkerfassung ohne Sichtverbindung in Sekundenbruchteilen)",
                "Manuelles Abhaken auf Papier-Lieferscheinen"
            ),
            correctIndex = 1,
            explanation = "RFID ermöglicht die gleichzeitige Pulkerfassung vieler Transponder ohne direkte Sichtlinie — ideal für automatisierte Wareneingangs-Gates."
        ),
        LearningDrillQuestion(
            id = "bq_mikp_q10",
            topicId = "bq_mikp",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Statistik & Kennzahlen · Streuung",
            prompt = "Zwei Lieferanten haben beide eine durchschnittliche Lieferzeit von 4 Tagen. Lieferant A schwankt zwischen 3 und 5 Tagen, Lieferant B zwischen 1 und 9 Tagen. Welche Kennzahl zeigt das Risiko von Lieferant B?",
            options = listOf(
                "Spannweite und Standardabweichung (Streuungsmaße der Lieferzeit)",
                "Ausschließlich der arithmetische Mittelwert",
                "Die Summe der Bestellnummern"
            ),
            correctIndex = 0,
            explanation = "Der Mittelwert allein verschleiert Risiken. Spannweite und Standardabweichung machen Lieferzeitschwankungen (und damit den nötigen Sicherheitsbestand) sichtbar."
        ),

        // BQ 4: Zusammenarbeit, Gesundheit & Generationen
        LearningDrillQuestion(
            id = "bq_zib_q9",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Arbeitsschutz & Gesundheit · § 5 ArbSchG & BEM",
            prompt = "Ein Lagerarbeiter war innerhalb der letzten 12 Monate länger als 6 Wochen arbeitsunfähig erkrankt. Welche gesetzliche Pflicht trifft den Betrieb nach § 167 Abs. 2 SGB IX?",
            options = listOf(
                "Sofortige verhaltensbedingte Kündigung ohne Gespräch",
                "Einladung zu einem Betrieblichen Eingliederungsmanagement (BEM), das für den Beschäftigten freiwillig und streng zweckgebunden ist.",
                "Verpflichtende Gehaltskürzung ab der siebten Woche"
            ),
            correctIndex = 1,
            explanation = "Ab > 6 Wochen Arbeitsunfähigkeit in 12 Monaten muss der Arbeitgeber ein BEM anbieten (§ 167 Abs. 2 SGB IX), um Arbeitsfähigkeit zu sichern und erneuter Erkrankung vorzubeugen."
        ),
        LearningDrillQuestion(
            id = "bq_zib_q10",
            topicId = "bq_zib",
            moduleCode = "BQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "ArbSchG § 5 · Psychische Belastungen im Lager",
            prompt = "Im Rahmen der Gefährdungsbeurteilung (§ 5 Abs. 3 Nr. 6 ArbSchG) müssen auch psychische Belastungen beurteilt werden. Was gehört im Logistikbetrieb typischerweise dazu?",
            options = listOf(
                "Hoher Zeit- und Termin- bzw. Pickdruck, widersprüchliche Arbeitsanweisungen, Lärm und mangelnde Pausenerholung",
                "Nur die Farbe der Staplerreifen",
                "Private Urlaubspläne der Führungskräfte"
            ),
            correctIndex = 0,
            explanation = "Arbeitsintensität (Zeitdruck), soziale Beziehungen, Arbeitsorganisation und Umgebungsfaktoren (Lärm/Licht) sind zentrale Prüfpunkte der psychischen Gefährdungsbeurteilung."
        ),

        // BQ 5: Aktuelle NTG (Li-Ion & Energie)
        LearningDrillQuestion(
            id = "bq_ntg_q9",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Antriebstechnik · Lithium-Ionen vs. Blei-Säure",
            prompt = "Welche Vorteile und besonderen Sicherheitsanforderungen bringt der Umstieg auf Lithium-Ionen-Staplerbatterien im 3-Schicht-Betrieb mit sich?",
            options = listOf(
                "Zwischenladen in Pausen möglich (kein Batteriewechsel, kein Knallgas), aber im Brandfall Gefahr eines Thermal Runaway (viel Kühlwasser / Quarantänebereich nötig).",
                "Lithium-Ionen-Batterien benötigen täglich destilliertes Wasser und erzeugen Schwefelsäure-Nebel.",
                "Lithium-Ionen-Batterien dürfen nicht unter 80 % geladen werden."
            ),
            correctIndex = 0,
            explanation = "Li-Ion erlaubt schnelles Zwischenladen ohne Wechselraum und spart ca. 20–30 % Energie; Sicherheitskonzept (Brandschutz, Beschädigungskontrolle) muss Thermal Runaway berücksichtigen."
        ),
        LearningDrillQuestion(
            id = "bq_ntg_q10",
            topicId = "bq_ntg",
            moduleCode = "BQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Energieeffizienz · Staplerflotte & Hallenstrom",
            prompt = "Eine LED-Umrüstung im Hochregallager spart gegenüber alten Leuchtstoffröhren 18 kW elektrische Leistung über 4.000 Betriebsstunden pro Jahr ein. Wie viele kWh und Euro (bei 0,30 €/kWh) werden jährlich gespart?",
            options = listOf(
                "72.000 kWh und 21.600 € pro Jahr",
                "7.200 kWh und 2.160 € pro Jahr",
                "18.000 kWh und 5.400 € pro Jahr"
            ),
            correctIndex = 0,
            explanation = "Energieeinsparung W = P × t = 18 kW × 4.000 h = 72.000 kWh/Jahr; Kosteneinsparung = 72.000 × 0,30 € = 21.600 €/Jahr."
        ),

        // HQ 1: Aktuelle Logistikprozesse (AMR, AKL/Shuttle, Green Logistics, Bullwhip)
        LearningDrillQuestion(
            id = "hq_logistik_q11",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Intralogistik 4.0 · AMR vs. klassisches FTS",
            prompt = "Worin unterscheidet sich ein Autonomous Mobile Robot (AMR) im Vergleich zu einem klassisch spurgeführten Fahrerlosen Transportfahrzeug (FTS/AGV)?",
            options = listOf(
                "AMRs navigieren per SLAM-Sensorik frei und umfahren dynamische Hindernisse ohne Boden-Magnetbänder oder feste Schienen.",
                "AMRs können ausschließlich manuell per Deichsel geschoben werden.",
                "AMRs benötigen grundsätzlich Oberleitungen in 6 m Höhe."
            ),
            correctIndex = 0,
            explanation = "AMRs nutzen LiDAR/Kameras und SLAM-Algorithmen zur freien Navigation und dynamischen Routenanpassung — ideal bei wechselnden Layouts."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q12",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Hochleistungs-Lagertechnik · AKL-RBG vs. Shuttle",
            prompt = "Warum erreichen Shuttle-Lagersysteme (Multi-Shuttle) im E-Commerce oft eine deutlich höhere Pickleistung je Stunde als konventionelle Regalbediengeräte (RBG)?",
            options = listOf(
                "Weil pro Regalebene unabhängige leichte Shuttles parallel verfahren und Aufzüge nur vertikal übergeben, statt dass ein schweres RBG jede Fahrt allein ausführt.",
                "Weil Shuttles keine Stromversorgung benötigen.",
                "Weil Shuttle-Systeme grundsätzlich ohne WMS funktionieren."
            ),
            correctIndex = 0,
            explanation = "Durch Entkopplung von Horizontal- (viele Shuttles parallel je Ebene) und Vertikalbewegung (schnelle Lifte) vervielfacht sich die Ein-/Auslagerleistung."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q13",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Green Logistics & CO2-Reduktion (Scope 1–3)",
            prompt = "Sie sollen die CO2-Bilanz in der Transport- und Intralogistik nachhaltig senken. Welche Maßnahme adressiert sowohl Wirtschaftlichkeit als auch Ökologie direkt?",
            options = listOf(
                "Vermeidung von Leerfahrten durch Rundlauf-/Tourenoptimierung, höhere Lkw-Auslastung, Rekuperation bei Regalbediengeräten und Einsatz von Mehrweg-Ladungsträgern.",
                "Alle Artikel einzeln per Express-Sprinter statt gebündelt versenden.",
                "Verzicht auf jegliche Bestandsführung."
            ),
            correctIndex = 0,
            explanation = "Green Logistics verbindet Emissionssenkung (Scope 1/3) direkt mit Kostensenkung durch Auslastungssteigerung, Energierückgewinnung und Kreislaufverpackungen."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q14",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Supply Chain Management · Bullwhip-Effekt",
            prompt = "Was verursacht den sogenannten 'Bullwhip-Effekt' (Peitscheneffekt) in mehrstufigen Lieferketten — und wie dämmt ein Logistikmeister ihn ein?",
            options = listOf(
                "Aufschaukeln kleiner Endkunden-Nachfrageschwankungen durch verzögerte Infos, Bündelbestellungen und Sicherheitszuschläge; Gegenmaßnahme: transparente Abverkaufsdaten (EDI/VMI) und kleine Lose.",
                "Zu schnelle Lkw auf Autobahnen; Gegenmaßnahme: Tempolimit 60 km/h.",
                "Fehlende Staplerscheine im Wareneingang."
            ),
            correctIndex = 0,
            explanation = "Informationsverzögerung, Losgrößenbündelung und Sicherheitsaufschläge führen stromaufwärts zu massiven Bestandsschwankungen. VMI, EDI und kurze Regelkreise stoppen ihn."
        ),
        LearningDrillQuestion(
            id = "hq_logistik_q15",
            topicId = "hq_logistik",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Beleglose Kommissionierung · Pick-by-Systeme",
            prompt = "Welches Kommissionierverfahren kombiniert freihändiges Arbeiten ('Hands-free, Eyes-free') mit besonders hoher Flexibilität ohne feste Fachanzeigen am Regal?",
            options = listOf(
                "Pick-by-Voice (Sprachdialog über Headset mit Prüfziffern-Bestätigung) bzw. Pick-by-Vision (Datenbrille)",
                "Klassischer gedruckter Laufzettel auf Klemmbrett",
                "Reine Blocklagerung ohne Kennzeichnung"
            ),
            correctIndex = 0,
            explanation = "Pick-by-Voice und Pick-by-Vision lassen beide Hände frei für Greifvorgänge und erfordern keine aufwendige Verkabelung am gesamten Regal wie Pick-by-Light."
        ),

        // HQ 2: Aktuelle Organisation, Regalprüfung DIN EN 15635, FFZ, VerpackG, TCO
        LearningDrillQuestion(
            id = "hq_org_q11",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Lagersicherheit · DIN EN 15635 Regalprüfung",
            prompt = "Welche Prüfpflichten und Einstufungen schreibt die DIN EN 15635 (in Verbindung mit BetrSichV) für betretene bzw. mit Staplern befahrene Palettenregale vor?",
            options = listOf(
                "Wöchentliche Sichtprüfung (interner Regalbeauftragter) plus mind. jährliche Experteninspektion; Schäden werden in Grün, Orange (Entlastung innerhalb 4 Wochen) und Rot (sofort sperren & entladen!) eingeteilt.",
                "Eine einmalige Prüfung beim Regalaufbau genügt für die gesamte Lebensdauer.",
                "Nur bei Einsturz eines Regals ist ein Protokoll nötig."
            ),
            correctIndex = 0,
            explanation = "DIN EN 15635 unterscheidet Grün (beobachten), Orange (so bald wie möglich, max. 4 Wochen reparieren/entlasten) und Rot (akute Gefahr -> sofort sperren & vollständig entladen!)."
        ),
        LearningDrillQuestion(
            id = "hq_org_q12",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "BetrSichV & DGUV Vorschrift 68 · Flurförderzeuge",
            prompt = "In welchem Intervall müssen Gabelstapler und andere kraftbetriebene Flurförderzeuge nach BetrSichV / DGUV Grundsatz 308-001 (FEM 4.004) durch eine befähigte Person geprüft werden?",
            options = listOf(
                "Mindestens einmal jährlich (wiederkehrende Prüfung) sowie nach wesentlichen Änderungen oder Unfällen; zusätzlich tägliche Einsatzkontrolle durch den Fahrer.",
                "Alle 5 Jahre durch den Paketdienst.",
                "Nur bei Ölwechsel."
            ),
            correctIndex = 0,
            explanation = "Nach § 14 BetrSichV und DGUV V68 gilt die jährliche Prüfpflicht durch eine zur Prüfung befähigte Person plus tägliche Sicht-/Funktionsprüfung vor Schichtbeginn."
        ),
        LearningDrillQuestion(
            id = "hq_org_q13",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Investitionskosten · Total Cost of Ownership (TCO)",
            prompt = "Warum reicht beim Vergleich zweier automatischer Verpackungsstraßen nicht allein der Anschaffungspreis aus, sondern eine TCO-Betrachtung (Total Cost of Ownership)?",
            options = listOf(
                "TCO berücksichtigt alle Lebenszykluskosten: Anschaffung, Energieverbrauch, Wartung/Ersatzteile, Stillstandsrisiko, Software-Updates und Entsorgung.",
                "TCO misst ausschließlich die Transportkosten vom Herstellerwerk.",
                "TCO ignoriert laufende Instandhaltungskosten bewusst."
            ),
            correctIndex = 0,
            explanation = "Gerade in der Intralogistik übersteigen laufende Energie-, Service-, Lizenz- und Ausfallkosten über 8–10 Jahre oft den ursprünglichen Kaufpreis."
        ),
        LearningDrillQuestion(
            id = "hq_org_q14",
            topicId = "hq_org_kosten",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Umwelt- & Verpackungsmanagement · VerpackG / LkSG",
            prompt = "Welche gesetzlichen Ziele verfolgt das Verpackungsgesetz (VerpackG) für Versand- und Transportverpackungen in der Logistik?",
            options = listOf(
                "Vermeidung von Verpackungsabfällen, hohe Recyclingquoten, Systembeteiligungspflicht (LUCID-Register) bei B2C-Service/Versandverpackungen sowie Rücknahmepflichten für Transportverpackungen.",
                "Verbot aller Mehrwegpaletten (z. B. Europaletten) ab 2026.",
                "Freiwillige Verbrennung sämtlicher Kartonagen auf dem Betriebshof."
            ),
            correctIndex = 0,
            explanation = "Transportverpackungen müssen unentgeltlich zurückgenommen und verwertet werden (§ 15 VerpackG); Systembeteiligung & Recyclingfähigkeit reduzieren Müll und Gebühren."
        ),

        // HQ 3: Aktuelle Führung & Personal (Arbeitssicherheit, Onboarding, Change)
        LearningDrillQuestion(
            id = "hq_fuehrung_q9",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Unterweisungspflicht · § 12 ArbSchG & DGUV V1",
            prompt = "Wie oft müssen Mitarbeiter im Lager über Sicherheit und Gesundheitsschutz (inkl. Flurförderzeuge & Gefahrstoffe) unterwiesen werden und wie ist dies nachzuweisen?",
            options = listOf(
                "Bei Einstellung, vor Aufnahme neuer Tätigkeiten sowie mindestens jährlich (bei Jugendlichen halbjährlich) — zwingend schriftlich/dokumentiert mit Datum und Unterschrift.",
                "Einmal mündlich alle 4 Jahre genügt rechtssicher.",
                "Nur bei Mitarbeitern ohne Deutschkenntnisse."
            ),
            correctIndex = 0,
            explanation = "§ 12 ArbSchG und § 4 DGUV Vorschrift 1 schreiben mindestens jährliche (und anlassbezogene) Unterweisungen mit verständlicher Erfolgskontrolle und Dokumentation vor."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q10",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Fachkräftemangel · Strukturiertes Onboarding",
            prompt = "Im Lager scheiden viele neue Fachkräfte bereits in der Probezeit wegen Überforderung aus. Welche Maßnahme verbessert Bindung und Einarbeitung nachweislich am stärksten?",
            options = listOf(
                "Strukturierter Einarbeitungsplan mit festem Paten/Mentor, klaren Lernzielen pro Woche und regelmäßigen Feedbackgesprächen nach 14, 30 und 90 Tagen.",
                "Sofortige Einzelarbeit in der Nachtschicht ohne Einweisung.",
                "Streichung aller Pausen in den ersten vier Wochen."
            ),
            correctIndex = 0,
            explanation = "Gezieltes Onboarding durch Patenmodell, klare Checklisten (Sicherheit, WMS, Prozesse) und Meilenstein-Feedback senkt Frühfluktuation und Einarbeitungsfehler drastisch."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q11",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Arbeitsrecht im Schichtalltag · Abmahnung",
            prompt = "Ein Mitarbeiter vergisst wiederholt die Sicherheitsweste im Staplerbereich. Welche drei Funktionen muss eine wirksame arbeitsrechtliche Abmahnung schriftlich erfüllen?",
            options = listOf(
                "1. Rüge- & Dokumentationsfunktion (konkretes Fehlverhalten mit Datum/Ort), 2. Hinweisfunktion (Pflichtverstoß klar benennen), 3. Warnfunktion (Androhung der Kündigung im Wiederholungsfall).",
                "Nur der Satz 'Sie müssen besser aufpassen' ohne Datum.",
                "Sofortige Geldstrafe ohne Begründung."
            ),
            correctIndex = 0,
            explanation = "Ohne konkrete Beschreibung (Wer, Was, Wann), Benennung der verletzten Pflicht und ausdrückliche Kündigungsandrohung ist eine Abmahnung rechtlich angreifbar."
        ),
        LearningDrillQuestion(
            id = "hq_fuehrung_q12",
            topicId = "hq_fuehrung",
            moduleCode = "HQ",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Predictive Maintenance & KI in der Führung",
            prompt = "Wie verändert Predictive Maintenance (vorausschauende Wartung per Sensorik an Fördertechnik/RBG) die Einsatzplanung des Logistikmeisters?",
            options = listOf(
                "Wartungen lassen sich gezielt in schwache Schichtfenster vor einem Ausfall planen statt teure ungeplante Bandstillstände zu erleiden; Mitarbeiter müssen für Daten-Interpretation qualifiziert werden.",
                "Regelmäßige Inspektionen entfallen weltweit für immer.",
                "Mitarbeiter verlieren dadurch jegliche Arbeitssicherheits-Verantwortung."
            ),
            correctIndex = 0,
            explanation = "Zustandsdaten (Vibration, Stromaufnahme, Temperatur) ermöglichen planbare Instandhaltung vor Ausfall — erfordert aber enge Abstimmung zwischen Instandhaltung, Schichtplan und Qualifizierung."
        ),

        // SA: Offizielle LogMstrV Prüfungsordnung & Mündliche Ergänzungsprüfung
        LearningDrillQuestion(
            id = "sa_q7",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "LogMstrV · Schriftliche Prüfungszeiten (Stand 2025/2026)",
            prompt = "Wie viel reguläre Bearbeitungszeit sieht die Logistikmeisterprüfungsverordnung (LogMstrV) für die fünf BQ-Klausuren und die beiden schriftlichen HQ-Situationsaufgaben vor?",
            options = listOf(
                "BQ: je Fach 90 Minuten · HQ: jeweils 240 Minuten (4 Stunden) je schriftlicher Situationsaufgabe",
                "BQ: 30 Minuten insgesamt · HQ: 45 Minuten je Situationsaufgabe",
                "Alle Prüfungen finden ausschließlich als 15-Minuten-Multiple-Choice statt."
            ),
            correctIndex = 0,
            explanation = "Laut LogMstrV dauern die BQ-Fächer jeweils ca. 90 Min. und die beiden schriftlichen HQ-Situationsaufgaben jeweils 240 Min. (zuzüglich mündliches Fachgespräch mit 30 Min. Vorbereitung)."
        ),
        LearningDrillQuestion(
            id = "sa_q8",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.KENNZAHLEN_SPRINT,
            contextBanner = "Mündliche Ergänzungsprüfung · 2:1-Gewichtung",
            prompt = "Sie erzielen in genau einem BQ-Prüfungsfach 42 Punkte (Note 5: mangelhaft). Wie greift die mündliche Ergänzungsprüfung nach LogMstrV?",
            options = listOf(
                "Auf Antrag möglich bei genau einem mangelhaften Fach (30–49 P.); schriftliche Note und mündliche Ergänzungsnote werden im Verhältnis 2 : 1 gewichtet (hier wären mind. 66 P. mündlich nötig -> (42×2 + 66)/3 = 50 P.).",
                "Man ist automatisch endgültig durch alle Meisterteile durchgefallen.",
                "Die mündliche Ergänzungsprüfung zählt zu 100 % allein ohne Vorleistung."
            ),
            correctIndex = 0,
            explanation = "Merke die Formel: (2 × schriftl. Punkte + 1 × mündl. Punkte) / 3 ≥ 50 Punkte! Funktioniert bei genau einer Note 5 (30–49 P.) in BQ bzw. einer schriftlichen HQ-Situationsaufgabe (bei ungenügend < 30 P. nicht zulässig)."
        ),
        LearningDrillQuestion(
            id = "sa_q9",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRUEFUNGS_CHECK,
            contextBanner = "Zulassung zur HQ-Teilprüfung (LogMstrV)",
            prompt = "Welcher zusätzliche Nachweis muss nach Logistikmeisterprüfungsverordnung spätestens bis zur letzten HQ-Prüfungsleistung bzw. Zulassung zum HQ-Teil vorliegen?",
            options = listOf(
                "Der Nachweis der berufs- und arbeitspädagogischen Kenntnisse (Ausbildereignung / AdA-Schein nach AEVO) sowie das vorherige Ablegen der BQ-Prüfung (max. 5 Jahre alt).",
                "Ein Hochschulabschluss in Luft- und Raumfahrttechnik.",
                "Eine Bescheinigung über mindestens 10 Auslandsreisen."
            ),
            correctIndex = 0,
            explanation = "Für Teil 2 (HQ) der Logistikmeisterprüfung sind das abgelegte BQ-Verfahren (innerhalb der letzten 5 Jahre) und der Nachweis der AEVO-Ausbildereignung Pflicht."
        ),
        LearningDrillQuestion(
            id = "sa_q10",
            topicId = "sa_fachgespraech",
            moduleCode = "SA",
            mode = DrillMode.PRAXIS_DIALOG,
            contextBanner = "Situationsaufgabe · Verknüpfung Logistik + Kosten + Recht",
            prompt = "Warum stellt die IHK in der 1. und 2. schriftlichen Situationsaufgabe oft eine durchgängige Ausgangssituation eines Modellbetriebs über 240 Minuten?",
            options = listOf(
                "Weil ein Geprüfter Logistikmeister im Betrieb technische Logistikprozesse, KLR-Wirtschaftlichkeit, Arbeitsschutz/Recht und Mitarbeiterführung integriert lösen muss.",
                "Damit man jeden Paragrafen auswendig ohne Fallbezug abschreibt.",
                "Weil alle Teilaufgaben unabhängig voneinander sind und nichts mit der Ausgangssituation zu tun haben."
            ),
            correctIndex = 0,
            explanation = "In der HQ werden nicht isolierte Definitionen abgefragt, sondern jede Antwort und Maßnahme muss konkret die Zahlen, Engpässe und Mitarbeiter der Ausgangssituation einbeziehen."
        )
    )
}
