package com.mdbank.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Configuration
@ConfigurationProperties(prefix = "globaldata")
@Getter
@Setter
public class GlobalDataConfig {
    @Min(-90)
    @Max(90)
    private Double minLatitude;
    @Min(-90)
    @Max(90)
    private Double maxLatitude;
    @Min(-180)
    @Max(180)
    private Double minLongitude;
    @Min(-180)
    @Max(180)
    private Double maxLongitude;
    private Double latStep = 0.5;
    private Double lonStep = 0.625;
}
