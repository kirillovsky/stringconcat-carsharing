package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleInRent

class ExtractedRideVehicleInRent(private val rideExtractor: RideExtractor) : RideVehicleInRent {
    override fun check(vehicleId: RideVehicleId): Boolean =
        rideExtractor.getByVehicleId(vehicleId) != null
}