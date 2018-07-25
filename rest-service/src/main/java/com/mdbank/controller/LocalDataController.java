package com.mdbank.controller;

import com.mdbank.model.Position;
import com.mdbank.model.metadata.MeteoParameter;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.service.LocalDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("${api.root}/local-data")
public class LocalDataController {
    @Autowired
    LocalDataService localDataService;

    @RequestMapping(method = RequestMethod.GET, value = "/{latitude}/{longitude}/{parameter}/{year}")
    @ApiOperation("Получить данные для конкретной точки")
    public ResponseEntity<Map<LocalDateTime, Float>> getLocalData(
            @PathVariable double latitude,
            @PathVariable double longitude,
            @PathVariable NetCdfParam parameter,
            @PathVariable int year) {
        Position position = new Position(latitude, longitude);
        return new ResponseEntity<>(localDataService.getBy(position, parameter, year), HttpStatus.OK);
    }

}