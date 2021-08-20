package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.ride.domain.RideId
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.InvalidFinishRideParameters.InvalidCoveredKilometers
import java.math.BigDecimal
import java.time.OffsetDateTime

data class FinishRideRequest internal constructor(
    val rideId: RideId,
    val finishedDateTime: OffsetDateTime,
    val coveredDistance: Distance
) {
    companion object {
        fun from(
            rideId: Long,
            finishedDateTime: OffsetDateTime,
            coveredKilometers: BigDecimal
        ): Either<InvalidFinishRideParameters, FinishRideRequest> =
            Distance.ofKilometers(coveredKilometers)
                .mapLeft { InvalidCoveredKilometers }
                .map {
                    FinishRideRequest(
                        rideId = RideId(value = rideId),
                        finishedDateTime = finishedDateTime,
                        coveredDistance = it
                    )
                }
    }
}

sealed class InvalidFinishRideParameters(val message: String) {
    object InvalidCoveredKilometers : InvalidFinishRideParameters("Invalid covered kilometers")
}