package com.mdbank.controller.dto.localdata

import com.mdbank.model.LocalData
import com.mdbank.model.metadata.DataMetaInfo
import java.time.LocalDateTime

class FormattedLocalData(localData: LocalData) {
    val dataMetaInfo: DataMetaInfo = localData.dataMetaInfo
    val dataSeries: Map<String, String>

    init {
        val data = LinkedHashMap<String, String>()
        val startDateTime = LocalDateTime.of(localData.year.value, 1, 1, 0, 0)
        localData.payload.withIndex().forEach { (index, value) ->
            val formattedTime = startDateTime.plusHours(index.toLong()).toString()
            data[formattedTime] = value.toString()
        }
        this.dataSeries = data
    }
}