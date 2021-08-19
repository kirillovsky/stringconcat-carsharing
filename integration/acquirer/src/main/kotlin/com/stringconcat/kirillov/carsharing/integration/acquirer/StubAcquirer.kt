package com.stringconcat.kirillov.carsharing.integration.acquirer

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.ride.RideCustomerId

object StubAcquirer : Acquirer {
    override fun debitPriceFromCustomer(id: RideCustomerId, price: Price) {
        println("Successfully debit price($price) from customer(id=$id)")
    }
}