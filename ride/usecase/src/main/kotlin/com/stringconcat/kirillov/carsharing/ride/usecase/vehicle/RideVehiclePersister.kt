package com.stringconcat.kirillov.carsharing.ride.usecase.vehicle

import com.stringconcat.kirillov.carsharing.ride.RideVehicle

interface RideVehiclePersister {
    fun save(vehicle: RideVehicle)
}
