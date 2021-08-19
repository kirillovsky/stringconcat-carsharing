package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.ride.randomRideId
import com.stringconcat.kirillov.carsharing.ride.randomRideVehicleId
import com.stringconcat.kirillov.carsharing.ride.startedRide
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ExtractedRideVehicleInRentTest {
    @Test
    fun `should be false if not found ride with same vehicle id`() {
        val result = ExtractedRideVehicleInRent(rideExtractor = InMemoryRideRepository())
            .check(vehicleId = randomRideVehicleId())

        result shouldBe false
    }

    @Test
    fun `should be true if exists ride with same vehicle id`() {
        val vehicleId = randomRideVehicleId()
        val extractor = InMemoryRideRepository().apply {
            put(randomRideId(), startedRide(vehicleId = vehicleId))
        }

        val result = ExtractedRideVehicleInRent(extractor).check(vehicleId)

        result shouldBe true
    }
}