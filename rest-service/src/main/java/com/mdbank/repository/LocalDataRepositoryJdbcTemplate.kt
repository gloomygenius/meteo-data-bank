package com.mdbank.repository

import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.model.metadata.DataMetaInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.Year
import org.springframework.jdbc.support.GeneratedKeyHolder


@Repository
class LocalDataRepositoryJdbcTemplate @Autowired constructor(private val jdbcTemplate: JdbcTemplate) : LocalDataRepository {

    override fun findByPositionAndYearAndDataMetaInfo(position: Position, year: Year, metaInfo: DataMetaInfo): LocalData? {
        val resultList = jdbcTemplate.query("select ld.* from local_data ld where ld.position_id=? and ld.year=? and ld.data_meta_info_id=?",
                arrayOf(position.id, year.value, metaInfo.id)
        ) { rs, _ ->
            @Suppress("UNCHECKED_CAST")
            LocalData(
                    rs.getLong("local_data_id"),
                    metaInfo,
                    position,
                    year,
                    (rs.getArray("payload").array as Array<Float?>).toList()
            )
        }
        return resultList.firstOrNull()
    }

    override fun save(localData: LocalData): LocalData {
        if (localData.id != null) {
            return update(localData)
        } else {
            val holder = GeneratedKeyHolder()
            localData.id = generateId()
            jdbcTemplate.update({ con ->
                return@update con.prepareStatement("INSERT INTO local_data(data_meta_info_id, payload, year, position_id, local_data_id) VALUES (?, ?, ?, ?, ?)")
                        .also {
                            it.setLong(1, localData.dataMetaInfo.id
                                    ?: throw RuntimeException("DataMetaInfo ID is NULL"))
                        }
                        .also { it.setArray(2, con.createArrayOf("float", localData.getValuesAsArray())) }
                        .also { it.setInt(3, localData.year.value) }
                        .also { it.setLong(4, localData.position.id ?: throw RuntimeException("Position ID is NULL")) }
                        .also { it.setLong(5, localData.id!!) }
            }, holder)
//            localData.id = holder.key?.toLong() ?: throw RuntimeException("ID for local data wasn't generated")
            return localData
        }
    }

    private fun generateId(): Long {
        return jdbcTemplate.queryForObject("select nextval('local_data_id_seq')") { rs, _ -> rs.getLong(1) }
    }

    private fun update(localData: LocalData): LocalData {
        jdbcTemplate.update { con ->
            con.prepareStatement("INSERT INTO local_data (data_meta_info_id, payload, year, position_id) VALUES (?, ?, ?, ?)")
                    .also {
                        it.setLong(1, localData.dataMetaInfo.id ?: throw RuntimeException("DataMetaInfo ID is NULL"))
                    }
                    .also { it.setArray(2, con.createArrayOf("float", localData.getValuesAsArray())) }
                    .also { it.setInt(3, localData.year.value) }
                    .also { it.setLong(4, localData.position.id ?: throw RuntimeException("Position ID is NULL")) }
        }
        return localData
    }
}