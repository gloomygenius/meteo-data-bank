package com.mdbank.model.metadata

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.persistence.*

@Entity
data class DataSourceInfo(@Id
                          @Column(name = "data_source_info_id")
                          @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_source_info_seq_gen")
                          @SequenceGenerator(name = "data_source_info_seq_gen", sequenceName = "data_source_info_id_seq", allocationSize = 1)
                          private val id: Long? = null,
                          val protocol: String,
                          val server: String,
                          val dataSet: String,
                          val data: String) {

    fun generateLink(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val monthFormatter = DateTimeFormatter.ofPattern("MM")
        return "https://$server.gesdisc.eosdis.nasa.gov/dataSeries/MERRA2/$dataSet/${date.year}/${monthFormatter.format(date)}/$data.${formatter.format(date)}.nc4"
    }
}