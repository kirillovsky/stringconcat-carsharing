package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.ride.domain.Ride
import com.stringconcat.kirillov.carsharing.ride.domain.RideId
import com.stringconcat.kirillov.carsharing.ride.domain.RideIdGenerator
import com.stringconcat.kirillov.carsharing.ride.domain.RideStartingError
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleInRent
import com.stringconcat.kirillov.carsharing.ride.usecase.customer.RideCustomerExtractor
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.StartRideUseCaseError.CustomerNotFound
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.StartRideUseCaseError.VehicleNotFound
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.RideVehicleExtractor

class StartRideUseCase(
    private val idGenerator: RideIdGenerator,
    private val vehicleInRent: RideVehicleInRent,
    private val customerExtractor: RideCustomerExtractor,
    private val vehicleExtractor: RideVehicleExtractor,
    private val ridePersister: RidePersister
) : StartRide {
    override fun execute(request: StartRideRequest): Either<StartRideUseCaseError, RideId> =
        tupled(
            customerExtractor.getById(request.customerId).rightIfNotNull { CustomerNotFound },
            vehicleExtractor.getById(request.vehicleId).rightIfNotNull { VehicleNotFound }
        ).flatMap { (customer, vehicle) ->
            Ride.startRide(vehicleInRent, idGenerator, customer, vehicle, startDateTime = request.startDateTime)
                .mapLeft { it.toErrorMessage() }
        }.map {
            ridePersister.save(it)

            it.id
        }
}

private fun RideStartingError.toErrorMessage(): StartRideUseCaseError =
    when (this) {
        RideStartingError.CustomerIsRejected -> StartRideUseCaseError.CustomerIsRejected
        RideStartingError.VehicleNotInRentalPool -> StartRideUseCaseError.VehicleNotInRentalPool
        RideStartingError.VehicleAlreadyInRent -> StartRideUseCaseError.VehicleAlreadyInRent
    }