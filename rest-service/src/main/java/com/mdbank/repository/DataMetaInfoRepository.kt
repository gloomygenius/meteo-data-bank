package com.mdbank.repository

import com.mdbank.model.metadata.DataMetaInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DataMetaInfoRepository : JpaRepository<DataMetaInfo, Long>