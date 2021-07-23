package com.stringconcat.kirillov.carsharing.maintenance.domain

import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleAddedToMaintenanceInventory
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleBroken
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleCoveredMileageIncreased
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleRepaired

class MaintenanceVehicle internal constructor(
    id: MaintenanceVehicleId,
    val model: VehicleModel,
    val vin: Vin,
    coveredMileage: Distance,
) : AggregateRoot<MaintenanceVehicleId>(id) {
    companion object {
        fun addVehicleToInventory(
            id: MaintenanceVehicleId,
            model: VehicleModel,
            vin: Vin,
            coveredMileage: Distance,
        ) = MaintenanceVehicle(id, model, vin, coveredMileage).apply {
            addEvent(
                VehicleAddedToMaintenanceInventory(vehicleId = id)
            )
        }
    }

    var broken: Boolean = false
        internal set

    var coveredMileage = coveredMileage
        private set

    fun increaseMileage(additional: Distance) {
        coveredMileage += additional

        addEvent(
            VehicleCoveredMileageIncreased(vehicleId = id, added = additional)
        )
    }

    fun detectFault() {
        if (broken) return

        broken = true

        addEvent(
            VehicleBroken(vehicleId = id)
        )
    }

    fun repair() {
        if (!broken) return

        broken = false

        addEvent(
            VehicleRepaired(vehicleId = id)
        )
    }
}
