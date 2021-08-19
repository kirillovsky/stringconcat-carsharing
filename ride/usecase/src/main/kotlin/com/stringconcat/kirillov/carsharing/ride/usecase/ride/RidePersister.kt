package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.ride.Ride

interface RidePersister {
    fun save(ride: Ride)
}