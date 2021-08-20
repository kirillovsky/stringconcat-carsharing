package com.stringconcat.kirillov.carsharing.rules

import com.stringconcat.kirillov.carsharing.listeners.DomainEventListener
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleAddedToMaintenanceInventory
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.PutRideVehicle

class AddNewVehicleToRentalPool(
    private val putRideVehicle: PutRideVehicle
) : DomainEventListener<VehicleAddedToMaintenanceInventory> {
    override fun handle(event: VehicleAddedToMaintenanceInventory) {
        putRideVehicle.execute(
            id = RideVehicleId(event.vehicleId.value),
            inRentalPool = true
        )
    }
}