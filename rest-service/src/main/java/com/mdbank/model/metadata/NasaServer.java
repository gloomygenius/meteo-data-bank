package com.mdbank.model.metadata;

import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public enum NasaServer {
    SOLAR("goldsmr4", "M2T1NXRAD.5.12.4", "MERRA2_400.tavg1_2d_rad_Nx", NetCdfParam.SOLAR_SW),
    WIND("goldsmr4", "M2T1NXSLV.5.12.4", "MERRA2_400.tavg1_2d_slv_Nx", NetCdfParam.WIND_2M),
    TEMPERATURE("goldsmr4", "M2T1NXSLV.5.12.4", "MERRA2_400.tavg1_2d_slv_Nx");
    private final String server;
    private final String dataSet;
    private final String data;
    private final NetCdfParam[] netCdfParams;

    NasaServer(String server, String dataSet, String data, NetCdfParam... netCdfParams) {
        this.server = server;
        this.dataSet = dataSet;
        this.data = data;
        this.netCdfParams = netCdfParams;
    }

    public String getLink(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        return "https://" + server + ".gesdisc.eosdis.nasa.gov/data/MERRA2/" + dataSet + "/"
                + date.getYear() + "/" + monthFormatter.format(date) + "/" + data + "."
                + formatter.format(date) +
                ".nc4";
    }
}
