package com.mdbank.service

import com.mdbank.controller.dto.localdata.FormattedLocalData
import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.model.transformIndexToLat
import com.mdbank.model.transformIndexToLon
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.repository.LocalDataRepository
import com.mdbank.util.forEachHour
import com.mdbank.util.hourSinceYearBeginning
import com.mdbank.util.totalHoursInYear
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.Year

@Service
class LocalDataService @Autowired constructor(private val localDataRepository: LocalDataRepository,
                                              private val positionService: PositionService,
                                              private val metaInfoRepository: DataMetaInfoRepository) {
    @Transactional(readOnly = true)
    fun getFormattedDataByPositionAndYear(latitude: Double, longitude: Double, year: Year, parameter: String): FormattedLocalData? {
        val position = positionService.getPosition(latitude, longitude)
        val metaInfo = metaInfoRepository.findByParameterName(parameter)!! //FIXME
        val localData = if (position != null) {
            localDataRepository.findByPositionAndYearAndDataMetaInfo(position, year, metaInfo)
        } else {
            interpolateLocalData(latitude, longitude, year, parameter)
        }
        return localData?.let { FormattedLocalData(it) }
    }

    /**
     * Метод для биллинейной интерполяции данных
     */
    fun interpolateLocalData(latitude: Double, longitude: Double, year: Year, parameter: String): LocalData {
        val doubleLatIndex = (latitude + 90) * 2
        val roundedUpLat = transformIndexToLat(Math.ceil(doubleLatIndex).toInt())
        val roundedDownLat = transformIndexToLat(Math.floor(doubleLatIndex).toInt())

        val doubleLongIndex = (longitude + 180) / 0.625
        val roundedUpLong = transformIndexToLon(Math.ceil(doubleLongIndex).toInt())
        val roundedDownLong = transformIndexToLon(Math.floor(doubleLongIndex).toInt())

        val position1 = positionService.getPosition(roundedDownLat, roundedDownLong)!!
        val position2 = positionService.getPosition(roundedDownLat, roundedUpLong)!!
        val position3 = positionService.getPosition(roundedUpLat, roundedUpLong)!!
        val position4 = positionService.getPosition(roundedUpLat, roundedDownLong)!!

        val localData1 = findLocalData(position1, year, parameter)!!
        val localData2 = findLocalData(position2, year, parameter)!!
        val localData3 = findLocalData(position3, year, parameter)!!
        val localData4 = findLocalData(position4, year, parameter)!!

        val interpolationFunction = { instant: Instant ->
            if (localData1.getValue(instant) != null) {
                val interpolator = LinearInterpolator()
                val interpolationFunc1 = interpolator.interpolate(
                        doubleArrayOf(position1.latitude, position4.latitude),
                        doubleArrayOf(localData1.getValue(instant)!!.toDouble(), localData4.getValue(instant)!!.toDouble()))
                val interpolationFunc2 = interpolator.interpolate(
                        doubleArrayOf(position2.latitude, position3.latitude),
                        doubleArrayOf(localData2.getValue(instant)!!.toDouble(), localData3.getValue(instant)!!.toDouble()))
                val value1 = interpolationFunc1.value(latitude)
                val value2 = interpolationFunc2.value(latitude)
                val finalInterpolationFunc = interpolator.interpolate(doubleArrayOf(position1.longitude, position2.longitude), doubleArrayOf(value1, value2))
                finalInterpolationFunc.value(longitude)
            } else {
                null
            }
        }

        val result = arrayOfNulls<Float?>(year.totalHoursInYear())
        year.forEachHour { result[it.hourSinceYearBeginning()] = interpolationFunction(it)?.toFloat() }
        val interpolatedData = LocalData(null, localData1.dataMetaInfo, Position(latitude = latitude, longitude = longitude), year, result.toList())

        return interpolatedData
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

    fun findLocalData(position: Position, year: Year, parameterName: String): LocalData? {
        //todo можно передалать на один запрос с джоином
        val metaInfo = metaInfoRepository.findByParameterName(parameterName)
                ?: throw RuntimeException("Parameter $parameterName is not valid")
        return localDataRepository.findByPositionAndYearAndDataMetaInfo(position, year, metaInfo)
    }
}