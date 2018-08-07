package com.mdbank.util

import java.time.Instant
import java.time.Year
import java.time.ZoneOffset

fun Year.totalHoursInYear(): Int = this.length() * 24

fun Instant.hourSinceYearBeginning(): Int {
    val atZone = this.atZone(ZoneOffset.UTC)
    return atZone.hour + (atZone.dayOfYear - 1) * 24
}

val Instant.year: Year
    get() = this.atZone(ZoneOffset.UTC).year.let { Year.of(it) }