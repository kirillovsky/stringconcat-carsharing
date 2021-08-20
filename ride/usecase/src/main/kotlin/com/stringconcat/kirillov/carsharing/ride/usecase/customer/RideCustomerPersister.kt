package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomer

interface RideCustomerPersister {
    fun save(customer: RideCustomer)
}
