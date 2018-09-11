package com.mdbank.service.globaldata

import com.mdbank.exception.repository.EntityNotFoundException
import com.mdbank.model.FetchDataTask
import com.mdbank.model.GlobalData
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.repository.DataSourceInfoRepository
import com.mdbank.service.LocalDataService
import com.mdbank.service.PositionService
import com.mdbank.service.nc4.Nc4Manager
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.ChronoUnit.DAYS

@Service
class GlobalDataService @Autowired constructor(val dataSourceInfoRepository: DataSourceInfoRepository,
                                               val dataMetaInfoRepository: DataMetaInfoRepository,
                                               val taskQueue: TaskQueue,
                                               val nc4Manager: Nc4Manager,
                                               val positionService: PositionService,
                                               val localDataService: LocalDataService) {

    private val log = LoggerFactory.getLogger(this.javaClass)

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

    @Transactional
    fun updateFromFiles(parameters: List<String>) {
        for (parameter in parameters) {
            val metaInfo = dataMetaInfoRepository.findByParameterName(parameter)
            val dataForParameter = nc4Manager.getDataForParameter(parameter)
            val globalDataList = dataForParameter.map { GlobalData(metaInfo, it) }
            if (globalDataList.isNotEmpty()) {
                val allPositions = positionService.getAllPositions()
                for (globalData in globalDataList) {
                    allPositions.map { globalData.toLocalData(it) }.forEach { localDataService.save(it) }
                    log.info("Global data for $parameter and date was successfully saved")
                }
            }
        }
    }
}