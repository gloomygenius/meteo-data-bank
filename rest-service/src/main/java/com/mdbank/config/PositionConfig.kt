package com.mdbank.config

import com.mdbank.service.PositionService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct

@Configuration
//Инциализация всех позиций только при полном старте приложения, исключаются тесты
@Profile("dev", "prod")
class PositionConfig constructor(val positionService: PositionService) {

    @PostConstruct
    fun init() {
        positionService.initAllPositions()
    }
}