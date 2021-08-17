package com.stringconcat.kirillov.carsharing.maintenance.usecase

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

interface DetectVehiclesFault {
    fun execute(id: MaintenanceVehicleId): Either<DetectVehiclesFaultUseCaseError, Unit>
}

sealed class DetectVehiclesFaultUseCaseError(val message: String) {
    object VehicleNotFound : DetectVehiclesFaultUseCaseError("maintenance vehicle not found")
}