package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.ride.RideCustomerId
import com.stringconcat.kirillov.carsharing.ride.RideId
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId
import java.time.OffsetDateTime

interface StartRide {
    fun execute(request: StartRideRequest): Either<StartRideUseCaseError, RideId>
}

class StartRideRequest(
    val customerId: RideCustomerId,
    val vehicleId: RideVehicleId,
    val startDateTime: OffsetDateTime
)

sealed class StartRideUseCaseError(val message: String) {
    object CustomerNotFound : StartRideUseCaseError("customer not found")
    object VehicleNotFound : StartRideUseCaseError("vehicle not found")
    object CustomerIsRejected : StartRideUseCaseError("customer is rejected")
    object VehicleNotInRentalPool : StartRideUseCaseError("vehicle not in rental pool")
    object VehicleAlreadyInRent : StartRideUseCaseError("vehicle already in rent")
}