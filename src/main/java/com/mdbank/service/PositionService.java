package com.mdbank.service;

import com.mdbank.model.Position;
import com.mdbank.repository.PositionRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PositionService {
    final private PositionRepository repository;
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
    private double latStep;
    private double lonStep;
    @Getter
    private List<Position> positionList;

    @Autowired
    public PositionService(@Value("${global_data.min_latitude}") double minLatitude,
                           @Value("${global_data.max_latitude}") double maxLatitude,
                           @Value("${global_data.min_longitude}") double minLongitude,
                           @Value("${global_data.max_longitude}") double maxLongitude,
                           @Value("${global_data.lat_step}") double latStep,
                           @Value("${global_data.lon_step}") double lonStep,
                           PositionRepository repository) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.latStep = latStep;
        this.lonStep = lonStep;
        this.repository = repository;
    }

    @PostConstruct
    private void init() {
        log.info("Initialization position service started");
        StopWatch timer = new StopWatch();
        timer.start();

        positionList = repository.findAll();
        //если в базе нет данных о координатах, то берём их их конфигурации и потом сохраняем в базе
        if (positionList.isEmpty()) {
            positionList = new ArrayList<>();
            for (double latitude = minLatitude; latitude <= maxLatitude; latitude += latStep) {
                for (double longitude = minLongitude; longitude <= maxLongitude; longitude += lonStep) {
                    Position position = new Position(latitude, longitude);
                    positionList.add(position);
                }
            }
            repository.saveAll(positionList);
        }

        timer.stop();
        log.info("Initialization position service ended. Execution time: {} seconds", timer.getTotalTimeSeconds());
    }

    public Position getPositionWithId(Position position) {
        if (position.getId() == null) {
            return repository.findOptionalByLatitudeAndLongitude(position.getLatitude(), position.getLongitude())
                    .orElseGet(() -> repository.save(position));
        } else {
            return position;
        }
    }
}
