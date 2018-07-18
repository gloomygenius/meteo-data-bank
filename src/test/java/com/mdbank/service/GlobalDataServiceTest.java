package com.mdbank.service;

import com.mdbank.Application;
import com.mdbank.model.GlobalData;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.util.Exceptional;
import com.mdbank.util.NetCdfProcessor;
import org.junit.Before;
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
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@ContextConfiguration(classes = Application.class)
public class GlobalDataServiceTest {
    @Autowired
    private GlobalDataService service;
    private GlobalData globalData;
    private GlobalData globalData2;
    @Autowired
    private NetCdfProcessor netCdfProcessor;

    @SuppressWarnings("Duplicates")
    @Before
    public void init() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("MERRA2_400.tavg1_2d_rad_Nx.20170626.nc4").getFile());
        String path = URLDecoder.decode(file.getAbsolutePath(), "UTF-8");
        Map<Instant, float[][]> instantMap = netCdfProcessor.readFromNc4(
                path,
                NetCdfParam.SOLAR_SW);
        globalData = new GlobalData(instantMap, NetCdfParam.SOLAR_SW);
        globalData2 = new GlobalData(instantMap, NetCdfParam.WIND_2M);
    }

    @Ignore
    @Test
    public void updateData() {
    }
}