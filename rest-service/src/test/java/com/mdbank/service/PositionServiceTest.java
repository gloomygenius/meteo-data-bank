package com.mdbank.service;

import com.mdbank.config.GlobalDataConfig;
import com.mdbank.model.Position;
import com.mdbank.repository.PositionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class PositionServiceTest {
    PositionService positionService;

    @Before
    public void init() {
        PositionRepository positionRepository = Mockito.mock(PositionRepository.class);
        Mockito.when(positionRepository.saveAll(any())).then(invocation -> new ArrayList<>((Set<Position>) invocation.getArgument(0)));

        GlobalDataConfig globalDataConfig = new GlobalDataConfig();
        globalDataConfig.setMinLatitude(0.0);
        globalDataConfig.setMaxLatitude(30.0);
        globalDataConfig.setMinLongitude(0.0);
        globalDataConfig.setMaxLongitude(20.0);

        positionService = new PositionService(globalDataConfig, positionRepository);
        positionService.initAllPositions();
    }

    @Test
    public void testThatForEachPositionAppliesForAllPositions() {
        AtomicInteger count = new AtomicInteger();
        positionService.forEachPosition(position -> {
            count.incrementAndGet();
            return null;
        });
        final int expectedTotalPositions = 2013;
        assertEquals(expectedTotalPositions, count.get());
    }
}
