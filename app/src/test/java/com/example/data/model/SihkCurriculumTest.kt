package com.example.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SihkCurriculumTest {

    @Test
    fun meisterRank_resolvesThresholdsCorrectly() {
        assertEquals(MeisterRank.LAGERHELFER, MeisterRank.fromXp(0L))
        assertEquals(MeisterRank.FACHKRAFT, MeisterRank.fromXp(500L))
        assertEquals(MeisterRank.SCHICHTFUEHRER, MeisterRank.fromXp(1500L))
        assertEquals(MeisterRank.KOORDINATOR, MeisterRank.fromXp(3500L))
        assertEquals(MeisterRank.LOGISTIKMEISTER, MeisterRank.fromXp(7000L))
    }

    @Test
    fun ihkGrade_followsOfficial100PointScale() {
        assertEquals(1, SihkCurriculum.calculateIhkGrade(92))
        assertEquals(2, SihkCurriculum.calculateIhkGrade(81))
        assertEquals(3, SihkCurriculum.calculateIhkGrade(67))
        assertEquals(4, SihkCurriculum.calculateIhkGrade(50))
        assertEquals(5, SihkCurriculum.calculateIhkGrade(49))
        assertEquals(6, SihkCurriculum.calculateIhkGrade(29))
    }

    @Test
    fun supplementaryOralTarget_calculatesTwoToOnePassingScore() {
        assertEquals(66, SihkCurriculum.supplementaryOralTarget(42))
        assertEquals(70, SihkCurriculum.supplementaryOralTarget(40))
        assertEquals(90, SihkCurriculum.supplementaryOralTarget(30))
        assertEquals(null, SihkCurriculum.supplementaryOralTarget(50))
        assertEquals(null, SihkCurriculum.supplementaryOralTarget(29))
    }
}
