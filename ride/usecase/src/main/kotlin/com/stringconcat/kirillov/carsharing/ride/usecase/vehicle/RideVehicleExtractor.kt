package com.stringconcat.kirillov.carsharing.ride.usecase.vehicle

import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicle
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId

interface RideVehicleExtractor {
    fun getById(id: RideVehicleId): RideVehicle?
}
