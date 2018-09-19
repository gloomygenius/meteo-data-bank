package com.mdbank.service;

import com.mdbank.model.LocalData;
import com.mdbank.model.Position;
import com.mdbank.model.metadata.DataMetaInfo;
import com.mdbank.repository.DataMetaInfoRepository;
import com.mdbank.repository.LocalDataRepository;
import com.mdbank.util.DateExtensionsKt;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.time.Year;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocalDataServiceTest {

    @Test
    public void testInterpolateData() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        final PositionService positionService = Mockito.mock(PositionService.class);
        final Position position1 = new Position(1L, 8, 10, 0);
        final Position position2 = new Position(2L, 8, 10.625, 0);
        final Position position3 = new Position(3L, 8.5, 10.625, 0);
        final Position position4 = new Position(4L, 8.5, 10, 0);
        when(positionService.getPosition(8, 10)).thenReturn(position1);
        when(positionService.getPosition(8, 10.625)).thenReturn(position2);
        when(positionService.getPosition(8.5, 10.625)).thenReturn(position3);
        when(positionService.getPosition(8.5, 10)).thenReturn(position4);

        final String parameter = "SWGDN";
        final Year year = Year.of(2018);

        final LocalDataRepository repositoryMock = Mockito.mock(LocalDataRepository.class);
//        when(repositoryMock.findByPositionAndYearAndDataMetaInfo(position1, year, parameter)).thenReturn()

        final List<Float> payload1 = createNewListOfNulls(year);
        payload1.set(1, 10F);
        final List<Float> payload2 = createNewListOfNulls(year);
        payload2.set(1, 40F);
        final List<Float> payload3 = createNewListOfNulls(year);
        payload3.set(1, 60F);
        final List<Float> payload4 = createNewListOfNulls(year);
        payload4.set(1, 30F);

//        new DataMetaInfo(null, "SWGDN", "Solar", null, 60);
        Map<Position, LocalData> localDataMap = new HashMap<>();
        final DataMetaInfo dataMetaInfo = mock(DataMetaInfo.class);
        localDataMap.put(position1, new LocalData(null, dataMetaInfo, position1, year, payload1));
        localDataMap.put(position2, new LocalData(null, dataMetaInfo, position2, year, payload2));
        localDataMap.put(position3, new LocalData(null, dataMetaInfo, position3, year, payload3));
        localDataMap.put(position4, new LocalData(null, dataMetaInfo, position4, year, payload4));

        LocalDataService localDataService = new LocalDataService(mock(LocalDataRepository.class), positionService, mock(DataMetaInfoRepository.class)) {
            @Override
            public LocalData findLocalData(Position position, Year year, String parameterName) {
                return localDataMap.get(position);
            }
        };
        //точка между lat 8 и 8.5 , lat 10 и 10.625
        LocalData localData = localDataService.interpolateLocalData(8.2, 10.3, year, parameter);
        Float[] valuesAsArray = localData.getValuesAsArray();
        assertThat(valuesAsArray[1], is(32.4F));
    }

    private List<Float> createNewListOfNulls(Year year) {
        Float[] floatArray = new Float[DateExtensionsKt.totalHoursInYear(year)];
        final List<Float> payload1 = Arrays.asList(floatArray);
        return payload1;
    }
}
