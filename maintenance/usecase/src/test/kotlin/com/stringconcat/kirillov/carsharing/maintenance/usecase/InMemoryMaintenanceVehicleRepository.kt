package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicle
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

class InMemoryMaintenanceVehicleRepository : MaintenanceVehiclePersister, MaintenanceVehicleExtractor,
    HashMap<MaintenanceVehicleId, MaintenanceVehicle>() {
    override fun save(vehicle: MaintenanceVehicle) {
        this[vehicle.id] = vehicle
    }

    override fun getById(id: MaintenanceVehicleId): MaintenanceVehicle? = this[id]
}