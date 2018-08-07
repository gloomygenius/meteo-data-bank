package com.mdbank.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.concurrent.Executors
import javax.annotation.PreDestroy

@Service
open class GlobalDataService @Autowired constructor() {
    private val service = Executors.newFixedThreadPool(1)

    @PreDestroy
    private fun destroy() {
        if (!service.isShutdown) {
            service.shutdown()
        }
    }

    fun fetchFromSource(startDate: LocalDate, endDate: LocalDate, sourceId: Long?) {
//        FetchDataTask(startDate, endDate,)
    }
}