package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId

interface RideCustomerExtractor {
    fun getById(id: RideCustomerId): RideCustomer?
}
