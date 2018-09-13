package com.mdbank.service.nc4

import org.springframework.stereotype.Component
import ucar.nc2.NetcdfFile
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/**
 * Компонент для обработки
 */
@Component
class NetCdfProcessor {

    /**
     * Метод чтения данных из nc4 файла для заданного в аргументах параметра.
     * Если указанного параметра нет, то возвращается null
     *
     * @return трёхмернный массив [t, lat, lon], где t - время, lat - индекс широты, lon - индекс долготы
     */
    fun readFromNc4(filePath: String, parameterName: String): Map<Instant, Array<FloatArray>>? {
        return NetcdfFile.open(filePath).use { ncFile ->
            val variable = ncFile.findVariable(parameterName) ?: return@use null

            val startDate = ncFile.findGlobalAttribute("RangeBeginningDate").stringValue

            val startTime = LocalDate.parse(startDate).atTime(0, 0).toInstant(ZoneOffset.UTC)

            val timeList = (0..23).map { startTime.plus(it.toLong(), ChronoUnit.HOURS) }

            val dataFromFile = variable.read()

            return@use (0..23).associate { timeList[it] to readSeries(it, dataFromFile) }
        }
    }

    private fun readSeries(timeIndex: Int, ucarArray: ucar.ma2.Array): Array<FloatArray> {

        val index = ucarArray.index
        val maxLatIndex = 360
        val maxLongIndex = 575
        val twoDimensionalArray = Array(maxLatIndex+1) { FloatArray(maxLongIndex+1) }

        for (latIndex in 0..maxLatIndex) {
            for (longIndex in 0..maxLongIndex) {
                val netCdfIndex = index.set(timeIndex, latIndex, longIndex)
                twoDimensionalArray[latIndex][longIndex] = ucarArray.getDouble(netCdfIndex).toFloat()
            }
        }

        return twoDimensionalArray
    }
}