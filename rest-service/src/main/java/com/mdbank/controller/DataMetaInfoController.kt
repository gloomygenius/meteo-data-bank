package com.mdbank.controller

import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.repository.DataMetaInfoRepository
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("\${api.root.v1}/data-meta-info")
class DataMetaInfoController @Autowired constructor(val dataMetaInfoRepository: DataMetaInfoRepository) {
    @PostMapping
    @ApiOperation("Добавить новую информацию о метео-параметре")
    fun addNewDataMetaInfo(@RequestBody dataMetaInfo: DataMetaInfo): ResponseEntity<DataMetaInfo> {
        val savedDataMetaInfo = dataMetaInfoRepository.save(dataMetaInfo)
        return ResponseEntity(savedDataMetaInfo, HttpStatus.OK)
    }

    @GetMapping
    @ApiOperation("Получить информацию о метео-параметре по названию параметра")
    fun getDataMetaInfo(@RequestParam parameter: String): ResponseEntity<out Any> {
        return dataMetaInfoRepository.findByParameterName(parameter)?.let { ResponseEntity(it, HttpStatus.OK) }
                ?: ResponseEntity("Parameter was not found", HttpStatus.NOT_FOUND)
    }

    @GetMapping("/{id}")
    @ApiOperation("Получить информацию о метео параметре по ID")
    fun getDataMetaInfo(@PathVariable id: Long): ResponseEntity<out Any> {
        return dataMetaInfoRepository.findById(id).map { ResponseEntity<Any>(it, HttpStatus.OK) }
                .orElseGet { ResponseEntity("Parameter was not found", HttpStatus.NOT_FOUND) }
    }

    @GetMapping("/all")
    @ApiOperation("Получить информацию о всех метео параметрах")
    fun getDataMetaInfoList(): ResponseEntity<List<DataMetaInfo>> {
        return ResponseEntity(dataMetaInfoRepository.findAll(), HttpStatus.OK)
    }
}