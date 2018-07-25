package com.mdbank.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * Преечисление возможных параметров массива метео данных (скорость ветра, температура и т.д.)
 */
@Getter
@AllArgsConstructor
public enum MeteoParameter {
    WIND_2M("Скорость ветра на высоте 2 м над уровнем земли"),
    SOLAR_SW("\"surface incoming shortwave flux\" - коротковолновое излучение"),
    SOLAR_DIRECT("Прямая солнечная радиация");

    private final String description;
}
