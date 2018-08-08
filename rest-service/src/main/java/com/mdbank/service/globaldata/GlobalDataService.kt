package com.mdbank.service.globaldata

import com.mdbank.exception.repository.EntityNotFoundException
import com.mdbank.model.FetchDataTask
import com.mdbank.repository.DataSourceInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

@Service
class GlobalDataService @Autowired constructor(val dataSourceInfoRepository: DataSourceInfoRepository,
                                               val taskQueue: TaskQueue) {

    /**
     * Добавление задания на обновление данных для диапазона дат \[startDate..endDate\]
     *
     * @param sourceId - идентификатор сущности источника данных
     */
    @Transactional
    fun fetchFromSource(startDate: LocalDate, endDate: LocalDate, sourceId: Long) {
        val sourceInfo = dataSourceInfoRepository.findById(sourceId)
                .orElseThrow { EntityNotFoundException("DataMetaSource with id $sourceId doesn't exist") }

        val numberOfDaysBetweenDates = DAYS.between(startDate, endDate)

        for (dayNumber in 0..(numberOfDaysBetweenDates)) {
            FetchDataTask(startDate.plusDays(dayNumber), sourceInfo)
                    .let { taskQueue.putNew(it) }
        }
    }
}