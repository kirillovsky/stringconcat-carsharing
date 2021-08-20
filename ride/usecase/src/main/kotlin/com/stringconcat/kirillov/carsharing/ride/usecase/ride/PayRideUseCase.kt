package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.PayRideUseCaseError.RideNotFound
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.PayRideUseCaseError.RideNotInFinishStatusError

class PayRideUseCase(
    private val extractor: RideExtractor,
    private val persister: RidePersister
) : PayRide {
    override fun execute(request: PayRideRequest): Either<PayRideUseCaseError, Unit> =
        extractor.getBy(request.rideId)
            .rightIfNotNull { RideNotFound }
            .flatMap { ride ->
                ride.pay(request.price)
                    .map { persister.save(ride) }
                    .mapLeft { RideNotInFinishStatusError }
            }
}