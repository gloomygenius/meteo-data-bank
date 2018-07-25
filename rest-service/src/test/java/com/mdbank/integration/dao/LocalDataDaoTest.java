package com.mdbank.integration.dao;

import com.mdbank.Application;
import com.mdbank.dao.postgres.LocalDataDao;
import com.mdbank.model.LocalData;
import com.mdbank.model.Position;
import com.mdbank.model.metadata.MeteoParameter;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.repository.PositionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@ContextConfiguration(classes = Application.class)
public class LocalDataDaoTest {
    @Autowired
    LocalDataDao localDataDao;
    @Autowired
    PositionRepository repository;

    @Test
    public void testSave() {
        Position position = new Position(10.0, 30.0);
        Position save = repository.save(position);

        LocalData localData = new LocalData(NetCdfParam.SOLAR_SW, position, Year.now(), generateArray(10, true));
        localData = localDataDao.save(localData);
        LocalData byId = localDataDao.getById(localData.getId());

    }

    @Test
    public void testGetById() {
        LocalData byId = localDataDao.getById(1L);
        assertThat(byId.getArr(), hasSize(3));
    }

    @Test
    public void testUpdate() {
        //noinspection ConstantConditions
        Position one = repository.findById(1L).get();
        LocalData localData = new LocalData(NetCdfParam.SOLAR_SW, one, Year.of(1990), generateArray(10, true));
        localData = localDataDao.save(localData);
        LocalData byId = localDataDao.getById(localData.getId());
        List<Float> floats = generateArray(20, true);

        byId.setArr(floats);
        localDataDao.update(byId);
        LocalData data = localDataDao.getById(byId.getId());
        assertThat(data.getArr(), is(floats));
    }

    private List<Float> generateArray(int size, boolean withNull) {
        return Stream.generate(Math::random)
                .limit(size)
                .map(Double::floatValue)
                .map(value -> withNull && value < 0.5 ? null : value)
                .collect(Collectors.toList());
    }
}