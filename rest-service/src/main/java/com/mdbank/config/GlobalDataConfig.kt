package com.mdbank.config

import com.mdbank.exception.InitializationException
import com.mdbank.model.validation.LatitudeConstraint
import com.mdbank.model.validation.LongitudeConstraint
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Configuration
@ConfigurationProperties(prefix = "globaldata")
class GlobalDataConfig {
    @LatitudeConstraint
    var minLatitude: Double? = null

    @LatitudeConstraint
    var maxLatitude: Double? = null

    @LongitudeConstraint
    var minLongitude: Double? = null

    @LongitudeConstraint
    var maxLongitude: Double? = null

    var latStep: Double = 0.5

    var lonStep: Double = 0.625

    @PostConstruct
    fun check() {
        if (minLatitude!! > maxLatitude!!) {
            throw  InitializationException()
        }

        if (minLongitude!! > maxLongitude!!) {
            throw  InitializationException()
        }
    }
}