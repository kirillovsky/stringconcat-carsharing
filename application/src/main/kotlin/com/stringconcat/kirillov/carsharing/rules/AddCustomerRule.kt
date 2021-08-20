package com.stringconcat.kirillov.carsharing.rules

import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistered
import com.stringconcat.kirillov.carsharing.listeners.DomainEventListener
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId
import com.stringconcat.kirillov.carsharing.ride.usecase.customer.PutRideCustomer

class AddCustomerRule(private val putRideCustomer: PutRideCustomer) : DomainEventListener<CustomerRegistered> {
    override fun handle(event: CustomerRegistered) {
        putRideCustomer.execute(
            id = RideCustomerId(event.customerId.value),
            needsToReject = false
        )
    }
}