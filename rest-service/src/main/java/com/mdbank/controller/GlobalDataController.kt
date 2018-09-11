package com.mdbank.controller

import com.mdbank.service.globaldata.GlobalDataService
import com.mdbank.service.globaldata.TaskQueue
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("\${api.root.v1}/global-data")
class GlobalDataController @Autowired constructor(val globalDataService: GlobalDataService,
                                                  val taskQueue: TaskQueue) {
    @PostMapping("/fetch")
    @ApiOperation("Обновить данные с серверов naca. Формат даты: dd-MM-yyyy. endDate должно быть больше startDate хотя бы на 1 день")
    fun addTaskToUpdateData(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") startDate: LocalDate,
                            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") endDate: LocalDate,
                            @RequestParam sourceId: Long): ResponseEntity<String> {
        globalDataService.fetchFromSource(startDate, endDate, sourceId)
        return ResponseEntity("Данные успешно добавлены в очередь на обновление", HttpStatus.OK)
    }

    @GetMapping("/from-files")
    @ApiOperation("Обновление из файлов")
    fun updateFromFiles(parameters: List<String>): ResponseEntity<String> {
        globalDataService.updateFromFiles(parameters)
        return ResponseEntity("Success", HttpStatus.OK)
    }
}