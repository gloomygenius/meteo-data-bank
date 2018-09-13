package com.mdbank.model

import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.util.hourSinceYearBeginning
import com.mdbank.util.totalHoursInYear
import com.mdbank.util.year
import java.time.Instant
import java.util.*


class GlobalData(val metaInfo: DataMetaInfo,
                 val data: Map<Instant, Array<FloatArray>>) {

    /**
     * Метод вычленения данных для определённой точки на сетке
     *
     * @param position координата точки
     * @return локальный массив данных LocalData
     */
    fun toLocalData(position: Position): LocalData {
        val instantToValueSortedMap = data.mapValues { it.value[position.latIndex][position.longIndex] }

        val countOfYearsInKeys = instantToValueSortedMap.keys.stream().distinct().count()

        if (countOfYearsInKeys != 1L) {
            throw IllegalStateException("Map contains times with different years")
        }

        val year = instantToValueSortedMap.keys.first().year

        val floats = arrayOfNulls<Float>(year.totalHoursInYear())

        instantToValueSortedMap.mapValues { floats[it.key.hourSinceYearBeginning()] = it.value }

        val values = floats.asList()

        return LocalData(dataMetaInfo = metaInfo, position = position, year = year, payload = values)
    }
}