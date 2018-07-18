package com.mdbank.unit;

import com.mdbank.Application;
import com.mdbank.model.metadata.MeteoParameter;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.util.NetCdfProcessor;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.net.URLDecoder;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@ContextConfiguration(classes = Application.class)
public class NetCdfProcessorTest {

    @Autowired
    NetCdfProcessor netCdfProcessor;

    @Test
    public void readFromNc4() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("MERRA2_400.tavg1_2d_rad_Nx.20170626.nc4")
                .getFile());
        String path = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
        Map<Instant, float[][]> instantMap = netCdfProcessor.readFromNc4(path, NetCdfParam.SOLAR_SW);
        assertThat(instantMap, is(notNullValue()));
        long l = countNotZeroValue(instantMap);
        assertTrue(l > 0);
    }

    private long countNotZeroValue(Map<Instant, float[][]> instantMap) {

        return instantMap.values().stream()
                .flatMap(Arrays::stream)
                .flatMapToDouble(s -> {
                    double[] doubles = new double[s.length];
                    for (int i = 0; i < s.length; i++) {
                        doubles[i] = s[i];
                    }
                    return Arrays.stream(doubles);
                }).filter(s -> s > 0)
                .count();
    }

    @Test
    @Ignore
    public void readAllNC4() throws Exception {
    }

    @Test
    @Ignore
    public void save() throws Exception {
    }


}