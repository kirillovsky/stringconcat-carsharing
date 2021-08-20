package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId

interface AddVehicleToInventory {
    fun execute(request: AddVehicleToInventoryRequest): MaintenanceVehicleId
}