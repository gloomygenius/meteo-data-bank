package com.mdbank.controller.dto.localdata

import com.fasterxml.jackson.databind.ObjectMapper
import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.util.ResourceLoader
import com.mdbank.util.totalHoursInYear
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Year

class FormattedLocalDataTest {
    @Test
    fun testThatJacksonCorrectlyFormatsLocalDataObjects() {
        val objectMapper = ObjectMapper()

        val dataMetaInfo = DataMetaInfo(id = 1, description = "Какие-то данные", parameterName = "WIND", resolution = 60)

        val position = Position(latitude = 30.0, longitude = 30.0)
        val year = Year.of(2018)

        val payload = arrayOfNulls<Float>(year.totalHoursInYear()).toMutableList()
        payload[0] = 23.0F
        payload[1] = 24.0F
        payload[2] = 25.0F

        val localData = LocalData(id = 1, dataMetaInfo = dataMetaInfo, position = position, year = year, payload = payload)
        val formattedLocalData = FormattedLocalData(localData)
        val formattedJsonValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(formattedLocalData)
        val expectedJson = ResourceLoader.getResourceAsString("json/controller/local_data/expected_local_data.json")

        assertEquals(expectedJson, formattedJsonValue)
    }
}