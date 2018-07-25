package com.mdbank.util;

import com.mdbank.exception.NetCdfReadException;
import com.mdbank.model.GlobalData;
import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class NetCdfProcessor {
    private final PositionService positionService;

    @Autowired
    public NetCdfProcessor(@Value("${global_data.min_latitude}") double minLatitude,
                           @Value("${global_data.max_latitude}") double maxLatitude,
                           @Value("${global_data.min_longitude}") double minLongitude,
                           @Value("${global_data.max_longitude}") double maxLongitude, PositionService positionService) {
        this.positionService = positionService;
    }

    public static void save(String path, NetCdfParam variable, Map<Instant, float[][]> map) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));
        String maxDate = map.keySet()
                .stream()
                .max(Instant::compareTo)
                .map(formatter::format)
                .orElse("");
        String minDate = map.keySet()
                .stream()
                .min(Instant::compareTo)
                .map(formatter::format)
                .orElse("");
        String name = path + "\\result\\" + variable + "from" + minDate + "to" + maxDate + ".gz";
        File file = new File(name);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdir()) throw new RuntimeException("Path can't be create");
        }
        name = name.replaceAll("\\\\\\\\", "\\\\"); //меняем два слэша на один, если вдруг так получилось
        FileManager.saveToGZ(name, map);
    }

    public Map<Instant, float[][]> readFromNc4(String filePath, NetCdfParam parameter)
            throws NetCdfReadException {

        Map<Instant, float[][]> dataMap;

        try (NetcdfFile ncFile = NetcdfFile.open(filePath)) {

            String startDate = ncFile.findGlobalAttribute("RangeBeginningDate").getStringValue();

            Instant startTime = LocalDate
                    .parse(startDate)
                    .atTime(0, 0)
                    .toInstant(ZoneOffset.UTC);

            Instant[] time = IntStream.range(0, 24)
                    .mapToObj(i -> startTime.plus(i, ChronoUnit.HOURS))
                    .toArray(Instant[]::new);


            Array data = parameter.getValue()
                    .map(ncFile::findVariable)
                    .map(Exceptional.function(Variable::read))
                    .orElseThrow(() -> new NetCdfReadException("There are no value for parameter: " + parameter.name()));

            dataMap = IntStream
                    .range(0, 24)
                    .boxed()
                    .collect(Collectors.toMap(
                            t -> time[t],
                            t -> readSeries(t, data)));

        } catch (IOException e) {
            throw new NetCdfReadException(e);
        }

        return dataMap;
    }

    public GlobalData readFromNc4ToGlobalData(Path filePath, NetCdfParam parameter) {
        Map<Instant, float[][]> instantMap = readFromNc4(filePath.toString(), parameter);
        return new GlobalData(instantMap, parameter);
    }

    private float[][] readSeries(int timeIndex, Array data) {

        Index index = data.getIndex();
        int maxLatIndex = 361;
        int maxLongIndex = 576;
        float[][] value = new float[maxLatIndex][maxLongIndex];

        positionService.getPositionList()
                .forEach(p -> {
                    Index local = index.set(timeIndex, p.getLatIndex(), p.getLongIndex());
                    value[p.getLatIndex()][p.getLongIndex()] = (float) data.getDouble(local);
                });

        return value;
    }

    public Map<Instant, float[][]> readAllNC4(String path, NetCdfParam variable) throws IOException {
        return Files.list(Paths.get(path))
                .collect(Collectors.toList())
                .parallelStream()
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .peek(System.out::println)
                .map(s -> readFromNc4(s, variable))
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue));
    }

    @Deprecated
    private void transformAndSave(String path, NetCdfParam param) {
        Map<Instant, float[][]> map = null;
        try {
            map = readAllNC4(path, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        save("E:\\MERRA 2\\", param, map);
    }

}