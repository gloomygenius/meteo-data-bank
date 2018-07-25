package com.mdbank.service;

import com.mdbank.model.metadata.NasaServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class Nc4Manager {

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    private Path homePath;

    public Nc4Manager() {
        this.homePath = Paths.get(System.getProperty("user.home")).resolve("meteo_data_bank/global_data/");
    }

    @PostConstruct
    private void init() {
        try {
            Files.delete(homePath);
        } catch (IOException e) {
            log.error("Directory with nc4 files ( {} )can't be cleaned", homePath, e);
        }
    }

    public Path getByDate(NasaServer server, LocalDate date) {
        String fileName = date.format(dateTimeFormatter) + ".nc4";
        return homePath.resolve(server.name()).resolve(fileName);
    }
}
