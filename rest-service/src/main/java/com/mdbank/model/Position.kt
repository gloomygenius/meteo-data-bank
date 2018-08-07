package com.mdbank.model

import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "latitude_longitude_index", columnList = "latitude, longitude", unique = true)])
data class Position(
        @Id
        @Column(name = "position_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position_seq_gen")
        @SequenceGenerator(name = "position_seq_gen", sequenceName = "position_id_seq", allocationSize = 1)
        val id: Long? = null,

        val latitude: Double,

        val longitude: Double,

        val altitude: Double = 0.0 //in meters
)