package com.example.data.formulas

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LogisticsFormulaEngineTest {

    @Test
    fun andlerEoq_calculatesCorrectOptimalQuantity() {
        val result = LogisticsFormulaEngine.calculate(
            "andler_eoq",
            mapOf("demand" to 10000.0, "orderCost" to 120.0, "price" to 25.0, "holdingRate" to 16.0)
        )
        assertTrue(result.isSuccess)
        val res = result.getOrThrow()
        // sqrt((200 * 10000 * 120) / (25 * 16)) = sqrt(240000000 / 400) = sqrt(600000) ≈ 774.60
        assertEquals(774.60, res.value, 0.1)
        assertEquals(4, res.steps.size)
    }

    @Test
    fun lashingStraps_calculatesStrapsPerVdi2700() {
        val result = LogisticsFormulaEngine.calculate(
            "lashing_straps",
            mapOf("massKg" to 4000.0, "mu" to 0.3, "stfDaN" to 350.0, "angleDeg" to 60.0)
        )
        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().value >= 10.0)
    }

    @Test
    fun breakEven_rejectsNonPositiveMargin() {
        val result = LogisticsFormulaEngine.calculate(
            "break_even",
            mapOf("fixedCost" to 10000.0, "price" to 40.0, "varCost" to 50.0)
        )
        assertTrue(result.isFailure)
    }
}
