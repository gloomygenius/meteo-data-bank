package com.mdbank.model

import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.util.totalHoursInYear
import com.mdbank.util.year
import java.time.Instant
import java.time.ZoneId
import java.util.*
import java.util.stream.Collectors


class GlobalData(val metaInfo: DataMetaInfo,
                 val data: Map<Instant, Array<FloatArray>>) {

    /**
     * Метод вычленения данных для определённой точки на сетке
     *
     * @param position координата точки
     * @return локальный массив данных LocalData
     */
    fun toLocalData(position: Position): LocalData {
        val latIndex = position.latIndex
        val longIndex = position.longIndex

        val instantToValueSortedMap = data.keys.stream()
                .collect(Collectors.toMap(
                        { instant -> instant },
                        { instant -> data[instant]!![latIndex][longIndex] },
                        { _, _ -> throw IllegalStateException("Same values of map") },
                        { TreeMap<Instant, Float>() }))


        val year = instantToValueSortedMap.keys
                .maxWith(Comparator.naturalOrder())?.year
                ?: throw RuntimeException("Не определён год глобальных данных")

        val floats = arrayOfNulls<Float>(year.totalHoursInYear())

        instantToValueSortedMap.map { entry ->
            val instant = entry.key
            val utc1 = instant.atZone(ZoneId.of("UTC"))
            val utc = (utc1.dayOfYear - 1) * 24 + utc1.hour
            floats[utc] = entry.value
        }
        val values = floats.asList()

        return LocalData(dataMetaInfo = metaInfo, position = position, year = year, payload = values)
    }
}