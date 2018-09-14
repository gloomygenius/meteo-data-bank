package com.mdbank.controller

import com.mdbank.model.metadata.DataSourceInfo
import com.mdbank.repository.DataSourceInfoRepository
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.root.v1}/data-source-info")
class DataSourceInfoController @Autowired constructor(private val dataSourceInfoRepository: DataSourceInfoRepository) {

    @PostMapping
    @ApiOperation("Добавить новую информацию о источнике данных")
    fun addNewDataSourceInfo(@RequestBody dataSourceInfo: DataSourceInfo): ResponseEntity<DataSourceInfo> {
        val savedDataSourceInfo = dataSourceInfoRepository.save(dataSourceInfo)
        return ResponseEntity(savedDataSourceInfo, HttpStatus.OK)
    }

    @PutMapping
    @ApiOperation("Обновить информацию о существующем источнике данных")
    fun updateDataSourceInfo(@RequestBody dataSourceInfo: DataSourceInfo): ResponseEntity<Any> {
        if (dataSourceInfo.id == null || !dataSourceInfoRepository.existsById(dataSourceInfo.id)) {
            return ResponseEntity("Should be specified correct ID", HttpStatus.BAD_REQUEST)
        }
        val savedDataSourceInfo = dataSourceInfoRepository.save(dataSourceInfo)
        return ResponseEntity(savedDataSourceInfo, HttpStatus.OK)
    }

    @GetMapping("/{id}")
    @ApiOperation("Получить информацию о источнике данных по ID")
    fun getDataSourceInfo(@PathVariable id: Long): ResponseEntity<out Any> {
        return dataSourceInfoRepository.findById(id).map { ResponseEntity<Any>(it, HttpStatus.OK) }
                .orElseGet { ResponseEntity("Data source was not found", HttpStatus.NOT_FOUND) }
    }

    @GetMapping("/all")
    @ApiOperation("Получить информацию о всех источниках данных")
    fun getDataMetaInfoList(): ResponseEntity<List<DataSourceInfo>> {
        return ResponseEntity(dataSourceInfoRepository.findAll(), HttpStatus.OK)
    }
}