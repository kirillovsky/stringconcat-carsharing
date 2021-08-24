package com.stringconcat.kirillov.carsharing.maintenance.persistence

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicle
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.usecase.MaintenanceVehicleExtractor

class StubMaintenanceVehicleExtractor : MaintenanceVehicleExtractor {
    override fun getById(id: MaintenanceVehicleId): MaintenanceVehicle? = null
}