package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either

interface PayRide {
    fun execute(request: PayRideRequest): Either<PayRideUseCaseError, Unit>
}

sealed class PayRideUseCaseError(val message: String) {
    object RideNotFound : PayRideUseCaseError("ride not found")
    object RideNotInFinishStatusError : PayRideUseCaseError("ride not in finish status error")
}