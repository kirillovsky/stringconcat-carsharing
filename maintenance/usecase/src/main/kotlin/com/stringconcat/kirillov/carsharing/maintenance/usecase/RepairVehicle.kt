package com.stringconcat.kirillov.carsharing.maintenance.usecase

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

interface RepairVehicle {
    fun execute(id: MaintenanceVehicleId): Either<RepairVehicleUseCaseError, Unit>
}

sealed class RepairVehicleUseCaseError(val message: String) {
    object VehicleNotFound : RepairVehicleUseCaseError("vehicle not found")
}