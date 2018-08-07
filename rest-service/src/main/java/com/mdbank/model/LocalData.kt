package com.mdbank.model

import com.mdbank.model.metadata.DataMetaInfo
import org.hibernate.annotations.Type
import java.time.Instant
import java.time.Year
import javax.persistence.*

@Entity
data class LocalData(
        @Id
        @Column(name = "local_data_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "local_data_seq_gen")
        @SequenceGenerator(name = "local_data_seq_gen", sequenceName = "local_data_id_seq", allocationSize = 1)
        val id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "data_meta_info_id", nullable = false)
        val dataMetaInfo: DataMetaInfo,

        @ManyToOne
        @JoinColumn(name = "position_id", nullable = false)
        val position: Position,

        @Column(nullable = false)
        val year: Year,

        @Type(type = "float_list_type")
        val payload: List<Float?> = ArrayList()) {

        fun getValue(instant: Instant) {
                instant
        }
}