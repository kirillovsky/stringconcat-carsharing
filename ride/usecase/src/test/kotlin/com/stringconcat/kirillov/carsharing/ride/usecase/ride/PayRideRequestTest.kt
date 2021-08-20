package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.ride.domain.randomRideId
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.InvalidPayRideParametersError.InvalidPriceValue
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import org.junit.jupiter.api.Test

internal class PayRideRequestTest {
    @Test
    fun `should contains parameters for further pay ride`() {
        val price = randomPrice()
        val rideId = randomRideId()

        val result = PayRideRequest.from(rideId.value, price.value)

        result shouldBeRight PayRideRequest(rideId, price)
    }

    @Test
    fun `shouldn't create request with negative priceValue`() {
        val result = PayRideRequest.from(
            rideIdValue = randomRideId().value,
            priceValue = (-100501).toBigDecimal()
        )

        result shouldBeLeft InvalidPriceValue
    }
}