package test.integration.service

import com.mdbank.Application
import com.mdbank.exception.InitializationException
import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.repository.PositionRepository
import com.mdbank.service.LocalDataService
import com.mdbank.util.totalHoursInYear
import org.junit.Assert
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

@RunWith(SpringRunner::class)
@SpringBootTest
@TestPropertySource("classpath:/application-test.properties")
@Transactional
@ContextConfiguration(classes = [Application::class])
open class LocalDataServiceTest {
    @Autowired
    private lateinit var positionRepository: PositionRepository
    @Autowired
    private lateinit var dataMetaInfoRepository: DataMetaInfoRepository
    @Autowired
    private lateinit var localDataService: LocalDataService

    private lateinit var position: Position
    private lateinit var dataMetaInfo: DataMetaInfo

    @Before
    fun init() {
        position = positionRepository.findByLatitudeAndLongitude(10.0, 20.0) ?: throw InitializationException()
        dataMetaInfo = dataMetaInfoRepository.findByParameterName("SWGDN") ?: throw InitializationException()
    }

    /**
     * Проверяется работа метода #save() в случае, когда в БД уже существует LocalData.
     * Значения должны склеиваться и сохраняться в БД.
     */
    @Test
    fun testMergeLocalData() {
        val year = Year.of(2018)
        val payload = arrayOfNulls<Float?>(year.totalHoursInYear()).toMutableList()
        payload[25] = 1.0F
        payload[26] = 3.0F
        payload[27] = 2.0F

        val localData = LocalData(payload = payload, dataMetaInfo = dataMetaInfo, year = year, position = position)
        val savedData = localDataService.save(localData)

        val searchedLocalData = localDataService.findByPositionAndYearAndParameter(position, year, dataMetaInfo.parameterName)

        Assert.assertNotNull(searchedLocalData)
        val newPayload = arrayOfNulls<Float?>(year.totalHoursInYear()).toMutableList()
        newPayload[28] = 1.0F
        newPayload[29] = 3.0F
        newPayload[30] = 2.0F

        val newLocalData = LocalData(payload = newPayload, dataMetaInfo = dataMetaInfo, year = year, position = position)

        val savedLocalData = localDataService.save(newLocalData)

        val valuesAsArray = savedLocalData.getValuesAsArray()
        for (index in 25..30) {
            Assert.assertNotNull(valuesAsArray[index])
        }
    }
}