package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.RideCustomerId

interface PutRideCustomer {
    fun execute(id: RideCustomerId, needsToReject: Boolean)
}