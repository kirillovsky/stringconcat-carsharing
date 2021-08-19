package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.ride.Ride
import com.stringconcat.kirillov.carsharing.ride.RideId
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId

interface RideExtractor {
    fun getBy(id: RideId): Ride?
    fun getByVehicleId(vehicleId: RideVehicleId): Ride?
}