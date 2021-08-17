package com.stringconcat.kirillov.carsharing.maintenance.usecase

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

class RepairVehicleUseCase(
    private val persister: MaintenanceVehiclePersister,
    private val vehicleExtractor: MaintenanceVehicleExtractor
) : RepairVehicle {
    override fun execute(id: MaintenanceVehicleId): Either<RepairVehicleUseCaseError, Unit> =
        vehicleExtractor.getById(id).rightIfNotNull {
            RepairVehicleUseCaseError.VehicleNotFound
        }.map {
            it.repair()

            persister.save(it)
        }
}