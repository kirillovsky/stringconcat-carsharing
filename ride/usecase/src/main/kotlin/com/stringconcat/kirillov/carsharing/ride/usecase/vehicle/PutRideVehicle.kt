package com.stringconcat.kirillov.carsharing.ride.usecase.vehicle

import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId

interface PutRideVehicle {
    fun execute(id: RideVehicleId, inRentalPool: Boolean)
}