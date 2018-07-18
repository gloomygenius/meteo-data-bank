package com.mdbank.model;

import com.mdbank.model.metadata.NetCdfParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
public class LocalData implements Serializable {
    private static final long serialVersionUID = 9116546558503639478L;
    @Id
    @Column(name = "local_data_id")
    @GeneratedValue(strategy = SEQUENCE, generator = "local_data_seq_gen")
    @SequenceGenerator(name = "local_data_seq_gen", sequenceName = "local_data_local_data_id_seq", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    private NetCdfParam parameter;
    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;
    private Year year;
    private List<Float> arr;

    public LocalData(Long id, NetCdfParam parameter, Position position, Year year, List<Float> arr) {
        this.id = id;
        this.parameter = parameter;
        this.position = position;
        this.year = year;
        this.arr = arr;
    }

    public LocalData(NetCdfParam parameter, Position position, Year year, List<Float> arr) {
        this(null, parameter, position, year, arr);
    }


    /**
     * Метод читает из всех файлов в указанной папке глобальные данные, потом преобразует к локальным данным
     * и объединяет в один экземпляр
     *
     * @param path     путь к папке с файлами данных (кроме файлов данных, не должно быть никаких других файлов)
     * @param position координата для преобразования в локальные данные
     */
    @SneakyThrows
    public static LocalData readFromFiles(String path, Position position) {
        return Files.list(Paths.get(path))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .peek(System.out::println)
                .map(GlobalData::readFromOldFormat)
                .map(globalData -> globalData.toLocalData(position))
                .reduce(null) // FIXME: 04.11.2017 надо подумать
                .orElseThrow(() -> new RuntimeException("LocalData doesn't exist"));
    }

    /**
     * Метод возвращает новый экземпляр LocalData
     * @throws IllegalArgumentException если не совпдают parameter или year
     * @return новый объект LocalData со слитыми данными
     */
    public LocalData merge(LocalData localData) {
        if (parameter != localData.parameter) {
            throw new IllegalArgumentException("Ошибка при слиянии данных: разные параметры " + parameter.name() + " " + localData.parameter);
        }

        if (!year.equals(localData.year)) {
            throw new IllegalArgumentException("Ошибка при слиянии данных: разные года " + parameter.name() + " " + localData.parameter);
        }

        int daysInYear = year.length() * 24;

        List<Float> floats = new ArrayList<>(daysInYear);

        for (int i = 0; i < daysInYear; i++) {
            Float f1 = arr.get(i);
            Float f2 = localData.arr.get(i);
            Float aFloat = Optional.ofNullable(f1).orElse(f2);
            floats.add(aFloat);
        }

        return new LocalData(id, parameter, position, year, floats);
    }
}