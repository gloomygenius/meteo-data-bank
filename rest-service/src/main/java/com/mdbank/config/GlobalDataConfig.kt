package com.mdbank.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Configuration
@ConfigurationProperties(prefix = "globaldata")
open class GlobalDataConfig {
    @Min(-90)
    @Max(90)
    var minLatitude: Double? = null

    @Min(-90)
    @Max(90)
    var maxLatitude: Double? = null

    @Min(-180)
    @Max(180)
    var minLongitude: Double? = null

    @Min(-180)
    @Max(180)
    var maxLongitude: Double? = null

    var latStep: Double = 0.5

    var lonStep: Double = 0.625
}