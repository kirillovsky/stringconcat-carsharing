package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.ride.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.RideVehicleInRent

class ExtractedRideVehicleInRent(private val rideExtractor: RideExtractor) : RideVehicleInRent {
    override fun check(vehicleId: RideVehicleId): Boolean =
        rideExtractor.getByVehicleId(vehicleId) != null
}