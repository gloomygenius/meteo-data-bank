package com.mdbank.repository

import com.mdbank.model.metadata.DataSourceInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DataSourceInfoRepository : JpaRepository<DataSourceInfo, Long>