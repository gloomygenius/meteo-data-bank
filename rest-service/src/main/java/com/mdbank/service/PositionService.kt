package com.mdbank.service

import com.mdbank.config.GlobalDataConfig
import com.mdbank.exception.InitializationException
import com.mdbank.model.Position
import com.mdbank.repository.PositionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class PositionService @Autowired constructor(globalDataConfig: GlobalDataConfig, private val repository: PositionRepository) {
    private var cachedPositions: MutableList<Position> = ArrayList()
    private var minLatitude: Double = globalDataConfig.minLatitude ?: throw InitializationException()
    private var maxLatitude: Double = globalDataConfig.maxLatitude ?: throw InitializationException()
    private var minLongitude: Double = globalDataConfig.minLongitude ?: throw InitializationException()
    private var maxLongitude: Double = globalDataConfig.maxLongitude ?: throw InitializationException()
    private val latStep: Double = globalDataConfig.latStep
    private val lonStep: Double = globalDataConfig.lonStep
    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    internal fun init() {
        if (minLatitude > maxLatitude) {
            throw  InitializationException()
        }

        if (minLongitude > maxLongitude) {
            throw  InitializationException()
        }

        val aroundLat: (Double) -> Double = { lat -> transformLatToIndex(lat).let { index -> transformIndexToLat(index) } }

        aroundLat(minLatitude).also {
            if (it != minLatitude) {
                log.warn("Min latitude was rounded from $minLatitude to $it")
                minLatitude = it
            }
        }

        aroundLat(maxLatitude).also {
            if (it != maxLatitude) {
                log.warn("Max latitude was rounded from $maxLatitude to $it")
                maxLatitude = it
            }
        }

        val aroundLongitude: (Double) -> Double = { lat -> transformLonToIndex(lat).let { index -> transformIndexToLon(index) } }

        aroundLongitude(minLongitude).also {
            if (it != minLongitude) {
                log.warn("Min longitude was rounded from $minLongitude to $it")
                minLongitude = it
            }
        }

        aroundLongitude(maxLongitude).also {
            if (it != maxLongitude) {
                log.warn("Max longitude was rounded from $maxLongitude to $it")
                maxLongitude = it
            }
        }

    }

    fun forEachPosition(consumer: (Position) -> Unit) {
        if (cachedPositions.isEmpty()) {
            loadAllPositionFromDB()
        }
        cachedPositions.forEach(consumer)
    }

    private fun loadAllPositionFromDB() {
        for (latIndex in transformLatToIndex(minLatitude)..transformLatToIndex(maxLatitude)) {
            for (lonIndex in transformLonToIndex(minLongitude)..transformLonToIndex(maxLongitude)) {
                val positionFromDb = repository.findByLatitudeAndLongitude(transformIndexToLat(latIndex), transformIndexToLon(lonIndex))
                if (positionFromDb == null) {
                    val newPosition = Position(latitude = transformIndexToLat(latIndex), longitude = transformIndexToLon(lonIndex))
                    repository.save(newPosition).let { cachedPositions.add(it)}
                } else {
                    cachedPositions.add(positionFromDb)
                }
            }
        }
    }

    fun getPosition(latitude: Double, longitude: Double): Position {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

/**
 * Индекс широты в диапазоне [0..360]
 */
val Position.latIndex: Int
    get() = transformLatToIndex(latitude)

/**
 * Индекс долготы в диапазоне [0..576]
 */
val Position.longIndex: Int
    get() = transformLonToIndex(longitude)


fun transformLatToIndex(latitude: Double): Int {
    if (latitude < -90.01 || latitude > 90.01) {
        throw IllegalArgumentException(String.format("Latitude %s out of range (-90..90)", latitude))
    }
    return Math.round((latitude + 90) * 2).toInt()
}

fun transformLonToIndex(longitude: Double): Int {
    if (longitude < -180.01 || longitude >= 180) {
        throw IllegalArgumentException(String.format("Longitude %s out of range (-180..180)", longitude))
    }
    return Math.round((longitude + 180) / 0.625).toInt()
}

fun transformIndexToLat(index: Int): Double = index / 2.0 - 90


fun transformIndexToLon(index: Int): Double = index * 0.625 - 180