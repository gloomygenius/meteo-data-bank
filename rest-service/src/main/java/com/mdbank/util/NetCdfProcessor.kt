package com.mdbank.util

import com.mdbank.exception.NetCdfReadException
import com.mdbank.model.GlobalData
import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ucar.nc2.NetcdfFile
import java.io.IOException
import java.nio.file.Path
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class NetCdfProcessor @Autowired constructor(private val positionService: PositionService) {

    @Throws(NetCdfReadException::class)
    private fun readFromNc4(filePath: String, dataMetaInfo: DataMetaInfo): Map<Instant, Array<FloatArray>> {
        NetcdfFile.open(filePath).use { ncFile ->

            val startDate = ncFile.findGlobalAttribute("RangeBeginningDate").stringValue

            val startTime = LocalDate.parse(startDate).atTime(0, 0).toInstant(ZoneOffset.UTC)

            val time = (0..23).map { startTime.plus(it.toLong(), ChronoUnit.HOURS) }

            val data = dataMetaInfo.parameterName.let { ncFile.findVariable(it) }.read()

            return (0..23).associate { time[it] to readSeries(it, data) }
        }
    }

    fun readFromNc4ToGlobalData(filePath: Path, dataMetaInfo: DataMetaInfo): GlobalData {
        val instantMap = readFromNc4(filePath.toString(), dataMetaInfo)
        return GlobalData(dataMetaInfo, instantMap)
    }

    private fun readSeries(timeIndex: Int, ucarArray: ucar.ma2.Array): Array<FloatArray> {

        val index = ucarArray.index
        val maxLatIndex = 361
        val maxLongIndex = 576
        val value = Array(maxLatIndex) { FloatArray(maxLongIndex) }


        positionService.forEachPosition { pos ->
            val netCdfIndex = index.set(timeIndex, pos.latIndex, pos.longIndex)
            value[pos.latIndex][pos.longIndex] = ucarArray.getDouble(netCdfIndex).toFloat()
        }

        return value
    }
}