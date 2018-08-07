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
        val id: Long? = null,

        @Column(nullable = false)
        val startDate: LocalDate,

        @Column(nullable = false)
        val endDate: LocalDate,

        var processedFiles: Int = 0,

        @ManyToOne
        @JoinColumn(name = "data_meta_info_id", nullable = false)
        val source: DataSourceInfo) {

    constructor(start: LocalDate, end: LocalDate, src: DataSourceInfo) : this(startDate = start, endDate = end, source = src)

    fun generateLinks(): List<String> {
        val numberOfDays = DAYS.between(startDate, endDate)
        val links = ArrayList<String>()
        for (day in processedFiles..numberOfDays) {
            startDate.plusDays(day).let { date -> source.generateLink(date) }.let { links.add(it) }
        }
        return links
    }
}
