package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicle
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

class AddVehicleToInventoryHandler(
    private val vehiclePersister: MaintenanceVehiclePersister,
    private val vehicleExtractor: MaintenanceVehicleExtractor
) : AddVehicleToInventory {
    override fun execute(
        request: AddVehicleToInventoryRequest
    ): MaintenanceVehicleId {
        val id = request.id
        if (vehicleExtractor.getById(id) != null) return id

        val maintenanceVehicle = MaintenanceVehicle.addVehicleToInventory(
            id = id,
            model = request.model,
            vin = request.vin,
            coveredMileage = request.coveredMileage,
            registrationPlate = request.registrationPlate
        )

        vehiclePersister.save(maintenanceVehicle)

        return maintenanceVehicle.id
    }
}