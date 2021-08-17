package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicle

interface MaintenanceVehiclePersister {
    fun save(vehicle: MaintenanceVehicle)
}
