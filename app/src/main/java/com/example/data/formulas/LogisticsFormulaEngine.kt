package com.example.data.formulas

import java.util.Locale
import kotlin.math.ceil
import kotlin.math.sqrt

data class FormulaInputField(
    val key: String,
    val label: String,
    val unit: String,
    val defaultValue: String
)

data class LogisticsFormula(
    val id: String,
    val title: String,
    val category: String, // "BQ" or "HQ"
    val topicTag: String,
    val formulaNotation: String,
    val unitHint: String,
    val description: String,
    val inputs: List<FormulaInputField>
)

data class FormulaCalculationResult(
    val value: Double,
    val formattedValue: String,
    val unit: String,
    val steps: List<String>,
    val interpretation: String
)

object LogisticsFormulaEngine {

    val formulas: List<LogisticsFormula> = listOf(
        LogisticsFormula(
            id = "avg_stock",
            title = "Durchschnittlicher Lagerbestand",
            category = "HQ",
            topicTag = "Lagerkennzahlen",
            formulaNotation = "Ø Bestand = (AB + EB) / 2",
            unitHint = "Stück / EUR",
            description = "Ermittelt die durchschnittlich im Lager gebundene Menge oder den Bestandswert aus Anfangs- und Endbestand.",
            inputs = listOf(
                FormulaInputField("ab", "Anfangsbestand (AB)", "Stk", "1200"),
                FormulaInputField("eb", "Endbestand (EB)", "Stk", "1800")
            )
        ),
        LogisticsFormula(
            id = "inventory_turnover",
            title = "Lagerumschlagshäufigkeit",
            category = "HQ",
            topicTag = "Lagerkennzahlen",
            formulaNotation = "LUH = Jahresverbrauch / Ø Lagerbestand",
            unitHint = "pro Jahr",
            description = "Zeigt an, wie oft der durchschnittliche Lagerbestand pro Periode komplett umgeschlagen wird.",
            inputs = listOf(
                FormulaInputField("usage", "Jahresverbrauch / Wareneinsatz", "Stk oder EUR", "36000"),
                FormulaInputField("avg", "Ø Lagerbestand", "Stk oder EUR", "3000")
            )
        ),
        LogisticsFormula(
            id = "storage_duration",
            title = "Durchschnittliche Lagerdauer",
            category = "HQ",
            topicTag = "Lagerkennzahlen",
            formulaNotation = "Ø Lagerdauer = 360 / Lagerumschlagshäufigkeit",
            unitHint = "Tage",
            description = "Gibt an, wie viele Tage ein Artikel durchschnittlich bis zum Verbrauch im Lager liegt (IHK-Bankjahr = 360 Tage).",
            inputs = listOf(
                FormulaInputField("turnover", "Lagerumschlagshäufigkeit (LUH)", "x/Jahr", "12")
            )
        ),
        LogisticsFormula(
            id = "storage_interest",
            title = "Lagerzinssatz & Lagerzinsen",
            category = "HQ",
            topicTag = "Lagerkosten",
            formulaNotation = "LZ = (Ø Bestandswert × p_Jahr × Ø Lagerdauer) / (100 × 360)",
            unitHint = "EUR",
            description = "Berechnet die Kapitalbindungskosten für durchschnittlich gelagerte Bestände.",
            inputs = listOf(
                FormulaInputField("stockValue", "Ø Lagerbestandswert", "EUR", "150000"),
                FormulaInputField("interestRate", "Jahreszinssatz (p)", "%", "8"),
                FormulaInputField("days", "Ø Lagerdauer", "Tage", "30")
            )
        ),
        LogisticsFormula(
            id = "andler_eoq",
            title = "Optimale Bestellmenge (Andler)",
            category = "HQ",
            topicTag = "Beschaffungslogistik",
            formulaNotation = "Q_opt = √((200 × M × K_b) / (p × q))",
            unitHint = "Stück",
            description = "Bestimmt die kostenminimale Bestellmenge im Zielkonflikt zwischen Bestellfix- und Lagerhaltungskosten.",
            inputs = listOf(
                FormulaInputField("demand", "Jahresbedarf (M)", "Stk", "10000"),
                FormulaInputField("orderCost", "Fixe Bestellkosten (K_b)", "EUR", "120"),
                FormulaInputField("price", "Einstandspreis je Stk (p)", "EUR", "25"),
                FormulaInputField("holdingRate", "Lagerkostensatz (q)", "%", "16")
            )
        ),
        LogisticsFormula(
            id = "reorder_point",
            title = "Meldebestand (Bestellpunkt)",
            category = "HQ",
            topicTag = "Bestandsmanagement",
            formulaNotation = "MB = (Tagesverbrauch × WBZ) + Sicherheitsbestand",
            unitHint = "Stück",
            description = "Bestandsschwelle, bei deren Erreichen sofort eine Nachbestellung ausgelöst werden muss.",
            inputs = listOf(
                FormulaInputField("dailyUsage", "Durchschn. Tagesverbrauch", "Stk/Tag", "150"),
                FormulaInputField("leadTime", "Wiederbeschaffungszeit (WBZ)", "Tage", "6"),
                FormulaInputField("safetyStock", "Sicherheitsbestand", "Stk", "300")
            )
        ),
        LogisticsFormula(
            id = "warehouse_utilization",
            title = "Flächen- / Raumauslastungsgrad",
            category = "HQ",
            topicTag = "Lagerplanung",
            formulaNotation = "Auslastung = (Genutzte Kapazität / Gesamtkapazität) × 100",
            unitHint = "%",
            description = "Misst die reale Auslastung von Lagerfläche oder Stellplätzen (Zielkorridor meist 80–90 % für Dynamik).",
            inputs = listOf(
                FormulaInputField("used", "Belegte Stellplätze / m²", "Einheiten", "4250"),
                FormulaInputField("total", "Gesamte Stellplätze / m²", "Einheiten", "5000")
            )
        ),
        LogisticsFormula(
            id = "picking_performance",
            title = "Kommissionierleistung (Picks/h)",
            category = "HQ",
            topicTag = "Kommissionierung",
            formulaNotation = "Leistung = Positionen / Gesamtzeit(h)",
            unitHint = "Pos./h",
            description = "Berechnet die stündliche Kommissionierleistung unter Berücksichtigung von Basis-, Greif-, Weg- und Totzeit.",
            inputs = listOf(
                FormulaInputField("positions", "Anzahl Positionen je Auftrag", "Pos.", "24"),
                FormulaInputField("baseMin", "Basiszeit je Auftrag", "min", "4"),
                FormulaInputField("pickSec", "Greifzeit je Position", "s", "15"),
                FormulaInputField("walkSec", "Wegzeit je Position", "s", "25")
            )
        ),
        LogisticsFormula(
            id = "break_even",
            title = "Break-Even-Menge & Deckungsbeitrag",
            category = "BQ",
            topicTag = "KLR & Controlling",
            formulaNotation = "x_BEP = K_fix / (Preis − k_var)",
            unitHint = "Stück / Aufträge",
            description = "Ermittelt die Gewinnschwelle, ab der die Fixkosten durch den Stückdeckungsbeitrag vollständig gedeckt sind.",
            inputs = listOf(
                FormulaInputField("fixedCost", "Fixkosten (K_fix)", "EUR", "45000"),
                FormulaInputField("price", "Netto-Erlös je Einheit", "EUR", "65"),
                FormulaInputField("varCost", "Variable Kosten je Einheit (k_var)", "EUR", "40")
            )
        ),
        LogisticsFormula(
            id = "payback_period",
            title = "Statische Amortisationsdauer",
            category = "BQ",
            topicTag = "Investitionsrechnung",
            formulaNotation = "t_Amort = Investition / (Jährl. Rückfluss + AfA)",
            unitHint = "Jahre",
            description = "Zeigt, nach wie vielen Jahren sich ein neues Flurförderzeug oder Lagerlift durch Einsparungen refinanziert.",
            inputs = listOf(
                FormulaInputField("capex", "Anschaffungskosten", "EUR", "96000"),
                FormulaInputField("annualSaving", "Jährl. Gewinn / Einsparung", "EUR/Jahr", "18000"),
                FormulaInputField("depreciation", "Jährl. Abschreibung (AfA)", "EUR/Jahr", "12000")
            )
        ),
        LogisticsFormula(
            id = "lashing_straps",
            title = "Ladungssicherung: Niederzurren (VDI 2700)",
            category = "BQ",
            topicTag = "NTG & Sicherheit",
            formulaNotation = "F_v = F_G × (c_x − μ) / μ  → Gurte = ⌈F_v / (2 × S_TF × sin α)⌉",
            unitHint = "daN / Gurte",
            description = "Berechnet die erforderliche Vorspannkraft und Mindestanzahl an Zurrgurten beim Niederzurren (Vorwärtsbeschleunigung c_x = 0,8).",
            inputs = listOf(
                FormulaInputField("massKg", "Ladungsgewicht", "kg", "4000"),
                FormulaInputField("mu", "Gleitreibbeiwert (μ)", "Faktor", "0.3"),
                FormulaInputField("stfDaN", "Vorspannkraft je Gurt (S_TF)", "daN", "350"),
                FormulaInputField("angleDeg", "Zurrwinkel α (ca. 45–90°)", "Grad", "60")
            )
        ),
        LogisticsFormula(
            id = "conveyor_throughput",
            title = "Massendurchsatz Stetigförderer",
            category = "BQ",
            topicTag = "NTG & Fördertechnik",
            formulaNotation = "I_m = 3,6 × q × v",
            unitHint = "t/h",
            description = "Berechnet den stündlichen Massendurchsatz eines Gurtförderers aus Streckenlast (kg/m) und Fördergeschwindigkeit (m/s).",
            inputs = listOf(
                FormulaInputField("loadKgM", "Streckenlast (q)", "kg/m", "25"),
                FormulaInputField("speedMs", "Fördergeschwindigkeit (v)", "m/s", "1.4")
            )
        )
    )

    fun calculate(formulaId: String, rawValues: Map<String, Double>): Result<FormulaCalculationResult> {
        return runCatching {
            when (formulaId) {
                "avg_stock" -> {
                    val ab = rawValues.req("ab")
                    val eb = rawValues.req("eb")
                    val res = (ab + eb) / 2.0
                    FormulaCalculationResult(
                        value = res,
                        formattedValue = fmt(res),
                        unit = "Stk / EUR",
                        steps = listOf(
                            "1. Anfangsbestand und Endbestand addieren: ${fmt(ab)} + ${fmt(eb)} = ${fmt(ab + eb)}",
                            "2. Summe durch 2 teilen: ${fmt(ab + eb)} / 2 = ${fmt(res)}"
                        ),
                        interpretation = "Der durchschnittliche Lagerbestand beträgt ${fmt(res)} Einheiten."
                    )
                }

                "inventory_turnover" -> {
                    val usage = rawValues.req("usage")
                    val avg = rawValues.reqPositive("avg", "Ø Lagerbestand")
                    val res = usage / avg
                    FormulaCalculationResult(
                        value = res,
                        formattedValue = fmt(res),
                        unit = "x / Jahr",
                        steps = listOf(
                            "1. Formel: LUH = Jahresverbrauch / Ø Lagerbestand",
                            "2. Einsetzen: ${fmt(usage)} / ${fmt(avg)} = ${fmt(res)}"
                        ),
                        interpretation = "Der Bestand schlägt sich ${fmt(res)}-mal pro Jahr um."
                    )
                }

                "storage_duration" -> {
                    val turnover = rawValues.reqPositive("turnover", "Lagerumschlagshäufigkeit")
                    val res = 360.0 / turnover
                    FormulaCalculationResult(
                        value = res,
                        formattedValue = fmt(res),
                        unit = "Tage",
                        steps = listOf(
                            "1. IHK-Bankjahr = 360 Tage",
                            "2. Ø Lagerdauer = 360 / ${fmt(turnover)} = ${fmt(res)} Tage"
                        ),
                        interpretation = "Jeder Artikel liegt im Schnitt ${fmt(res)} Tage im Lager."
                    )
                }

                "storage_interest" -> {
                    val stockValue = rawValues.req("stockValue")
                    val rate = rawValues.req("interestRate")
                    val days = rawValues.req("days")
                    val storageRate = (rate * days) / 360.0
                    val interestEur = (stockValue * storageRate) / 100.0
                    FormulaCalculationResult(
                        value = interestEur,
                        formattedValue = fmt(interestEur),
                        unit = "EUR",
                        steps = listOf(
                            "1. Lagerzinssatz = (${fmt(rate)}% × ${fmt(days)} Tage) / 360 = ${fmt(storageRate)}%",
                            "2. Lagerzinsen = (${fmt(stockValue)} EUR × ${fmt(storageRate)}%) / 100 = ${fmt(interestEur)} EUR"
                        ),
                        interpretation = "Kapitalbindungskosten betragen ${fmt(interestEur)} EUR (Lagerzinssatz ${fmt(storageRate)}%)."
                    )
                }

                "andler_eoq" -> {
                    val m = rawValues.reqPositive("demand", "Jahresbedarf")
                    val kb = rawValues.reqPositive("orderCost", "Fixe Bestellkosten")
                    val p = rawValues.reqPositive("price", "Einstandspreis")
                    val q = rawValues.reqPositive("holdingRate", "Lagerkostensatz")
                    val numerator = 200.0 * m * kb
                    val denominator = p * q
                    val res = sqrt(numerator / denominator)
                    val ordersPerYear = m / res
                    FormulaCalculationResult(
                        value = res,
                        formattedValue = fmt(res),
                        unit = "Stück",
                        steps = listOf(
                            "1. Zähler: 200 × ${fmt(m)} × ${fmt(kb)} = ${fmt(numerator)}",
                            "2. Nenner: ${fmt(p)} × ${fmt(q)} = ${fmt(denominator)}",
                            "3. Quotient ziehen: √(${fmt(numerator / denominator)}) = ${fmt(res)} Stück",
                            "4. Bestellhäufigkeit: ${fmt(m)} / ${fmt(res)} ≈ ${fmt(ordersPerYear)} Bestellungen/Jahr"
                        ),
                        interpretation = "Optimal sind ca. ${ceil(res).toInt()} Stück je Bestellung (~${fmt(ordersPerYear)}× pro Jahr)."
                    )
                }

                "reorder_point" -> {
                    val daily = rawValues.req("dailyUsage")
                    val wbz = rawValues.req("leadTime")
                    val safety = rawValues.req("safetyStock")
                    val res = (daily * wbz) + safety
                    FormulaCalculationResult(
                        value = res,
                        formattedValue = fmt(res),
                        unit = "Stück",
                        steps = listOf(
                            "1. Verbrauch während WBZ: ${fmt(daily)} × ${fmt(wbz)} = ${fmt(daily * wbz)} Stk",
                            "2. Plus Sicherheitsbestand: ${fmt(daily * wbz)} + ${fmt(safety)} = ${fmt(res)} Stk"
                        ),
                        interpretation = "Bei Unterschreitung von ${fmt(res)} Stück sofort nachbestellen."
                    )
                }

                "warehouse_utilization" -> {
                    val used = rawValues.req("used")
                    val total = rawValues.reqPositive("total", "Gesamtkapazität")
                    val res = (used / total) * 100.0
                    FormulaCalculationResult(
                        value = res,
                        formattedValue = fmt(res),
                        unit = "%",
                        steps = listOf(
                            "1. Quotient bilden: ${fmt(used)} / ${fmt(total)} = ${fmt(used / total)}",
                            "2. In Prozent umrechnen: × 100 = ${fmt(res)}%"
                        ),
                        interpretation = when {
                            res > 92.0 -> "${fmt(res)}% — Kritisch hoch! Einlagerungsstau und Suchzeiten drohen."
                            res < 60.0 -> "${fmt(res)}% — Geringe Flächenproduktivität, Konsolidierung prüfen."
                            else -> "${fmt(res)}% — Gesunder Betriebsbereich für dynamische Lagerprozesse."
                        }
                    )
                }

                "picking_performance" -> {
                    val pos = rawValues.reqPositive("positions", "Positionen")
                    val baseMin = rawValues.req("baseMin")
                    val pickSec = rawValues.req("pickSec")
                    val walkSec = rawValues.req("walkSec")
                    val totalSec = (baseMin * 60.0) + pos * (pickSec + walkSec)
                    val totalHours = totalSec / 3600.0
                    val picksPerHour = pos / totalHours
                    FormulaCalculationResult(
                        value = picksPerHour,
                        formattedValue = fmt(picksPerHour),
                        unit = "Pos./h",
                        steps = listOf(
                            "1. Basiszeit in Sekunden: ${fmt(baseMin)} min × 60 = ${fmt(baseMin * 60.0)} s",
                            "2. Positionenzeiten: ${fmt(pos)} × (${fmt(pickSec)} + ${fmt(walkSec)}) = ${fmt(pos * (pickSec + walkSec))} s",
                            "3. Auftragszeit gesamt: ${fmt(totalSec)} s (= ${fmt(totalSec / 60.0)} min)",
                            "4. Leistung/h: ${fmt(pos)} / (${fmt(totalSec)} / 3600) = ${fmt(picksPerHour)} Pos./h"
                        ),
                        interpretation = "Ein Auftrag dauert ${fmt(totalSec / 60.0)} min; Leistung = ${fmt(picksPerHour)} Picks/Stunde."
                    )
                }

                "break_even" -> {
                    val kFix = rawValues.req("fixedCost")
                    val price = rawValues.req("price")
                    val kVar = rawValues.req("varCost")
                    val db = price - kVar
                    require(db > 0.0) { "Der Stückdeckungsbeitrag (Preis − k_var) muss positiv sein." }
                    val bep = kFix / db
                    FormulaCalculationResult(
                        value = bep,
                        formattedValue = fmt(bep),
                        unit = "Einheiten",
                        steps = listOf(
                            "1. Stückdeckungsbeitrag db = ${fmt(price)} − ${fmt(kVar)} = ${fmt(db)} EUR",
                            "2. Break-Even-Menge = ${fmt(kFix)} / ${fmt(db)} = ${fmt(bep)} Einheiten"
                        ),
                        interpretation = "Ab ${ceil(bep).toInt()} Einheiten arbeitet der Prozess in der Gewinnzone."
                    )
                }

                "payback_period" -> {
                    val capex = rawValues.req("capex")
                    val saving = rawValues.req("annualSaving")
                    val afa = rawValues.req("depreciation")
                    val cashflow = saving + afa
                    require(cashflow > 0.0) { "Jährlicher Rückfluss (Gewinn + AfA) muss größer als 0 sein." }
                    val years = capex / cashflow
                    FormulaCalculationResult(
                        value = years,
                        formattedValue = fmt(years),
                        unit = "Jahre",
                        steps = listOf(
                            "1. Jährlicher Rückfluss = ${fmt(saving)} + ${fmt(afa)} = ${fmt(cashflow)} EUR/Jahr",
                            "2. Amortisationsdauer = ${fmt(capex)} / ${fmt(cashflow)} = ${fmt(years)} Jahre"
                        ),
                        interpretation = "Die Investition amortisiert sich nach ${fmt(years)} Jahren (${fmt(years * 12)} Monaten)."
                    )
                }

                "lashing_straps" -> {
                    val massKg = rawValues.reqPositive("massKg", "Ladungsgewicht")
                    val mu = rawValues.reqPositive("mu", "Reibbeiwert μ")
                    val stf = rawValues.reqPositive("stfDaN", "Vorspannkraft S_TF")
                    val angleDeg = rawValues.reqPositive("angleDeg", "Zurrwinkel").coerceIn(10.0, 90.0)
                    val fgDaN = massKg * 0.981
                    val cx = 0.8
                    val neededFactor = (cx - mu).coerceAtLeast(0.0) / mu
                    val fv = fgDaN * neededFactor
                    val sinAlpha = kotlin.math.sin(Math.toRadians(angleDeg))
                    val effectivePerStrap = 2.0 * stf * sinAlpha
                    val straps = if (fv <= 0.0) 0 else ceil(fv / effectivePerStrap).toInt()
                    FormulaCalculationResult(
                        value = straps.toDouble(),
                        formattedValue = "$straps",
                        unit = "Zurrgurte (F_v = ${fmt(fv)} daN)",
                        steps = listOf(
                            "1. Gewichtskraft F_G = ${fmt(massKg)} kg × 0,981 ≈ ${fmt(fgDaN)} daN",
                            "2. Erforderliche Vorspannkraft F_v = ${fmt(fgDaN)} × (0,8 − ${fmt(mu)}) / ${fmt(mu)} = ${fmt(fv)} daN",
                            "3. Wirksame Kraft je Gurt (k=2, sin ${fmt(angleDeg)}° = ${fmt(sinAlpha)}): ${fmt(effectivePerStrap)} daN",
                            "4. Benötigte Gurte = ⌈${fmt(fv)} / ${fmt(effectivePerStrap)}⌉ = $straps Gurte"
                        ),
                        interpretation = if (straps == 0) {
                            "Reibung μ ≥ 0,8 reicht rechnerisch aus; Mindestsicherung nach VDI 2700 vorsehen!"
                        } else {
                            "Mindestens $straps Niederzurr-Gurte (aufgerundet) gemäß VDI 2700 erforderlich."
                        }
                    )
                }

                "conveyor_throughput" -> {
                    val q = rawValues.req("loadKgM")
                    val v = rawValues.req("speedMs")
                    val im = 3.6 * q * v
                    FormulaCalculationResult(
                        value = im,
                        formattedValue = fmt(im),
                        unit = "t/h",
                        steps = listOf(
                            "1. Umrechnungsfaktor kg/s → t/h = 3,6",
                            "2. I_m = 3,6 × ${fmt(q)} kg/m × ${fmt(v)} m/s = ${fmt(im)} t/h"
                        ),
                        interpretation = "Der Stetigförderer bewegt ${fmt(im)} Tonnen pro Stunde."
                    )
                }

                else -> error("Unbekannte Formel-ID: $formulaId")
            }
        }
    }

    private fun Map<String, Double>.req(key: String): Double {
        return this[key] ?: throw IllegalArgumentException("Eingabewert '$key' fehlt.")
    }

    private fun Map<String, Double>.reqPositive(key: String, label: String): Double {
        val v = req(key)
        require(v > 0.0) { "$label muss größer als 0 sein." }
        return v
    }

    private fun fmt(value: Double): String {
        return String.format(Locale.GERMANY, "%,.2f", value)
            .replace(",00", "")
    }
}
