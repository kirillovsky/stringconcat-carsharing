package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.RideCustomerId

interface RideCustomerExtractor {
    fun getById(id: RideCustomerId): RideCustomer?
}
