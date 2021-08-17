package com.stringconcat.kirillov.carsharing.maintenance.usecase

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

interface IncreaseVehicleMileage {
    fun execute(id: MaintenanceVehicleId, addendum: Distance): Either<IncreaseVehicleMileageUseCaseError, Unit>
}

sealed class IncreaseVehicleMileageUseCaseError(val message: String) {
    object VehicleNotFound : IncreaseVehicleMileageUseCaseError("vehicle not found")
}