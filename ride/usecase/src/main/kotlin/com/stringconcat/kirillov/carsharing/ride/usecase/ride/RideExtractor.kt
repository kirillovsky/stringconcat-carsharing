package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.ride.domain.Ride
import com.stringconcat.kirillov.carsharing.ride.domain.RideId
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId

interface RideExtractor {
    fun getBy(id: RideId): Ride?
    fun getByVehicleId(vehicleId: RideVehicleId): Ride?
}