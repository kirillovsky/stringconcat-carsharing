package com.stringconcat.kirillov.carsharing.integration.acquirer

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.ride.RideCustomerId

interface Acquirer {
    fun debitPriceFromCustomer(id: RideCustomerId, price: Price)
}