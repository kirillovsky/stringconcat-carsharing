package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.ride.domain.RideId
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.InvalidPayRideParametersError.InvalidPriceValue
import java.math.BigDecimal

data class PayRideRequest internal constructor(val rideId: RideId, val price: Price) {
    companion object {
        fun from(rideIdValue: Long, priceValue: BigDecimal): Either<InvalidPayRideParametersError, PayRideRequest> =
            Price.from(priceValue).map {
                PayRideRequest(rideId = RideId(rideIdValue), price = it)
            }.mapLeft { InvalidPriceValue }
    }
}

sealed class InvalidPayRideParametersError(val message: String) {
    object InvalidPriceValue : InvalidPayRideParametersError("invalid price value")
}