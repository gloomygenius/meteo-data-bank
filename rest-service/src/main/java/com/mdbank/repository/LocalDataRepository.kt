package com.mdbank.repository

import com.mdbank.model.LocalData
import com.mdbank.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Year

@Repository
interface LocalDataRepository : JpaRepository<LocalData, Long> {
    fun findByPositionAndYear(position: Position, year: Year): LocalData?
}