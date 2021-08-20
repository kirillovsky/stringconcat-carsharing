package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.RideCustomerId

class PutRideCustomerHandler(private val persister: RideCustomerPersister) : PutRideCustomer {
    override fun execute(id: RideCustomerId, needsToReject: Boolean) {
        persister.save(
            customer = RideCustomer(id).apply {
                if (needsToReject) reject()
            }
        )
    }
}