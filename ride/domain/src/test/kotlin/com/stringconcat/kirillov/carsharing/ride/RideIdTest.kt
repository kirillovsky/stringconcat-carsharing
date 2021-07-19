package com.stringconcat.kirillov.carsharing.ride

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RideIdTest {
    @Test
    fun `ride id should be equals to another rideId by value`() {
        val oneRideId = RideId(value = 100L)
        val anotherRideId = RideId(value = 100L)

        oneRideId shouldBe anotherRideId
    }
}