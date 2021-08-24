package com.stringconcat.kirillov.carsharing.ride.persistence

import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId
import com.stringconcat.kirillov.carsharing.ride.usecase.customer.RideCustomerExtractor

class StubRideCustomerExtractor : RideCustomerExtractor {
    override fun getById(id: RideCustomerId): RideCustomer? = null
}