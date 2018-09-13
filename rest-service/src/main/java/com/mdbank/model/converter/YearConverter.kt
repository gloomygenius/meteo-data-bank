package com.mdbank.model.converter

import java.time.Year
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * Конвертер для автоматического отображения полей с типом java.time.Year в БД
 */
@Converter(autoApply = true)
class YearConverter : AttributeConverter<Year?, Int?> {
    override fun convertToDatabaseColumn(year: Year?): Int? = year?.value

    override fun convertToEntityAttribute(dbData: Int?): Year? = if (dbData != null) Year.of(dbData) else null
}