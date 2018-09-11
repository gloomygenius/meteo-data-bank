package com.mdbank.model.metadata

import javax.persistence.*

@Entity
class DataMetaInfo(
        @Id
        @Column(name = "data_meta_info_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_meta_info_seq_gen")
        @SequenceGenerator(name = "data_meta_info_seq_gen", sequenceName = "data_meta_info_id_seq", allocationSize = 1)
        val id: Long? = null,


        val parameterName: String,

        val description: String? = null,

        @ManyToOne
        val sourceInfo: DataSourceInfo? = null,
        /**
         * Шаг данных в минутах
         */
        val resolution: Int)