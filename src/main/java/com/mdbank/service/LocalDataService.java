package com.mdbank.service;

import com.mdbank.dao.postgres.LocalDataDao;
import com.mdbank.model.LocalData;
import com.mdbank.model.Position;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Service
public class LocalDataService {
    private PositionRepository positionRepository;
    private final LocalDataDao localDataDao;

    @Autowired
    public LocalDataService(PositionRepository positionRepository, LocalDataDao localDataDao) {
        this.positionRepository = positionRepository;
        this.localDataDao = localDataDao;
    }

    public Map<LocalDateTime, Float> getBy(Position position, NetCdfParam parameter, int year) {
        LocalData dataDaoBy = localDataDao.getBy(position, parameter, year)
                .orElseThrow(() -> new RuntimeException("Нет данных по выбранным параметрам"));
        List<Float> arr = dataDaoBy.getArr();
        LocalDateTime of = LocalDateTime.of(year, 1, 1, 0, 0);
        int endExclusive = Year.of(year).isLeap() ? 8784 : 8760;
        endExclusive = Math.min(endExclusive, arr.size());
        return IntStream.range(0, endExclusive)
                .filter(i -> arr.get(i) != null)
                .boxed()
                .sorted()
                .collect(
                        toMap(
                                (Function<Integer, LocalDateTime>) of::plusHours,
                                arr::get,
                                (v1, v2) -> v1,
                                TreeMap::new));
    }
}
