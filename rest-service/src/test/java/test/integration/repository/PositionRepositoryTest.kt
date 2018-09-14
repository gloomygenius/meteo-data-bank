package test.integration.repository

import com.mdbank.Application
import com.mdbank.model.Position
import com.mdbank.repository.LocalDataRepository
import com.mdbank.repository.PositionRepository
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@TestPropertySource("classpath:/application-test.properties")
@ContextConfiguration(classes = [Application::class])
open class PositionRepositoryTest {
    @Autowired
    lateinit var positionRepository: PositionRepository
    @Autowired
    lateinit var localDataRep: LocalDataRepository

    @Test
    fun testSavePosition() {
        val position = Position(latitude = 25.0, longitude = 50.0).let { positionRepository.save(it) }
        assertNotNull(position.id)
        positionRepository.delete(position)
    }

    @Test(expected = DataIntegrityViolationException::class)
    fun testSaveTwoEqualPositions() {
        val position1 = Position(latitude = 24.0, longitude = 50.0).let { positionRepository.save(it) }
        val position2 = Position(latitude = 24.0, longitude = 50.0).let { positionRepository.save(it) }
    }
}