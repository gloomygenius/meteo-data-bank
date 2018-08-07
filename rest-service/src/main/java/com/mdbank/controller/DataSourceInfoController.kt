package com.mdbank.controller

import com.mdbank.model.metadata.DataSourceInfo
import com.mdbank.repository.DataSourceInfoRepository
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${api.root.v1}/data-source-info")
class DataSourceInfoController @Autowired constructor(private val dataSourceInfoRepository: DataSourceInfoRepository) {

    @PostMapping
    @ApiOperation("Добавить новую информацию о источнике данных")
    fun addNewDataSourceInfo(@RequestBody dataSourceInfo: DataSourceInfo): ResponseEntity<DataSourceInfo> {
        val savedDataSourceInfo = dataSourceInfoRepository.save(dataSourceInfo)
        return ResponseEntity(savedDataSourceInfo, HttpStatus.OK)
    }
}