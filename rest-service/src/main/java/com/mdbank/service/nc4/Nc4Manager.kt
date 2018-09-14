package com.mdbank.service.nc4

import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Instant
import javax.annotation.PostConstruct
import kotlin.streams.toList

@Component
class Nc4Manager @Autowired constructor(private val processor: NetCdfProcessor) {
    private val appPath: Path
    private val log: Logger = LoggerFactory.getLogger(Nc4Manager::class.java)

    init {
        val homeUserPath = Paths.get(System.getProperty("user.home"))
        appPath = homeUserPath.resolve("meteo_data_bank/global_data/")
    }

    fun getDataForParameter(parameter: String): List<Map<Instant, Array<FloatArray>>> {
        return Files.list(appPath)
                .map { path -> processor.readFromNc4(path.toString(), parameter) }
                .toList().filterNotNull()
    }

    fun removeFiles() {
        try {
            //clean old files in directory
            if (Files.exists(appPath)) {
                FileUtils.cleanDirectory(appPath.toFile())
            }
        } catch (e: IOException) {
            log.error("Directory with nc4 files ( {} )can't be cleaned", appPath, e)
        }
    }
}