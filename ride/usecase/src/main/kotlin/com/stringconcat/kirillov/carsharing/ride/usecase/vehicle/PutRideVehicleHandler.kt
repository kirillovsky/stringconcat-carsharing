package com.stringconcat.kirillov.carsharing.ride.usecase.vehicle

import com.stringconcat.kirillov.carsharing.ride.RideVehicle
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId

class PutRideVehicleHandler(private val persister: RideVehiclePersister) : PutRideVehicle {
    override fun execute(id: RideVehicleId, inRentalPool: Boolean) {
        val vehicle = RideVehicle(id)

        if (inRentalPool) {
            vehicle.addToRentalPool()
        } else {
            vehicle.removeFromRentalPool()
        }

        persister.save(vehicle)
    }
}