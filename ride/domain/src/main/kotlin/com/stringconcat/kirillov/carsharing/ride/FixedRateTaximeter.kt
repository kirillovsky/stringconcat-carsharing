package com.stringconcat.kirillov.carsharing.ride

import arrow.core.Either
import arrow.core.left
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price

class FixedRateTaximeter(private val ratePerDistance: Price) : Taximeter {
    override fun calculatePrice(ride: Ride): Either<CalculationRidePriceError, Price> =
        if (ride.isFinished().not()) {
            CalculationRidePriceError.left()
        } else {
            ratePerDistance
                .times(ride.coveredDistance.kilometers)
                .mapLeft { CalculationRidePriceError }
        }
}