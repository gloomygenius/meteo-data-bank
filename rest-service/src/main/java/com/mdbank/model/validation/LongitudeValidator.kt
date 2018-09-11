package com.mdbank.model.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class LongitudeValidator : ConstraintValidator<LongitudeConstraint, Double> {
    override fun isValid(longitude: Double?, context: ConstraintValidatorContext?): Boolean {
        var result = true

        if (longitude == null) {
            result = false
        } else {
            val valueIsOutOfRange = longitude < -180.001 || longitude > 180.001

            val delta = computeDelta(longitude)
            val maxDelta = 0.001

            if (valueIsOutOfRange or (delta > maxDelta)) {
                result = false
            }
        }

        return result
    }

    /**
     * Дробная часть индекса. Должна быть минимально возможной, чтобы не сильно отличаться от округшлённого значения
     */
    private fun computeDelta(longitude: Double): Double {
        val doubleIndex = (longitude + 180) / 0.625
        val roundedValue = Math.round(doubleIndex)
        return Math.abs(roundedValue - doubleIndex)
    }
}