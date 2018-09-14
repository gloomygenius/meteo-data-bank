package com.mdbank.controller

import com.mdbank.controller.dto.localdata.FormattedLocalData
import com.mdbank.service.LocalDataService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Year

@RestController
@RequestMapping("\${api.root.v1}/local-data")
class LocalDataController {
    @Autowired
    private lateinit var localDataService: LocalDataService

    @GetMapping
    @ApiOperation("Поиск данных для конкретной географической точки, года и метео параметра")
    @Transactional(readOnly = true)
    fun getLocalDataByPositionAndYear(@RequestParam latitude: Double,
                                      @RequestParam longitude: Double,
                                      @RequestParam year: Year,
                                      @RequestParam parameter: String): ResponseEntity<FormattedLocalData> {
        val dataByPositionAndYear = localDataService.getFormattedDataByPositionAndYear(latitude, longitude, year, parameter)
        return dataByPositionAndYear?.let { ResponseEntity.ok(dataByPositionAndYear) }
                ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }
}