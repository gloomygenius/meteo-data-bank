package com.mdbank.service;

import com.mdbank.dao.postgres.LocalDataDao;
import com.mdbank.model.GlobalData;
import com.mdbank.model.LocalData;
import com.mdbank.model.UpdateProcess;
import com.mdbank.model.dto.Status;
import com.mdbank.model.metadata.MeteoParameter;
import com.mdbank.model.metadata.NasaServer;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.repository.UpdateProcessRepository;
import com.mdbank.util.ExRunnable;
import com.mdbank.util.NetCdfProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class GlobalDataService {
    private final DownloadService downloadService;
    private final Nc4Manager nc4Manager;
    private final NetCdfProcessor netCdfProcessor;
    private final LocalDataDao localDataDao;
    private final PositionService positionService;
    private final UserService userService;
    private final UpdateProcessRepository processRepository;
    private ExecutorService service = Executors.newFixedThreadPool(1);

    @PreDestroy
    private void destroy() {
        if (!service.isShutdown()) {
            service.shutdown();
        }
    }

    @Autowired
    public GlobalDataService(DownloadService downloadService,
                             Nc4Manager nc4Manager,
                             NetCdfProcessor netCdfProcessor,
                             LocalDataDao localDataDao,
                             PositionService positionService,
                             UserService userService,
                             UpdateProcessRepository processRepository) {
        this.processRepository = processRepository;
        this.downloadService = downloadService;
        this.nc4Manager = nc4Manager;
        this.netCdfProcessor = netCdfProcessor;
        this.localDataDao = localDataDao;
        this.positionService = positionService;
        this.userService = userService;
    }

    /**
     * Метод, который добавляет задание на отложенную загрузку данных с указанного сервера NASA
     * @param nasaServer - сервер NASA
     * @param startDate
     * @param endDate
     */
    public void addGlobalDataUpdateBatchTask(NasaServer nasaServer, LocalDate startDate, LocalDate endDate) {
        
        if (startDate.isAfter(endDate) || startDate.equals(endDate)) {
            throw new IllegalArgumentException("Start date can't be after or equals end date");
        }
        
        int days = Period.between(startDate, endDate).getDays();

        Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(days)
                .forEach(date -> {
                    UpdateProcess process = new UpdateProcess(userService.getAuthorizedUser());
                    process.setDescription("Update data for server " + nasaServer + " " + date);
                    process.setStatus(Status.PENDING);
                    processRepository.save(process);

                    ExRunnable task = () -> {
                        process.setStatus(Status.IN_PROGRESS);
                        processRepository.save(process);
                        Path fileOnDisk = nc4Manager.getByDate(nasaServer, date);
                        String webLink = nasaServer.getLink(date);
                        //проверяем, есть ли на диске уже скачанный файл данных
                        Path nc4file = Files.exists(fileOnDisk) ? fileOnDisk : downloadService.download(fileOnDisk, webLink);
                        //считываем параметры и обновляем дынные
                        Arrays.stream(nasaServer.getNetCdfParams())
                                .map(param -> netCdfProcessor.readFromNc4ToGlobalData(nc4file, param))
                                .forEach(globalData -> update(globalData, date.getYear()));
                        //удаляем считанный файл с диска
                        Files.delete(fileOnDisk);
                    };

                    CompletableFuture.runAsync(task, service)
                            .whenComplete((t, ex) -> {
                                if (ex != null) {
                                    log.error("Ошибка во время обновления данных", ex);
                                    // TODO: 25.02.2018 залогировать ex
                                    process.setStatus(Status.REJECTED);
                                } else {
                                    process.setStatus(Status.SUCCESS);
                                }
                                processRepository.save(process);
                            });
                });

    }

    private void update(GlobalData globalData, int year) {
        NetCdfParam parameter = globalData.getParameter();

        positionService.getPositionList()
                .forEach(position -> {
                    LocalData localData = globalData.toLocalData(position, parameter);
                    try {
                        LocalData localDataNew = localDataDao.getBy(position, parameter, year)
                                .map(s -> s.merge(localData))
                                .map(localDataDao::update)
                                .orElseGet(() -> localDataDao.save(localData));
                    } catch (Exception e) {
                        log.error("Данные параметра {} не обновлены для координат {}", parameter, position, e);
                    }
                });
    }

    private Map<MeteoParameter, List<LocalData>> convertToLocalDate(Map<NetCdfParam, GlobalData> globalDataMap) {
        NasaServer nasaServer = globalDataMap.keySet()
                .stream()
                .findFirst()
                .map(NetCdfParam::getNasaServer)
                .orElseThrow(() -> new IllegalStateException("Empty NetCdfParam"));
        // TODO: 16.01.18 доделать
        return null;
    }

    public List<String> getShortStatusOfUpdate() {
        return processRepository.findAll()
                .stream()
                .map(s -> s.getDescription() + " " + s.getStatus())
                .collect(Collectors.toList());
    }
}