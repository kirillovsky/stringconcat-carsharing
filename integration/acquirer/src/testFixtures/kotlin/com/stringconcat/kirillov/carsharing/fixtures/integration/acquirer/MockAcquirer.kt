package com.stringconcat.kirillov.carsharing.fixtures.integration.acquirer

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.integration.acquirer.Acquirer
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId

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