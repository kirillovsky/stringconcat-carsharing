package com.stringconcat.kirillov.carsharing.rules

import com.stringconcat.kirillov.carsharing.listeners.DomainEventListener
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleBroken
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.PutRideVehicle

class RemoveBrokenVehicleFromRentalPoolRule(
    private val putRideVehicle: PutRideVehicle
) : DomainEventListener<VehicleBroken> {
    override fun handle(event: VehicleBroken) {
        putRideVehicle.execute(
            id = RideVehicleId(event.vehicleId.value),
            inRentalPool = false
        )
    }
}