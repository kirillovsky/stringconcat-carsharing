package com.stringconcat.kirillov.carsharing.maintenance.usecase

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

class DetectVehiclesFaultUseCase(
    private val vehicleExtractor: MaintenanceVehicleExtractor,
    private val persister: MaintenanceVehiclePersister
) : DetectVehiclesFault {
    override fun execute(id: MaintenanceVehicleId): Either<DetectVehiclesFaultUseCaseError, Unit> =
        vehicleExtractor.getById(id)
            .rightIfNotNull { DetectVehiclesFaultUseCaseError.VehicleNotFound }
            .map {
                it.detectFault()

                persister.save(it)
            }
}