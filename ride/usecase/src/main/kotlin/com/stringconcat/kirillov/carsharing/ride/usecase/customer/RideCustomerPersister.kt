package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.RideCustomer

interface RideCustomerPersister {
    fun save(customer: RideCustomer)
}
