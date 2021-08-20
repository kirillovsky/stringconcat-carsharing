package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either

interface FinishRide {
    fun execute(request: FinishRideRequest): Either<FinishRideUseCaseError, Unit>
}

sealed class FinishRideUseCaseError(val message: String) {
    object RideNotFound : FinishRideUseCaseError("ride not found")
    object RideAlreadyFinishedError : FinishRideUseCaseError("ride already finished error")
}
