package com.example.ui.formulas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.DojoEvaluation
import com.example.data.ai.DojoProblem
import com.example.data.ai.LogistikAiRepository
import com.example.data.formulas.FormulaCalculationResult
import com.example.data.formulas.LogisticsFormula
import com.example.data.formulas.LogisticsFormulaEngine
import com.example.data.repository.LearningRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DojoUiState {
    data object Idle : DojoUiState
    data object Generating : DojoUiState
    data class Ready(val problem: DojoProblem, val showHint: Boolean = false) : DojoUiState
    data class Evaluating(val problem: DojoProblem) : DojoUiState
    data class Evaluated(val problem: DojoProblem, val evaluation: DojoEvaluation, val xpEarned: Long) : DojoUiState
    data class Error(val message: String) : DojoUiState
}

data class FormulaCalculatorState(
    val selectedCategory: String = "ALLE",
    val selectedFormula: LogisticsFormula = LogisticsFormulaEngine.formulas.first(),
    val inputValues: Map<String, String> = LogisticsFormulaEngine.formulas.first().inputs.associate {
        it.key to it.defaultValue
    },
    val calculationResult: FormulaCalculationResult? = null,
    val calculationError: String? = null
)

class FormulaViewModel(
    private val learningRepository: LearningRepository,
    private val aiRepository: LogistikAiRepository = LogistikAiRepository()
) : ViewModel() {

    private val _calcState = MutableStateFlow(FormulaCalculatorState())
    val calcState: StateFlow<FormulaCalculatorState> = _calcState.asStateFlow()

    private val _dojoState = MutableStateFlow<DojoUiState>(DojoUiState.Idle)
    val dojoState: StateFlow<DojoUiState> = _dojoState.asStateFlow()

    init {
        calculateSelected()
    }

    fun selectCategory(category: String) {
        _calcState.update { it.copy(selectedCategory = category) }
    }

    fun selectFormula(formula: LogisticsFormula) {
        _calcState.update {
            it.copy(
                selectedFormula = formula,
                inputValues = formula.inputs.associate { field -> field.key to field.defaultValue },
                calculationError = null
            )
        }
        calculateSelected()
    }

    fun updateInput(key: String, value: String) {
        _calcState.update { state ->
            state.copy(
                inputValues = state.inputValues + (key to value),
                calculationError = null
            )
        }
    }

    fun calculateSelected() {
        val state = _calcState.value
        val parsed = mutableMapOf<String, Double>()
        for (field in state.selectedFormula.inputs) {
            val raw = state.inputValues[field.key]?.replace(",", ".")?.trim().orEmpty()
            val num = raw.toDoubleOrNull()
            if (num == null) {
                _calcState.update {
                    it.copy(
                        calculationResult = null,
                        calculationError = "Bitte gültige Zahl für '${field.label}' eingeben."
                    )
                }
                return
            }
            parsed[field.key] = num
        }

        LogisticsFormulaEngine.calculate(state.selectedFormula.id, parsed)
            .onSuccess { res ->
                _calcState.update { it.copy(calculationResult = res, calculationError = null) }
            }
            .onFailure { err ->
                _calcState.update {
                    it.copy(
                        calculationResult = null,
                        calculationError = err.localizedMessage ?: "Berechnungsfehler"
                    )
                }
            }
    }

    fun generateDojoProblem(formula: LogisticsFormula = _calcState.value.selectedFormula) {
        viewModelScope.launch {
            _dojoState.value = DojoUiState.Generating
            try {
                val problem = aiRepository.generateDojoProblem(formula)
                _dojoState.value = DojoUiState.Ready(problem = problem)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to generate Dojo problem", e)
                _dojoState.value = DojoUiState.Error(
                    e.localizedMessage ?: "KI-Aufgabe konnte nicht generiert werden."
                )
            }
        }
    }

    fun toggleHint() {
        val current = _dojoState.value
        if (current is DojoUiState.Ready) {
            _dojoState.value = current.copy(showHint = !current.showHint)
        }
    }

    fun submitDojoAnswer(answerText: String, stepsText: String) {
        val current = _dojoState.value
        val problem = (current as? DojoUiState.Ready)?.problem ?: return
        val formula = _calcState.value.selectedFormula

        viewModelScope.launch {
            _dojoState.value = DojoUiState.Evaluating(problem)
            try {
                val evaluation = aiRepository.evaluateDojoSolution(
                    problem = problem,
                    userNumberInput = answerText,
                    userStepsInput = stepsText
                )
                val xp = if (evaluation.isCorrect) 40L else 10L
                learningRepository.recordFormulaPractice(
                    formulaId = formula.id,
                    formulaName = formula.title,
                    category = formula.category,
                    correct = evaluation.isCorrect
                )
                learningRepository.awardXpAndUpdateStreak(xpDelta = xp, studyMinutesDelta = 4L)
                _dojoState.value = DojoUiState.Evaluated(
                    problem = problem,
                    evaluation = evaluation,
                    xpEarned = xp
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Failed to evaluate Dojo answer", e)
                _dojoState.value = DojoUiState.Error(
                    e.localizedMessage ?: "KI-Auswertung fehlgeschlagen."
                )
            }
        }
    }

    fun resetDojo() {
        _dojoState.value = DojoUiState.Idle
    }

    private companion object {
        const val TAG = "FormulaViewModel"
    }
}
