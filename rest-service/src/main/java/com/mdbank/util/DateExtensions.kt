package com.mdbank.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.Year
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

fun Year.totalHoursInYear(): Int = this.length() * 24

fun Instant.hourSinceYearBeginning(): Int {
    val atZone = this.atZone(ZoneOffset.UTC)
    return atZone.hour + (atZone.dayOfYear - 1) * 24
}

val Instant.year: Year
    get() = this.atZone(ZoneOffset.UTC).year.let { Year.of(it) }


fun Year.forEachHour(function: (Instant) -> Unit) {
    val startHour = LocalDateTime.of(this.value, 1, 1, 0, 0).toInstant(ZoneOffset.UTC)
    for (hour in 0L until this.totalHoursInYear()) {
        function(startHour.plus(hour, ChronoUnit.HOURS))
    }
}