package com.mdbank.service

import com.mdbank.controller.dto.localdata.FormattedLocalData
import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.repository.LocalDataRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year

@Service
class LocalDataService @Autowired constructor(private val localDataRepository: LocalDataRepository,
                                              private val positionService: PositionService) {
    fun getFormattedDataByPositionAndYear(latitude: Double, longitude: Double, year: Year): FormattedLocalData {
        val position = positionService.getPosition(latitude, longitude)
        val localData = localDataRepository.findByPositionAndYear(position, year)
        return FormattedLocalData(localData ?: interpolateLocalData(position, year))
    }

    fun interpolateLocalData(position: Position, year: Year): LocalData {
        TODO("Реализовать интерполяцию локальных данных")
    }

    @Transactional
    fun save(localData: LocalData): LocalData {
        return localDataRepository.save(localData)
    }
}