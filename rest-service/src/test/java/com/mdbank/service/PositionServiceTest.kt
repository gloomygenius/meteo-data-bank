package com.mdbank.service

import com.mdbank.config.GlobalDataConfig
import com.mdbank.model.Position
import com.mdbank.repository.PositionRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import java.util.concurrent.atomic.AtomicInteger

class PositionServiceTest {

    lateinit var positionService: PositionService

    @Before
    fun init() {
        val positionRepository = Mockito.mock(PositionRepository::class.java)
        Mockito.`when`(positionRepository.save(any(Position::class.java))).then { invocation -> invocation.arguments.first() }

        val globalDataConfig = GlobalDataConfig()
                .also { it.minLatitude = 0.0 }
                .also { it.maxLatitude = 30.0 }
                .also { it.minLongitude = 0.0 }
                .also { it.maxLongitude = 20.2 }
        positionService = PositionService(globalDataConfig = globalDataConfig, repository = positionRepository)
        positionService.initAllPositions()
    }

    @Test
    fun forEachPosition() {
        val count = AtomicInteger()
        positionService.forEachPosition { count.incrementAndGet() }
        assertEquals(2013, count.get())
        println(count)
    }
}