package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.randomRideId
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.InvalidFinishRideParameters.InvalidCoveredKilometers
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import java.time.OffsetDateTime
import org.junit.jupiter.api.Test

internal class FinishRideRequestTest {
    private val rideId = randomRideId()
    private val finishDate = OffsetDateTime.MAX
    private val coveredDistance = randomDistance()

    @Test
    fun `should contains parameters for further finishing ride`() {
        val request = FinishRideRequest.from(
            rideId = rideId.value,
            finishDate,
            coveredKilometers = coveredDistance.kilometers
        )

        request shouldBeRight FinishRideRequest(rideId, finishDate, coveredDistance)
    }

    @Test
    fun `shouldn't create request if coveredKilometers is negative`() {
        val request = FinishRideRequest.from(
            rideId = rideId.value,
            finishDate,
            coveredKilometers = (-100500).toBigDecimal()
        )

        request shouldBeLeft InvalidCoveredKilometers
    }
}