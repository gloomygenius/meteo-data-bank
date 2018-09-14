package com.mdbank.controller

import com.mdbank.controller.dto.localdata.FormattedLocalData
import com.mdbank.controller.dto.localdata.LocalDataRequestDto
import com.mdbank.service.LocalDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.Year

@RestController
@RequestMapping("\${api.root.v1}/local-data")
class LocalDataController {
    @Autowired
    private lateinit var localDataService: LocalDataService

    @GetMapping
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