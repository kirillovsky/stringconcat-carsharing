package com.stringconcat.kirillov.carsharing.ride.usecase

import com.stringconcat.kirillov.carsharing.ride.Ride
import com.stringconcat.kirillov.carsharing.ride.RideId
import com.stringconcat.kirillov.carsharing.ride.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.RideExtractor
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.RidePersister

class InMemoryRideRepository : RideExtractor, RidePersister, HashMap<RideId, Ride>() {
    override fun getBy(id: RideId): Ride? = this[id]

    override fun getByVehicleId(vehicleId: RideVehicleId): Ride? =
        values.firstOrNull { it.vehicleId == vehicleId }

    override fun save(ride: Ride) {
        this[ride.id] = ride
    }
}