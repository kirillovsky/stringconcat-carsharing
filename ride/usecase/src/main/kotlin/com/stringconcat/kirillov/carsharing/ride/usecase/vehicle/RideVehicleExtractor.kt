package com.stringconcat.kirillov.carsharing.ride.usecase.vehicle

import com.stringconcat.kirillov.carsharing.ride.RideVehicle
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId

interface RideVehicleExtractor {
    fun getById(id: RideVehicleId): RideVehicle?
}
