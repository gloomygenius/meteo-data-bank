package com.mdbank.controller

import com.mdbank.service.GlobalDataService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("\${api.root.v1}/global-data")
class GlobalDataController {
    @Autowired
    lateinit var globalDataService: GlobalDataService

    @PostMapping("/fetch")
    @ApiOperation("Обновить данные с серверов naca. Формат даты: dd-MM-yyyy. endDate должно быть больше startDate хотя бы на 1 день")
    fun addTaskToUpdateData(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") startDate: LocalDate,
                            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") endDate: LocalDate,
                            @RequestParam sourceId: Long): ResponseEntity<String> {
        globalDataService.fetchFromSource(startDate, endDate, sourceId)
        return ResponseEntity("Данные успешно добавлены в очередь на обновление", HttpStatus.OK)
    }

    @GetMapping("/fetch/short-info")
    @ApiOperation("Краткая информация о запущенных обновлениях")
    fun getShortUpdateStatus(): ResponseEntity<List<String>>? {
        return null
    }
}