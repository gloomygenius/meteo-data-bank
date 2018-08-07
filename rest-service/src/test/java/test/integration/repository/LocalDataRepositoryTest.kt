package test.integration.repository

import com.mdbank.Application
import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.repository.LocalDataRepository
import com.mdbank.repository.PositionRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@TestPropertySource("classpath:/application-test.properties")
@Transactional
@ContextConfiguration(classes = [Application::class])
open class LocalDataRepositoryTest {
    @Autowired
    private lateinit var localDataRepository: LocalDataRepository
    @Autowired
    private lateinit var positionRepository: PositionRepository
    @Autowired
    private lateinit var dataMetaInfoRepository: DataMetaInfoRepository

    @Test
    fun testInsertLocalDataSuccessfully() {
        val payload = Arrays.asList(2f, 3f, 4f)
        val position = addSimplePositionToDb()
        val dataMetaInfo = addSimpleDataMetaInfoToDb()
        val localData = LocalData(payload = payload, dataMetaInfo = dataMetaInfo, year = Year.now(), position = position)

        val savedData = localDataRepository.save(localData)

        assertNotNull(savedData)
        assertEquals(localData.payload, savedData.payload)
    }


    @Test
    fun testFindLocalDataByPositionAndYear() {
        val payload = Arrays.asList(2f, 3f, 4f)
        val position = addSimplePositionToDb()
        val dataMetaInfo = addSimpleDataMetaInfoToDb()
        val localData = LocalData(payload = payload, dataMetaInfo = dataMetaInfo, year = Year.of(2018), position = position)
        val savedData = localDataRepository.save(localData)

        val searchedLocalData = localDataRepository.findByPositionAndYear(position, Year.of(2018))
        assertNotNull(searchedLocalData)
        assertEquals(savedData, searchedLocalData)
    }

    private fun addSimplePositionToDb(): Position = Position(latitude = 30.0, longitude = 30.0).let { positionRepository.save(it) }

    private fun addSimpleDataMetaInfoToDb(): DataMetaInfo {
        return DataMetaInfo(
                description = "Какие-то данные",
                parameterName = "WIND",
                resolution = 60)
                .also { dataMetaInfoRepository.save(it) }
    }
}