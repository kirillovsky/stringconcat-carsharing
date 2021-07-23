package com.stringconcat.kirillov.carsharing.ride

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price

fun interface Taximeter {
    fun calculatePrice(ride: Ride): Either<CalculationRidePriceError, Price>
}

object CalculationRidePriceError : BusinessError