package com.stringconcat.kirillov.carsharing.ride

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price

class FixedRateTaximeter(private val ratePerDistance: Price) : Taximeter {
    override fun calculatePrice(ride: Ride): Either<CalculationRidePriceError, Price> =
        if (ride.isFinished().not()) {
            CalculationRidePriceError.left()
        } else {
            ride.coveredDistance
                .rightIfNotNull { }
                .flatMap {
                    ratePerDistance.times(it.kilometers)
                }
                .mapLeft { CalculationRidePriceError }
        }
}