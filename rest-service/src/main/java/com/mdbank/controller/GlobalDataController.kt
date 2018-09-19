package com.mdbank.controller

import com.mdbank.service.globaldata.GlobalDataService
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.annotation.PreDestroy

@RestController
@RequestMapping("\${api.root.v1}/global-data")
class GlobalDataController @Autowired constructor(val globalDataService: GlobalDataService) {
    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)

    @PreDestroy
    fun deinit() = executorService.shutdown()

    @PostMapping("/from-files")
    @ApiOperation("Обновление всех параметров на основе файлов, лежащих в соответствующей директории")
    fun updateFromFiles(): ResponseEntity<String> {
        executorService.execute { globalDataService.updateFromFiles() }
        return ResponseEntity("Task was added to queue", HttpStatus.OK)
    }

    @GetMapping("/download-link")
    @ApiOperation("Ссылка на скачивание nc4 файла")
    fun getLink(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") date: LocalDate,
                @RequestParam parameter: String): ResponseEntity<String> {
        return ResponseEntity(globalDataService.getDownloadLink(date, parameter) ?: "Link doesn't exist", HttpStatus.OK)
    }
}