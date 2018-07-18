package com.mdbank.util;

import com.mdbank.model.metadata.NasaServer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinkGenerator {
    @Deprecated
    public static String generate(LocalDate date, NasaServer nasaServer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        return "https://" + nasaServer.getServer() + ".gesdisc.eosdis.nasa.gov/data/MERRA2/" + nasaServer.getDataSet() + "/"
                + date.getYear() + "/" + monthFormatter.format(date) + "/" + nasaServer.getData() + "."
                + formatter.format(date) +
                ".nc4";
    }

    private static List<String> getListOfLinks(LocalDate startDate, int month, NasaServer type) {
        return Stream.iterate(startDate, s -> s.plusDays(1))
                .limit(357)
                .filter(ld -> ld.getYear() == startDate.getYear())
                .filter(ld -> ld.getMonthValue() <= (startDate.getMonthValue() + month - 1))
                .map(s -> generate(s, type))
                .collect(Collectors.toList());
    }
}