package test.integration.repository

import com.mdbank.Application
import com.mdbank.exception.InitializationException
import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.repository.DataSourceInfoRepository
import com.mdbank.repository.LocalDataRepository
import com.mdbank.repository.PositionRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
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
    private lateinit var positionRepository: PositionRepository
    @Autowired
    private lateinit var dataMetaInfoRepository: DataMetaInfoRepository
    @Autowired
    private lateinit var localDataRepository: LocalDataRepository

    private lateinit var position: Position
    private lateinit var dataMetaInfo: DataMetaInfo

    @Before
    fun init() {
        position = positionRepository.findByLatitudeAndLongitude(10.0, 20.0) ?: throw InitializationException()
        dataMetaInfo = dataMetaInfoRepository.findByParameterName("SWGDN") ?: throw InitializationException()
    }

    @Test
    fun testInsertLocalDataSuccessfully() {
        val payload = arrayOfNulls<Float?>(365).toMutableList()
        payload[25] = 1.0F
        payload[26] = 3.0F
        payload[27] = 2.0F

        val localData = LocalData(payload = payload, dataMetaInfo = dataMetaInfo, year = Year.now(), position = position)

        val savedData = localDataRepository.save(localData)

        assertNotNull(savedData)
        assertEquals(localData, savedData)
    }


    @Test
    fun testFindLocalDataByPositionAndYear() {
        val payload = arrayOfNulls<Float?>(365).toMutableList()
        payload[25] = 1.0F
        payload[26] = 3.0F
        payload[27] = 2.0F

        val localData = LocalData(payload = payload, dataMetaInfo = dataMetaInfo, year = Year.of(2018), position = position)
        val savedData = localDataRepository.save(localData)

        val searchedLocalData = localDataRepository.findByPositionAndYearAndDataMetaInfo(position, Year.of(2018), dataMetaInfo)

        assertNotNull(searchedLocalData)
        assertEquals(savedData, searchedLocalData)
    }
}