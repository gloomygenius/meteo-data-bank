package com.mdbank.repository

import com.mdbank.model.LocalData
import com.mdbank.model.Position
import com.mdbank.model.metadata.DataMetaInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Year

interface LocalDataRepository {
    /**
     * Метод для поиска LocalData по Position, Year и DataMetaInfo
     */
    fun findByPositionAndYearAndDataMetaInfo(position: Position, year: Year, metaInfo: DataMetaInfo): LocalData?
    /**
     * Метод сохраняет сущность в БД или обновляет уже существующую, если задан ID
     */
    fun save(localData: LocalData): LocalData
}