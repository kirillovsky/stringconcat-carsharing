package com.stringconcat.kirillov.carsharing.integration.acquirer

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.ride.RideCustomerId

class MockAcquirer : Acquirer {
    var receivedId: RideCustomerId? = null
        private set
    var receivedPrice: Price? = null
        private set

    override fun debitPriceFromCustomer(id: RideCustomerId, price: Price) {
        receivedId = id
        receivedPrice = price
    }
}