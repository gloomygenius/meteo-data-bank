package com.mdbank.model.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class LatitudeValidator : ConstraintValidator<LatitudeConstraint, Double> {
    override fun isValid(latitude: Double?, context: ConstraintValidatorContext?): Boolean {
        var result = true

        if (latitude == null) {
            result = false
        } else {
            val valueIsOutOfRange = latitude < -90.001 || latitude > 90.001

            val delta = computeDelta(latitude)
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
    private fun computeDelta(latitude: Double): Double {
        val doubleIndex = (latitude + 90) * 2
        val roundedValue = Math.round(doubleIndex)
        return Math.abs(roundedValue - doubleIndex)
    }
}