package com.mdbank.model;

import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.util.TimeUtil;
import lombok.Data;

import java.io.*;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Data
public class GlobalData implements Serializable {
    private static final long serialVersionUID = 862841359675998516L;
    private NetCdfParam parameter;
    private Map<Instant, float[][]> data;

    public GlobalData(Map<Instant, float[][]> map) {
        data = map;
    }

    public GlobalData(Map<Instant, float[][]> map, NetCdfParam parameter) {
        data = map;
        this.parameter = parameter;
    }

    /**
     * Метод читает данные из файла формата gz, в котором сохранены обработанные данные
     *
     * @param filePath путь к файлу
     * @return массив данных по всему миру для указанного в файле периода
     */
    public static GlobalData readFromOldFormat(String filePath) {
        Map<Instant, float[][]> map;
        try (FileInputStream stream = new FileInputStream(filePath);
             GZIPInputStream gz = new GZIPInputStream(stream);
             ObjectInputStream out = new ObjectInputStream(gz)) {
            //noinspection unchecked
            map = (Map<Instant, float[][]>) out.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new GlobalData(map);
    }

    public static GlobalData rectangleDataToAzimuth(GlobalData eastWardData, GlobalData northWardData) {
        GlobalData linkedData = GlobalData.empty();
        for (Instant instant : eastWardData.data.keySet()) {
            float[][] eastWardArray = eastWardData.data.get(instant);
            float[][] northWardArray = northWardData.data.get(instant);
            float[][] newArray = new float[361][576];
            for (int i = 0; i < 361; i++) {
                for (int j = 0; j < 576; j++) {
                    double a = eastWardArray[i][j];
                    double b = northWardArray[i][j];
                    newArray[i][j] = (float) (Math.atan2(a, -b) * 180 / Math.PI + 180);
                }
            }
            linkedData.data.put(instant, newArray);
        }
        return linkedData;
    }

    private static GlobalData empty() {
        return new GlobalData(new HashMap<>());
    }

    /**
     * Для поддержки старого API. Надо использовать #toLocalData(Position,MeteoParameter)
     */
    @Deprecated
    public LocalData toLocalData(Position position) {
        return toLocalData(position, null);
    }

    /**
     * Метод вычленения данных для определённой точки на сетке
     *
     * @param position координата точки
     * @return локальный массив данных LocalData
     */
    public LocalData toLocalData(Position position, NetCdfParam parameter) {
        int latIndex = position.getLatIndex();
        int longIndex = position.getLongIndex();
        Map<Instant, Float> map = data.keySet().stream()
                .collect(Collectors.toMap(
                        instant -> instant,
                        instant -> data.get(instant)[latIndex][longIndex],
                        (v1, v2) -> {
                            throw new IllegalStateException("Same values of map");
                        },
                        TreeMap::new));
        Year year = map.keySet()
                .stream()
                .max(Comparator.naturalOrder())
                .map(s -> s.atZone(ZoneId.of("UTC")))
                .map(ZonedDateTime::getYear)
                .map(Year::of)
                .orElseThrow(() -> new RuntimeException("Не определён год глобальных данных"));
        Float[] floats = new Float[TimeUtil.hoursInYear(year)];

        map.forEach((k, v) -> {
            ZonedDateTime utc1 = k.atZone(ZoneId.of("UTC"));
            int utc = (utc1.getDayOfYear() - 1) * 24 + utc1.getHour();
            floats[utc] = v;
        });
        List<Float> list = Arrays.asList(floats);
        return new LocalData(parameter, position, year, list);
    }

    /**
     * Метод сохранения массива данных в файл в формате gz. Внимание! Сохраняется не данный класс, а только поле data!
     *
     * @param path - полный путь к файлу
     */
    @SuppressWarnings("Duplicates")
    public void saveToFile(String path) {
        //if (!new File(path).isDirectory()) throw new RuntimeException("Path " + path + " is not a directory");
        String name = path + "\\" + createFileName();
        File file = new File(name);
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdir()) throw new RuntimeException("Path " + name + " can't be create");
        }
        name = name.replaceAll("\\\\\\\\", "\\\\"); //меняем два слэша на один, если вдруг так получилось
        try (FileOutputStream stream = new FileOutputStream(name);
             GZIPOutputStream gz = new GZIPOutputStream(stream);
             ObjectOutputStream out = new ObjectOutputStream(gz)) {
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вспомогательный метод для создания унифицированного названия файла
     *
     * @return название файла
     */
    private String createFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));
        String maxDate = data.keySet()
                .stream()
                .max(Instant::compareTo)
                .map(formatter::format)
                .orElse("");
        String minDate = data.keySet()
                .stream()
                .min(Instant::compareTo)
                .map(formatter::format)
                .orElse("");
        return parameter + "from" + minDate + "to" + maxDate + ".gz";
    }

    /**
     * Метод предназначен для преобразования двух массивов данных о скоростях ветра (северная и восточная составляющие)
     * в единое значение скорости ветра. При этом теряется информация о направлении ветра.
     * V=sqrt(v*v+u*u);
     *
     * @param globalData массив данных о второй составляющей ветра
     */
    public void rectangleDataToRadius(GlobalData globalData) {
        for (Instant instant : data.keySet()) {
            float[][] array1 = data.get(instant);
            float[][] array2 = globalData
                    .getData()
                    .get(instant);
            for (int i = 0; i < 361; i++) {
                for (int j = 0; j < 576; j++) {
                    double a = array1[i][j];
                    double b = array2[i][j];
                    array1[i][j] = (float) Math.sqrt(a * a + b * b);
                }
            }
            data.put(instant, array1);
        }
    }
}