package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.FinishRideUseCaseError.RideAlreadyFinishedError
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.FinishRideUseCaseError.RideNotFound

class FinishRideUseCase(
    private val extractor: RideExtractor,
    private val persister: RidePersister
) : FinishRide {
    override fun execute(request: FinishRideRequest): Either<FinishRideUseCaseError, Unit> =
        extractor.getBy(request.rideId)
            .rightIfNotNull { RideNotFound }
            .flatMap { ride ->
                ride.finish(request.finishedDateTime, request.coveredDistance)
                    .map { ride }
                    .mapLeft { RideAlreadyFinishedError }
            }
            .map(persister::save)
}