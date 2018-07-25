package com.mdbank.model;

import com.mdbank.model.metadata.NetCdfParam;
import com.mdbank.util.DateUtilsKt;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
public class LocalData {
    @Id
    @Column(name = "local_data_id")
    @GeneratedValue(strategy = SEQUENCE, generator = "local_data_seq_gen")
    @SequenceGenerator(name = "local_data_seq_gen", sequenceName = "local_data_id_seq", allocationSize = 1)
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

        int daysInYear = DateUtilsKt.hoursInYear(year) * 24;

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