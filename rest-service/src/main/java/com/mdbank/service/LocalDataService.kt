package com.mdbank.service

import com.mdbank.controller.dto.localdata.FormattedLocalData
import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.repository.LocalDataRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year

@Service
class LocalDataService @Autowired constructor(private val localDataRepository: LocalDataRepository,
                                              private val positionService: PositionService,
                                              private val metaInfoRepository: DataMetaInfoRepository) {
    @Transactional(readOnly = true)
    fun getFormattedDataByPositionAndYear(latitude: Double, longitude: Double, year: Year, parameter: String): FormattedLocalData {
        val position = positionService.getPosition(latitude, longitude) ?: throw Exception("Illegal position")
        val metaInfo = metaInfoRepository.findByParameterName(parameter)!! //FIXME
        val localData = localDataRepository.findByPositionAndYearAndDataMetaInfo(position, year, metaInfo)
        return FormattedLocalData(localData ?: throw Exception("Lol kek"))
    }

    fun interpolateLocalData(position: Position, year: Year): LocalData {
        TODO("Реализовать интерполяцию локальных данных")
    }

    @Transactional
    fun save(localData: LocalData): LocalData {
        return localDataRepository.save(localData)
    }
}