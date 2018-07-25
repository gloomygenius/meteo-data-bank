package com.mdbank.service;

import com.mdbank.config.GlobalDataConfig;
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
    public PositionService(GlobalDataConfig globalDataConfig,
                           PositionRepository repository) {
        this.minLatitude = globalDataConfig.getMinLatitude();
        this.maxLatitude = globalDataConfig.getMaxLatitude();
        this.minLongitude = globalDataConfig.getMinLongitude();
        this.maxLongitude = globalDataConfig.getMaxLongitude();
        this.latStep = globalDataConfig.getLatStep();
        this.lonStep = globalDataConfig.getLonStep();
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
