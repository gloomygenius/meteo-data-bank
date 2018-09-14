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
    fun getFormattedDataByPositionAndYear(latitude: Double, longitude: Double, year: Year, parameter: String): FormattedLocalData? {
        val position = positionService.getPosition(latitude, longitude) ?: throw Exception("Illegal position")
        val metaInfo = metaInfoRepository.findByParameterName(parameter)!! //FIXME
        val localData = localDataRepository.findByPositionAndYearAndDataMetaInfo(position, year, metaInfo)
        return localData?.let { FormattedLocalData(it) }
    }

    fun interpolateLocalData(position: Position, year: Year): LocalData {
        TODO("Реализовать интерполяцию локальных данных")
    }

    /**
     * Метод сохраняет объект LocalData. Если для текущей позиции, года и dataMetaInfo нет сохранённого хначения,
     * то LocalData просто сохраняется в БД. Если в БД уже существует такая запись, то данные payload двух объектов
     * сливаются и запись обновляется
     */
    @Transactional
    fun save(localData: LocalData): LocalData {
        val existingLocalData = localDataRepository.findByPositionAndYearAndDataMetaInfo(localData.position, localData.year, localData.dataMetaInfo)
        var localDataToStore = localData
        if (existingLocalData != null) {
            localDataToStore = existingLocalData.merge(localData)
        }
        return localDataRepository.save(localDataToStore)
    }

    fun findByPositionAndYearAndParameter(position: Position, year: Year, parameterName: String): LocalData? {
        //todo можно передалать на один запрос с джоином
        val metaInfo = metaInfoRepository.findByParameterName(parameterName) ?: throw RuntimeException("Parameter $parameterName is not valid")
        return localDataRepository.findByPositionAndYearAndDataMetaInfo(position, year, metaInfo)
    }
}