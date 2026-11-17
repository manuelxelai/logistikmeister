package com.example.ui.formulas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.formulas.LogisticsFormula
import com.example.data.formulas.LogisticsFormulaEngine
import com.example.data.model.FormulaMastery
import com.example.ui.theme.EmeraldMastery
import com.example.ui.theme.ErrorCrimson
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SkyCognitive

@Composable
fun FormulaDojoScreen(
    formulaViewModel: FormulaViewModel,
    formulaMasteryList: List<FormulaMastery>,
    modifier: Modifier = Modifier
) {
    val calcState by formulaViewModel.calcState.collectAsStateWithLifecycle()
    val dojoState by formulaViewModel.dojoState.collectAsStateWithLifecycle()
    var activeTab by remember { mutableIntStateOf(0) } // 0 = Rechner, 1 = KI-Rechen-Dojo

    val filteredFormulas = remember(calcState.selectedCategory) {
        if (calcState.selectedCategory == "ALLE") {
            LogisticsFormulaEngine.formulas
        } else {
            LogisticsFormulaEngine.formulas.filter { it.category == calcState.selectedCategory }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 640.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Formel-Engine & Rechen-Dojo",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Alle IHK-BQ/HQ-Formeln mit Schritt-für-Schritt-Lösungsweg und KI-Aufgabengenerator.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        icon = { Icon(Icons.Filled.Calculate, contentDescription = null) }
                    ) {
                        Text("Formel-Rechner")
                    }
                    SegmentedButton(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        icon = { Icon(Icons.Filled.AutoAwesome, contentDescription = null) }
                    ) {
                        Text("KI-Rechen-Dojo")
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ALLE" to "Alle Formeln", "HQ" to "HQ Logistik", "BQ" to "BQ KLR/NTG").forEach { (cat, label) ->
                        FilterChip(
                            selected = calcState.selectedCategory == cat,
                            onClick = { formulaViewModel.selectCategory(cat) },
                            label = { Text(label) }
                        )
                    }
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filteredFormulas, key = { it.id }) { formula ->
                        val isSelected = formula.id == calcState.selectedFormula.id
                        val mastery = formulaMasteryList.find { it.formulaId == formula.id }
                        FormulaChipCard(
                            formula = formula,
                            isSelected = isSelected,
                            solvedCount = mastery?.solvedCount ?: 0L,
                            onClick = { formulaViewModel.selectFormula(formula) }
                        )
                    }
                }
            }

            item {
                FormulaOverviewHeader(formula = calcState.selectedFormula)
            }

            if (activeTab == 0) {
                item {
                    FormulaCalculatorCard(
                        formula = calcState.selectedFormula,
                        inputValues = calcState.inputValues,
                        onInputChange = formulaViewModel::updateInput,
                        onCalculate = formulaViewModel::calculateSelected,
                        onResetDefaults = { formulaViewModel.selectFormula(calcState.selectedFormula) },
                        calcState = calcState
                    )
                }
            } else {
                item {
                    RechenDojoCard(
                        dojoState = dojoState,
                        onGenerate = { formulaViewModel.generateDojoProblem(calcState.selectedFormula) },
                        onToggleHint = formulaViewModel::toggleHint,
                        onSubmit = formulaViewModel::submitDojoAnswer,
                        onReset = formulaViewModel::resetDojo
                    )
                }
            }

            item {
                IhkUnitsCheatSheetCard()
            }
        }
    }
}

@Composable
private fun FormulaChipCard(
    formula: LogisticsFormula,
    isSelected: Boolean,
    solvedCount: Long,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(210.dp)
            .clickable(onClick = onClick)
            .testTag("formula_chip_${formula.id}"),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SignalOrange.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) SignalOrange else MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formula.category,
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (formula.category == "HQ") SignalOrange else SkyCognitive
                )
                if (solvedCount > 0) {
                    Text(
                        text = "$solvedCount× gelöst",
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldMastery
                    )
                }
            }
            Text(
                text = formula.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )
            Text(
                text = formula.topicTag,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FormulaOverviewHeader(formula: LogisticsFormula) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = formula.title, style = MaterialTheme.typography.titleLarge)
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = formula.formulaNotation,
                    fontFamily = JetBrainsMonoFontFamily,
                    style = MaterialTheme.typography.titleMedium,
                    color = SignalOrange,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(
                text = formula.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FormulaCalculatorCard(
    formula: LogisticsFormula,
    inputValues: Map<String, String>,
    onInputChange: (String, String) -> Unit,
    onCalculate: () -> Unit,
    onResetDefaults: () -> Unit,
    calcState: FormulaCalculatorState
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Interaktiver Rechner", style = MaterialTheme.typography.titleMedium)
                TextButton(
                    onClick = onResetDefaults,
                    modifier = Modifier.testTag("reset_formula_inputs_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.RestartAlt,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Standardwerte")
                }
            }

            formula.inputs.forEach { field ->
                OutlinedTextField(
                    value = inputValues[field.key].orEmpty(),
                    onValueChange = { onInputChange(field.key, it) },
                    label = { Text("${field.label} (${field.unit})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("formula_input_${field.key}")
                )
            }

            Button(
                onClick = onCalculate,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("calculate_formula_button"),
                colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
            ) {
                Text("Schritt-für-Schritt berechnen")
            }

            calcState.calculationError?.let { err ->
                Text(
                    text = err,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ErrorCrimson
                )
            }

            calcState.calculationResult?.let { res ->
                Surface(
                    color = EmeraldMastery.copy(alpha = 0.12f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Ergebnis: ${res.formattedValue} ${res.unit}",
                            fontFamily = JetBrainsMonoFontFamily,
                            style = MaterialTheme.typography.titleLarge,
                            color = EmeraldMastery
                        )
                        res.steps.forEach { step ->
                            Text(text = step, style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = res.interpretation,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RechenDojoCard(
    dojoState: DojoUiState,
    onGenerate: () -> Unit,
    onToggleHint: () -> Unit,
    onSubmit: (String, String) -> Unit,
    onReset: () -> Unit
) {
    var answerInput by remember(dojoState) { mutableStateOf("") }
    var stepsInput by remember(dojoState) { mutableStateOf("") }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocalFireDepartment,
                    contentDescription = null,
                    tint = SignalOrange
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "KI-Rechen-Dojo (IHK-Praxisaufgaben)",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            when (dojoState) {
                is DojoUiState.Idle -> {
                    Text(
                        text = "Gemini generiert eine maßgeschneiderte Logistik-Rechenaufgabe zur ausgewählten Formel und prüft deinen Rechenweg.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = onGenerate,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("generate_dojo_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
                    ) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Neue IHK-Aufgabe generieren")
                    }
                }

                is DojoUiState.Generating -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = SignalOrange, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("IHK-Rechenaufgabe wird erstellt…")
                    }
                }

                is DojoUiState.Ready -> {
                    val p = dojoState.problem
                    Text(text = p.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = p.scenario, style = MaterialTheme.typography.bodyMedium)
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Gegebene Werte:", style = MaterialTheme.typography.labelLarge)
                            p.givenValues.forEach { gv ->
                                Text("• $gv", fontFamily = JetBrainsMonoFontFamily, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    Text(text = p.question, style = MaterialTheme.typography.titleMedium, color = SignalOrange)

                    TextButton(onClick = onToggleHint) {
                        Icon(Icons.Filled.Lightbulb, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (dojoState.showHint) "Tipp verbergen" else "Lösungs-Tipp anzeigen")
                    }
                    if (dojoState.showHint) {
                        Text(
                            text = p.hint,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SkyCognitive
                        )
                    }

                    OutlinedTextField(
                        value = answerInput,
                        onValueChange = { answerInput = it },
                        label = { Text("Dein Ergebnis (${p.unit})") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dojo_answer_input")
                    )
                    OutlinedTextField(
                        value = stepsInput,
                        onValueChange = { stepsInput = it },
                        label = { Text("Rechenweg (optional für KI-Feedback)") },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { onSubmit(answerInput, stepsInput) },
                        enabled = answerInput.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_dojo_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldMastery)
                    ) {
                        Text("Lösung & Rechenweg prüfen")
                    }
                }

                is DojoUiState.Evaluating -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = EmeraldMastery, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("KI-Prüfer kontrolliert deinen Rechenweg…")
                    }
                }

                is DojoUiState.Evaluated -> {
                    val eval = dojoState.evaluation
                    Surface(
                        color = if (eval.isCorrect) EmeraldMastery.copy(alpha = 0.14f) else ErrorCrimson.copy(alpha = 0.12f),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (eval.isCorrect) Icons.Filled.CheckCircle else Icons.Filled.ErrorOutline,
                                    contentDescription = null,
                                    tint = if (eval.isCorrect) EmeraldMastery else ErrorCrimson
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (eval.isCorrect) "Korrekt gelöst! +${dojoState.xpEarned} XP" else "Noch nicht exakt · +${dojoState.xpEarned} XP Übungsbonus",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Text(text = eval.feedback, style = MaterialTheme.typography.bodyMedium)
                            Text("Muster-Rechenweg:", style = MaterialTheme.typography.labelLarge)
                            eval.correctedSteps.forEach { step ->
                                Text("• $step", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(
                            onClick = onReset,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Zurück")
                        }
                        Button(
                            onClick = onGenerate,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SignalOrange)
                        ) {
                            Text("Nächste Aufgabe")
                        }
                    }
                }

                is DojoUiState.Error -> {
                    Text(
                        text = dojoState.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ErrorCrimson
                    )
                    Button(onClick = onGenerate) {
                        Text("Erneut versuchen")
                    }
                }
            }
        }
    }
}

@Composable
private fun IhkUnitsCheatSheetCard() {
    var expanded by remember { mutableStateOf(false) }
    val conversions = remember {
        listOf(
            "1 Stunde (h)" to "60 Min. = 3.600 s",
            "1 m/s" to "3,6 km/h",
            "Erdbeschleunigung g" to "9,81 m/s² (Ladungssicherung)",
            "1 kN" to "1.000 N ≈ 100 daN (100 kg)",
            "IHK-Zinsjahr" to "360 Tage (1 Monat = 30 Tage)",
            "1 m³" to "1.000 Liter (dm³)"
        )
    }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("ihk_units_cheatsheet_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Straighten,
                        contentDescription = null,
                        tint = SkyCognitive,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "IHK-Einheiten & Umrechnungs-Spickzettel",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Einklappen" else "Ausklappen"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    conversions.forEach { (left, right) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = left,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = right,
                                fontFamily = JetBrainsMonoFontFamily,
                                style = MaterialTheme.typography.labelMedium,
                                color = SkyCognitive
                            )
                        }
                    }
                }
            }
        }
    }
}
