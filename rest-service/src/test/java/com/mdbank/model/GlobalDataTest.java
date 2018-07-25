package com.mdbank.model;

import com.mdbank.Application;
import com.mdbank.model.metadata.MeteoParameter;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.util.NetCdfProcessor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.net.URLDecoder;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
@TestPropertySource(locations = "classpath:/application-test.properties")
@SpringBootTest
public class GlobalDataTest {
    private GlobalData globalData;
    @Autowired
    private NetCdfProcessor netCdfProcessor;

    @Before
    public void init() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("MERRA2_400.tavg1_2d_rad_Nx.20170626.nc4").getFile());
        String path = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
        Map<Instant, float[][]> instantMap = netCdfProcessor.readFromNc4(
                path,
                NetCdfParam.SOLAR_SW);
        globalData = new GlobalData(instantMap);
    }

    @Test
    @Ignore
    public void readFromOldFormat() throws Exception {
    }

    @Test
    @Ignore
    public void rectangleDataToAzimuth() throws Exception {
    }

    @Test
    public void toLocalData() {
        Position position = new Position(10, 30);
        LocalData localData = globalData.toLocalData(position, null);
        int HOURS_IN_NOT_LEAP_YEAR = 8760;
        List<Float> arr = localData.getArr();
        assertThat(arr.size(), is(HOURS_IN_NOT_LEAP_YEAR));

        long numberOfNonZeroValue = arr.stream()
                .filter(Objects::nonNull)
                .filter(s -> s > 0)
                .count();
        assertThat(numberOfNonZeroValue, is(greaterThan(0L)));
    }

    @Test
    @Ignore
    public void saveToFile() throws Exception {
    }
}