package com.stringconcat.kirillov.carsharing.maintenance.domain

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance

object MaintenanceVehicleEvents {
    data class VehicleBroken(val vehicleId: MaintenanceVehicleId) : DomainEvent()

    data class VehicleCoveredMileageIncreased(
        val vehicleId: MaintenanceVehicleId,
        val added: Distance,
    ) : DomainEvent()

    data class VehicleRepaired(val vehicleId: MaintenanceVehicleId) : DomainEvent()

    data class VehicleAddedToMaintenanceInventory(val vehicleId: MaintenanceVehicleId) : DomainEvent()
}