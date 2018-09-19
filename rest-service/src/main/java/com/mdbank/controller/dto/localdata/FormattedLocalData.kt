package com.mdbank.controller.dto.localdata

import com.mdbank.model.LocalData
import com.mdbank.model.metadata.DataMetaInfo
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class FormattedLocalData(localData: LocalData) {
    val dataMetaInfo: DataMetaInfo = localData.dataMetaInfo
    val dataSeries: Map<String, String>

    init {
        val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
        val data = LinkedHashMap<String, String>()
        localData.forEach { instant, value ->
            if (value != null) {
                data[formatter.format(ZonedDateTime.ofInstant(instant, ZoneOffset.UTC))] = value.toString()
            }
        }
        this.dataSeries = data
    }
}