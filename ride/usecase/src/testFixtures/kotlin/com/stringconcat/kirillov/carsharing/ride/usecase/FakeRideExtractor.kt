package com.stringconcat.kirillov.carsharing.ride.usecase

import com.stringconcat.kirillov.carsharing.ride.Ride
import com.stringconcat.kirillov.carsharing.ride.RideId

class FakeRideExtractor : RideExtractor, HashMap<RideId, Ride>() {
    override fun getBy(id: RideId): Ride? = this[id]
}