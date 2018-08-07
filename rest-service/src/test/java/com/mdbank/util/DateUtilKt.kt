package com.mdbank.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class DateUtilKt {
    @Test
    fun testThatHourSinceYearBeginningReturnTrueValue() {
        val instant1 = LocalDateTime.of(2018, 1, 1, 0, 1).toInstant(ZoneOffset.UTC)
        val firstHourInYear = 0
        assertEquals(firstHourInYear, instant1.hourSinceYearBeginning())

        val instant2 = LocalDateTime.of(2018, 12, 31, 23, 1).toInstant(ZoneOffset.UTC)
        val lastHourInYear = 8759
        assertEquals(lastHourInYear, instant2.hourSinceYearBeginning())
    }
}