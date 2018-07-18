package com.mdbank.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * Параметр для доступа к конкретным данным из файлов nc4.
 * Содержит привязку к серверу NASA.
 */
@AllArgsConstructor
@Getter
public enum NetCdfParam {
    /**
     * Скорость ветра на высоте 2 м над уровнем земли.
     */
    WIND_2M("WIND_2M", NasaServer.WIND),
    /**
     * "surface incoming shortwave flux" - коротковолновое излучение.
     */
    SOLAR_SW("SWGDN", NasaServer.SOLAR),
    ALBEDO("ALBEDO", NasaServer.SOLAR);



    private final String value;
    private final NasaServer nasaServer;

    public Optional<String> getValue(){
        return Optional.of(value);
    }
}
