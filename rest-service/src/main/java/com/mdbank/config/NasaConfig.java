package com.mdbank.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "nasa")
@Getter
@Setter
public class NasaConfig {
    @NotNull
    private String login;
    @NotNull
    private String password;
}
