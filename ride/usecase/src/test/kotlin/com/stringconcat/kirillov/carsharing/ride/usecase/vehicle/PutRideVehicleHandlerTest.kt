package com.stringconcat.kirillov.carsharing.ride.usecase.vehicle

import com.stringconcat.kirillov.carsharing.ride.domain.rideVehicle
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideVehicleRepository
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PutRideVehicleHandlerTest {
    @Test
    fun `should add to rental pool vehicle`() {
        val repo = InMemoryRideVehicleRepository()
        val vehicle = rideVehicle(inRentalPool = true)
        val usecase = PutRideVehicleHandler(persister = repo)

        usecase.execute(vehicle.id, inRentalPool = true)

        repo[vehicle.id] should {
            it.shouldNotBeNull()
            it.id shouldBe vehicle.id
            it.isInRentalPool shouldBe true
        }
    }

    @Test
    fun `should remove from rental pool vehicle`() {
        val repo = InMemoryRideVehicleRepository()
        val vehicle = rideVehicle(inRentalPool = false)
        val usecase = PutRideVehicleHandler(persister = repo)

        usecase.execute(vehicle.id, inRentalPool = false)

        repo[vehicle.id] should {
            it.shouldNotBeNull()
            it.id shouldBe vehicle.id
            it.isInRentalPool shouldBe false
        }
    }
}