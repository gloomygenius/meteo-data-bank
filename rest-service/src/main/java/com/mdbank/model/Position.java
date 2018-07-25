package com.mdbank.model;

import com.mdbank.util.PositionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Position {
    @Id
    @Column(name = "position_id")
    @GeneratedValue(strategy = SEQUENCE, generator = "position_seq_gen")
    @SequenceGenerator(name = "position_seq_gen", sequenceName = "position_position_id_seq", allocationSize = 1)
    private Long id;
    private Double latitude;
    private Double longitude;
    private Double altitude; //in meters

    public Position(double latitude, double longitude) {
        this(null, latitude, longitude, null);
    }


    public int getLatIndex() {
        return PositionUtil.transformLatToIndex(latitude);
    }

    public int getLongIndex() {
        return PositionUtil.transformLonToIndex(longitude);
    }
}