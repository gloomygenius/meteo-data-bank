package com.mdbank.controller

import com.mdbank.service.globaldata.GlobalDataService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("\${api.root.v1}/global-data")
class GlobalDataController @Autowired constructor(val globalDataService: GlobalDataService) {
    @GetMapping("/from-files")
    @ApiOperation("Обновление из файлов")
    fun updateFromFiles(parameters: Array<String>): ResponseEntity<String> {
        globalDataService.updateFromFiles(parameters)
        return ResponseEntity("Success", HttpStatus.OK)
    }

    @GetMapping("/download-link")
    @ApiOperation("Ссылка на скачивание nc4 файла")
    fun getLink(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") date: LocalDate,
                @RequestParam parameter: String): ResponseEntity<String> {
        return ResponseEntity(globalDataService.getDownloadLink(date, parameter) ?: "Link doesn't exist", HttpStatus.OK)
    }
}