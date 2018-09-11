package com.mdbank.repository

import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.model.metadata.DataSourceInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DataMetaInfoRepository : JpaRepository<DataMetaInfo, Long> {
    fun findBySourceInfo(source: DataSourceInfo): List<DataMetaInfo>
    fun findByParameterName(parameter: String): DataMetaInfo
}