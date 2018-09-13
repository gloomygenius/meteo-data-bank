package com.mdbank.service

import com.mdbank.config.GlobalDataConfig
import com.mdbank.exception.InitializationException
import com.mdbank.model.Position
import com.mdbank.model.transformIndexToLat
import com.mdbank.model.transformIndexToLon
import com.mdbank.repository.PositionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

    @Transactional
    internal fun initAllPositions() {
        val minPos = Position(latitude = minLatitude, longitude = minLongitude)
        val maxPos = Position(latitude = maxLatitude, longitude = maxLongitude)

        val maxLongIndex = maxPos.longIndex
        val minLongIndex = minPos.longIndex
        val maxLatIndex = maxPos.latIndex
        val minLatIndex = minPos.latIndex

        val count = (maxLongIndex - minLongIndex + 1) * (maxLatIndex - minLatIndex + 1)

        if (count.toLong() != repository.count()) {
            val positions = HashSet<Position>()
            for (longIndex in minLongIndex..maxLongIndex) {
                for (latIndex in minLatIndex..maxLatIndex) {
                    val position = Position(latitude = transformIndexToLat(latIndex), longitude = transformIndexToLon(longIndex))
                    positions.add(position)
                }
            }

            repository.saveAll(positions)
        }
    }

    fun forEachPosition(consumer: (Position) -> Unit) {
        cachedPositions.forEach(consumer)
    }

    @Transactional(readOnly = true)
    fun getPosition(latitude: Double, longitude: Double): Position? {
        return repository.findByLatitudeAndLongitude(latitude, longitude)
    }

    @Transactional(readOnly = true)
    fun getAllPositions(): List<Position> {
        return repository.findAll()
    }
}