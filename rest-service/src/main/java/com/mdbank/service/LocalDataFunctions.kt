package com.mdbank.service

import com.mdbank.model.LocalData
import com.mdbank.util.totalHoursInYear
import java.util.*

/**
 * Метод возвращает новый экземпляр LocalData
 *
 * @return новый объект LocalData со слитыми данными
 * @throws IllegalArgumentException если не совпдают parameter или year
 */
fun LocalData.merge(localData: LocalData): LocalData {
    if (this.dataMetaInfo != localData.dataMetaInfo) {
        throw IllegalArgumentException("Ошибка при слиянии данных: разные параметры ${dataMetaInfo.parameterName} : ${localData.dataMetaInfo.parameterName}")
    }

    if (year != localData.year) {
        throw IllegalArgumentException("Ошибка при слиянии данных: разные года $year : ${localData.year}")
    }

    val newPayload = ArrayList<Float?>(year.totalHoursInYear())

    for (i in 0 until year.totalHoursInYear()) {
        val f1 = payload[i]
        val f2 = localData.payload[i]
        newPayload.add(f1?:f2)
    }

    return LocalData(id, dataMetaInfo, position, year, newPayload)
}