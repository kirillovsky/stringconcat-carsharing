package com.stringconcat.kirillov.carsharing.ride.usecase

import com.stringconcat.kirillov.carsharing.ride.Ride
import com.stringconcat.kirillov.carsharing.ride.RideId

interface RideExtractor {
    fun getBy(id: RideId): Ride?
}