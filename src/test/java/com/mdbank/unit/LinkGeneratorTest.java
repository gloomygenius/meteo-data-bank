package com.mdbank.unit;

import com.mdbank.model.metadata.NasaServer;
import com.mdbank.util.LinkGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LinkGeneratorTest {

    private static final String EXPECTED_LINK = "https://goldsmr4.gesdisc.eosdis.nasa.gov/data/MERRA2/M2T1NXRAD.5.12.4/2017/06/MERRA2_400.tavg1_2d_rad_Nx.20170626.nc4";

    @Test
    @Ignore
    // TODO: 04.02.18 выпилить
    public void generate() {
        String link = LinkGenerator.generate(LocalDate.of(2017, 6, 26), NasaServer.SOLAR);
        assertThat(link, is(EXPECTED_LINK));
    }

    @Test
    public void gen() {
        PasswordEncoder encoder = new BCryptPasswordEncoder(11);
        System.out.println("pass: "+encoder.encode("123456"));
    }

}