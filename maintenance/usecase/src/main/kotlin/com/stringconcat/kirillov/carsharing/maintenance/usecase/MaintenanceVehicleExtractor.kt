package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicle
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

interface MaintenanceVehicleExtractor {
    fun getById(id: MaintenanceVehicleId): MaintenanceVehicle?
}
