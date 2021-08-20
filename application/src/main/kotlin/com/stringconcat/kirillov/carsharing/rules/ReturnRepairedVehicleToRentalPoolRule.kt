package com.stringconcat.kirillov.carsharing.rules

import com.stringconcat.kirillov.carsharing.listeners.DomainEventListener
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleRepaired
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.PutRideVehicle

class ReturnRepairedVehicleToRentalPoolRule(
    private val putRideVehicle: PutRideVehicle
) : DomainEventListener<VehicleRepaired> {
    override fun handle(event: VehicleRepaired) {
        putRideVehicle.execute(
            id = RideVehicleId(event.vehicleId.value),
            inRentalPool = true
        )
    }
}