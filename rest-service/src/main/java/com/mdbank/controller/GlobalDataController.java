package com.mdbank.controller;

import com.mdbank.model.metadata.NasaServer;
import com.mdbank.service.GlobalDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("${api.root}/global-data")
public class GlobalDataController {
    private final GlobalDataService globalDataService;

    @Autowired
    public GlobalDataController(GlobalDataService globalDataService, MessageSource messageSource) {
        this.globalDataService = globalDataService;
    }

    @PostMapping("/update/{nasaServer}/{startDate}/{endDate}")
    @ApiOperation("Обновить данные с определённого сервера. Формат даты: yyyy-MM-dd. " +
            "endDate должно быть больше startDate хотя бы на 1 день")
    @Secured("ADMIN")
    public ResponseEntity<String> updateData(@PathVariable NasaServer nasaServer,
                                             @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                             @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
        globalDataService.addGlobalDataUpdateBatchTask(nasaServer, startDate, endDate);
        return new ResponseEntity<>("Данные успешно добавлены в очередь на обновление", HttpStatus.OK);
    }

    @GetMapping("/update/short-info")
    @ApiOperation("Краткая информация о запущенных обновлениях")
    @Secured("ADMIN")
    public ResponseEntity<List<String>> getShortUpdateStatus() {
        return new ResponseEntity<>(globalDataService.getShortStatusOfUpdate(), HttpStatus.OK);
    }
}
