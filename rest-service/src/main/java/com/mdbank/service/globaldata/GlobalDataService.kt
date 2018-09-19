package com.mdbank.service.globaldata

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

@Service
class GlobalDataService @Autowired constructor(val dataMetaInfoRepository: DataMetaInfoRepository,
                                               val nc4Manager: Nc4Manager,
                                               val positionService: PositionService,
                                               val localDataService: LocalDataService) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Transactional
    fun updateFromFiles() {
        val parameters = dataMetaInfoRepository.findAll().map { it.parameterName }
        for (parameter in parameters) {
            val metaInfo = dataMetaInfoRepository.findByParameterName(parameter)
                    ?: throw Exception("$parameter doesn't store in DB")
            val dataForParameter = nc4Manager.getDataForParameter(parameter)
            val globalDataList = dataForParameter.map { GlobalData(metaInfo, it) }
            if (globalDataList.isNotEmpty()) {
                val allPositions = positionService.getAllPositions()
                for (globalData in globalDataList) {
                    allPositions.map { globalData.toLocalData(it) }
                            .forEach { localDataService.save(it) }
                    log.info("Global data for $parameter and date was successfully saved")
                }
            }
        }
        nc4Manager.removeFiles()
    }

    fun getDownloadLink(date: LocalDate, parameter: String): String? {
        val metaInfo = dataMetaInfoRepository.findByParameterName(parameter)
        return metaInfo?.sourceInfo?.generateLink(date)
    }
}