package com.mdbank.controller.dto.localdata

import org.springframework.format.annotation.DateTimeFormat
import java.time.Year
import javax.validation.constraints.NotNull

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
open class LocalDataRequestDto {
    @NotNull
    var latitude: Double? = null
    @NotNull
    var longitude: Double? = null
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull
    lateinit var year: Year
}