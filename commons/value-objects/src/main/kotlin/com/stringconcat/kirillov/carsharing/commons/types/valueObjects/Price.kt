package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.CEILING

private const val SCALE = 2

data class Price internal constructor(val value: BigDecimal) : ValueObject {
    companion object {
        fun from(value: BigDecimal): Either<NegativePriceValueError, Price> =
            if (value < ZERO) {
                NegativePriceValueError.left()
            } else {
                Price(value.setScale(SCALE, CEILING)).right()
            }
    }
}

object NegativePriceValueError : BusinessError