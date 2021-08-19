package com.stringconcat.kirillov.carsharing.ride.usecase

import com.stringconcat.kirillov.carsharing.ride.RideVehicle
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.RideVehicleExtractor

class InMemoryRideVehicleRepository : RideVehicleExtractor, HashMap<RideVehicleId, RideVehicle>() {
    override fun getById(id: RideVehicleId): RideVehicle? = this[id]
}
