package com.stringconcat.kirillov.carsharing.rules

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrHandle
import arrow.core.right
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.listeners.DomainEventListener
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.usecase.IncreaseVehicleMileage
import com.stringconcat.kirillov.carsharing.maintenance.usecase.IncreaseVehicleMileageUseCaseError
import com.stringconcat.kirillov.carsharing.ride.RideFinishedEvent
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.RideExtractor

class SendCoveredDistanceRule(
    private val increaseVehicleMileage: IncreaseVehicleMileage,
    private val rideExtractor: RideExtractor
) : DomainEventListener<RideFinishedEvent> {
    override fun handle(event: RideFinishedEvent) {
        rideExtractor.getBy(event.rideId)
            .rightIfNotNull { "Unable to find ride by id=${event.rideId}" }
            .flatMap {
                if (it.isFinished().not()) {
                    Either.left("Found ride(id=${it.id}) is not finished")
                } else {
                    it.right()
                }
            }
            .flatMap {
                increaseVehicleMileage.execute(
                    id = MaintenanceVehicleId(it.id.value),
                    addendum = it.coveredDistance
                ).mapLeft(
                    IncreaseVehicleMileageUseCaseError::message
                )
            }.getOrHandle(::error)
    }
}