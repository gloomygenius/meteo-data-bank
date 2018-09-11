package com.mdbank.controller.dto.localdata

import com.mdbank.model.LocalData
import com.mdbank.model.metadata.DataMetaInfo
import java.time.Instant
import java.time.LocalDateTime

class FormattedLocalData(localData: LocalData) {
    val dataMetaInfo: DataMetaInfo = localData.dataMetaInfo
    val dataSeries: Map<String, String>

    init {
        val data = LinkedHashMap<String, String>()
        localData.forEach { utc, value -> data[utc.toString()] = value.toString() }
        this.dataSeries = data
    }
}