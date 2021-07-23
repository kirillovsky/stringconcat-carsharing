package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.CEILING

private const val SCALE = 1

data class Distance internal constructor(val kilometers: BigDecimal) {
    companion object {
        fun ofKilometers(value: BigDecimal): Either<NegativeDistanceValueError, Distance> =
            if (value < ZERO) {
                NegativeDistanceValueError.left()
            } else {
                Distance(kilometers = value.setScale(SCALE, CEILING)).right()
            }
    }

    operator fun plus(anotherDistance: Distance): Distance =
        Distance(kilometers + anotherDistance.kilometers)

    object NegativeDistanceValueError : BusinessError
}