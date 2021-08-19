package com.stringconcat.kirillov.carsharing.ride.usecase

import com.stringconcat.kirillov.carsharing.ride.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.RideCustomerId
import com.stringconcat.kirillov.carsharing.ride.usecase.customer.RideCustomerExtractor

class InMemoryRideCustomerRepository : RideCustomerExtractor, HashMap<RideCustomerId, RideCustomer>() {
    override fun getById(id: RideCustomerId): RideCustomer? = this[id]
}