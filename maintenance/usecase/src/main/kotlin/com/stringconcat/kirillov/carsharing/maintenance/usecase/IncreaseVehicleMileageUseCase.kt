package com.stringconcat.kirillov.carsharing.maintenance.usecase

import arrow.core.Either
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

class IncreaseVehicleMileageUseCase(
    private val persister: MaintenanceVehiclePersister,
    private val vehicleExtractor: MaintenanceVehicleExtractor
) : IncreaseVehicleMileage {
    override fun execute(
        id: MaintenanceVehicleId,
        addendum: Distance
    ): Either<IncreaseVehicleMileageUseCaseError, Unit> =
        vehicleExtractor.getById(id).rightIfNotNull {
            IncreaseVehicleMileageUseCaseError.VehicleNotFound
        }.map {
            it.increaseMileage(addendum)

            persister.save(it)
        }
}