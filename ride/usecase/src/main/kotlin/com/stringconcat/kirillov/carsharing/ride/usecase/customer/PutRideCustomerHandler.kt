package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId

class PutRideCustomerHandler(private val persister: RideCustomerPersister) : PutRideCustomer {
    override fun execute(id: RideCustomerId, needsToReject: Boolean) {
        persister.save(
            customer = RideCustomer(id).apply {
                if (needsToReject) reject()
            }
        )
    }
}