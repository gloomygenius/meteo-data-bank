package com.mdbank.controller;

import com.mdbank.model.metadata.NasaServer;
import com.mdbank.service.GlobalDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("${api.root}/global-data")
public class GlobalDataController {
    private final GlobalDataService globalDataService;

    @Autowired
    public GlobalDataController(GlobalDataService globalDataService) {
        this.globalDataService = globalDataService;
    }

    @RequestMapping(method = POST, value = "/update/{nasaServer}/{startDate}/{endDate}")
    @ApiOperation("Обновить данные с определённого сервера. Формат даты: yyyy-MM-dd. " +
            "endDate должно быть больше startDate хотя бы на 1 день")
    @Secured("ADMIN")
    public ResponseEntity<String> updateData(@PathVariable NasaServer nasaServer,
                                             @PathVariable String startDate,
                                             @PathVariable String endDate) {
        globalDataService.updateData(nasaServer, startDate, endDate);
        return new ResponseEntity<>("Данные успешно добавлены в очередь на обновление", HttpStatus.OK);
    }

    @RequestMapping(method = GET, value = "/update/short-info")
    @ApiOperation("Краткая информация о запущенных обновлениях")
    @Secured("ADMIN")
    public ResponseEntity<List<String>> getShortUpdateStatus() {
        return new ResponseEntity<>(globalDataService.getShortStatusOfUpdate(), HttpStatus.OK);
    }
}
