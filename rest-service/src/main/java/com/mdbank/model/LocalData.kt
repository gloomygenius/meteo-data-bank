package com.mdbank.model

import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.util.hourSinceYearBeginning
import com.mdbank.util.totalHoursInYear
import com.mdbank.util.year
import org.hibernate.annotations.Type
import java.time.*
import javax.persistence.*

data class LocalData(var id: Long? = null,
                     val dataMetaInfo: DataMetaInfo,
                     val position: Position,
                     val year: Year,
                     private val payload: List<Float?> = ArrayList()) {

    /**
     * Метод возвращает массив данных, где индекс - количество часов с начала года
     */
    fun getValuesAsArray(): Array<Float?>{
        return payload.toTypedArray()
    }

    /**
     * Метод возвращает значение относящееся к указанному моменту времени
     *
     * @param date момент времени по UTC+0
     */
    fun getValue(date: Instant): Float? {
        //todo зарефакторить проверку года, чтобы не забивать память
        if (date.year != this.year) {
            throw IllegalArgumentException("Invalid date for getting value. Expected year is $year, but actual value is ${date.year}")
        }
        return payload[date.hourSinceYearBeginning()]
    }

    /**
     * Последовательная итерация по всем значениям
     */
    fun forEach(consumer: (date: Instant, value: Float?) -> Unit) {
        val firstHourInYear = LocalDateTime.of(year.value, 1, 1, 0, 0)
        (0..(year.totalHoursInYear() - 1)).forEach { i ->
            val utcdate= firstHourInYear.plusHours(i.toLong()).toInstant(ZoneOffset.UTC)
            consumer(utcdate, getValue(utcdate))
        }
    }

    /**
     * Метод возвращает новый экземпляр LocalData
     *
     * @return новый объект LocalData со слитыми данными
     * @throws IllegalArgumentException если не совпдают parameter или year
     */
    fun merge(localData: LocalData): LocalData {
        if (this.dataMetaInfo != localData.dataMetaInfo) {
            throw IllegalArgumentException("Ошибка при слиянии данных: разные параметры ${dataMetaInfo.parameterName} : ${localData.dataMetaInfo.parameterName}")
        }

        if (year != localData.year) {
            throw IllegalArgumentException("Ошибка при слиянии данных: разные года $year : ${localData.year}")
        }

        val newPayload = java.util.ArrayList<Float?>(year.totalHoursInYear())

        for (i in 0 until year.totalHoursInYear()) {
            val f1 = payload[i]
            val f2 = localData.payload[i]
            newPayload.add(f1 ?: f2)
        }

        return LocalData(id, dataMetaInfo, position, year, newPayload)
    }
}