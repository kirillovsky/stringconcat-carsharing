package com.stringconcat.kirillov.carsharing.ride.usecase.vehicle

import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicle

interface RideVehiclePersister {
    fun save(vehicle: RideVehicle)
}