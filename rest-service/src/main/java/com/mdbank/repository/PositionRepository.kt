package com.mdbank.repository

import com.mdbank.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PositionRepository : JpaRepository<Position, Long> {
    fun findByLatitudeAndLongitude(latitude: Double, longitude: Double): Position?
}