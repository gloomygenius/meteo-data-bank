package com.mdbank.service;

import com.mdbank.model.FetchDataTask;
import com.mdbank.model.metadata.DataSourceInfo;
import com.mdbank.repository.DataSourceInfoRepository;
import com.mdbank.service.globaldata.GlobalDataService;
import com.mdbank.service.globaldata.TaskQueue;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

public class GlobalDataServiceTest {
    private GlobalDataService globalDataService;
    private TaskQueue taskQueueMock;

    @Before
    public void init() {
        final DataSourceInfoRepository dataSourceInfoRepositoryMock = mock(DataSourceInfoRepository.class);
        DataSourceInfo dataSourceInfo = new DataSourceInfo(1L, "goldsmr4", "M2T1NXRAD.5.12.4", "MERRA2_400.tavg1_2d_rad_Nx");
        when(dataSourceInfoRepositoryMock.findById(1L)).thenReturn(Optional.of(dataSourceInfo));
        taskQueueMock = mock(TaskQueue.class);
        when(taskQueueMock.putNew(any(FetchDataTask.class))).thenAnswer(h -> h.getArgument(0));

    }

    @Test
    public void testThatLastDateDoesNotIncludes() {
        LocalDate startDate = LocalDate.ofYearDay(2018, 10);
        LocalDate endDate = LocalDate.ofYearDay(2018, 11);

        globalDataService.fetchFromSource(startDate, endDate, 1);
        verify(taskQueueMock, times(2)).putNew(any(FetchDataTask.class));
    }
}
