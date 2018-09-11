package com.mdbank.model

import com.mdbank.model.validation.LatitudeConstraint
import com.mdbank.model.validation.LongitudeConstraint
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
@Table(indexes = [Index(name = "latitude_longitude_index", columnList = "latitude, longitude", unique = true)])
data class Position(
        @Id
        @Column(name = "position_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "position_seq_gen")
        @SequenceGenerator(name = "position_seq_gen", sequenceName = "position_id_seq", allocationSize = 1)
        val id: Long? = null,

        @LatitudeConstraint
        val latitude: Double,

        @LongitudeConstraint
        val longitude: Double,

        val altitude: Double = 0.0 //in meters
) {

    /**
     * Индекс широты в диапазоне [0..360]
     */
    val latIndex: Int
        get() {
            val doubleIndex = (latitude + 90) * 2
            val roundedValue = Math.round(doubleIndex)
            return roundedValue.toInt()
        }

    /**
     * Индекс долготы в диапазоне [0..576]
     */
    val longIndex: Int
        get() {
            val doubleIndex = (longitude + 180) / 0.625
            val roundedValue = Math.round(doubleIndex)
            return roundedValue.toInt()
        }
}