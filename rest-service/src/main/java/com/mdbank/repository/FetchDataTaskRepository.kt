package com.mdbank.repository

import com.mdbank.model.FetchDataTask
import org.springframework.data.jpa.repository.JpaRepository

interface FetchDataTaskRepository : JpaRepository<FetchDataTask, Long>
