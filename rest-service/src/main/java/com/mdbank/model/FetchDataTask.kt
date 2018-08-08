package com.mdbank.model

import com.mdbank.model.metadata.DataSourceInfo
import java.time.LocalDate
import java.time.temporal.ChronoUnit.*
import javax.persistence.*

@Entity
data class FetchDataTask(
        @Id
        @Column(name = "fetch_data_task_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fetch_data_task_seq_gen")
        @SequenceGenerator(name = "fetch_data_task_seq_gen", sequenceName = "fetch_data_task_id_seq", allocationSize = 1)
        var id: Long? = null,

        @Column(nullable = false)
        val date: LocalDate,

        @ManyToOne
        @JoinColumn(name = "data_meta_info_id", nullable = false)
        val source: DataSourceInfo,

        @Enumerated(EnumType.STRING)
        var status: TaskStatus = TaskStatus.NEW) {

    constructor(date: LocalDate, src: DataSourceInfo) : this(date = date, source = src)

    fun generateLink(): String = source.generateLink(date)
}
