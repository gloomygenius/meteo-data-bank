package com.mdbank.service

import com.mdbank.model.GlobalData
import com.mdbank.model.metadata.DataMetaInfo
import java.time.Instant

fun rectangleDataToAzimuth(eastWardData: GlobalData, northWardData: GlobalData, newMetaInfo: DataMetaInfo): GlobalData {

    val newData = HashMap<Instant, Array<FloatArray>>()

    for (instant in eastWardData.data.keys) {
        val eastWardArray = eastWardData.data[instant]!!
        val northWardArray = northWardData.data[instant]!!
        val newArray = Array(361) { FloatArray(576) }
        for (i in 0..360) {
            for (j in 0..575) {
                val a = eastWardArray[i][j].toDouble()
                val b = northWardArray[i][j].toDouble()
                newArray[i][j] = (Math.atan2(a, -b) * 180 / Math.PI + 180).toFloat()
            }
        }
        newData[instant] = newArray
    }

    return GlobalData(newMetaInfo, newData)
}

/**
 * Метод предназначен для преобразования двух массивов данных о скоростях ветра (северная и восточная составляющие)
 * в единое значение скорости ветра. При этом теряется информация о направлении ветра.
 * V=sqrt(v*v+u*u); Для вычисления навправления ветра необходимо использовать функцию rectangleDataToAzimuth
 *
 */
fun rectangleDataToRadius(eastWardData: GlobalData, northWardData: GlobalData, newMetaInfo: DataMetaInfo): GlobalData {

    val newData = HashMap<Instant, Array<FloatArray>>()

    for (instant in eastWardData.data.keys) {
        val array1 = eastWardData.data[instant]!!
        val array2 = northWardData.data[instant]!!

        for (i in 0..360) {
            for (j in 0..575) {
                val a = array1[i][j].toDouble()
                val b = array2[i][j].toDouble()
                array1[i][j] = Math.sqrt(a * a + b * b).toFloat()
            }
        }
        newData[instant] = array1
    }

    return GlobalData(newMetaInfo, newData)
}