package com.mdbank.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "nasa")
open class NasaConfig {
    lateinit var login: String
    lateinit var password: String
}