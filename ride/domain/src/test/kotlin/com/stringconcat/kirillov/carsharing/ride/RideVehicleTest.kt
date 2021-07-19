package com.stringconcat.kirillov.carsharing.ride

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RideVehicleTest {
    @Test
    fun `ride vehicle should contains id`() {
        val expectedId = rideVehicleId()

        val vehicle = RideVehicle(id = expectedId)

        vehicle should {
            it.id shouldBe expectedId
            it.isInRentalPool shouldBe true
            it.popEvents() shouldBe emptyList()
        }
    }

    @Test
    fun `ride vehicle should be added to rental pool`() {
        rideVehicle(inRentalPool = false) should {
            it.isInRentalPool shouldBe false

            it.addToRentalPool()

            it.isInRentalPool shouldBe true
        }
    }

    @Test
    fun `ride vehicle should be removed from rental pool`() {
        rideVehicle(inRentalPool = true) should {
            it.isInRentalPool shouldBe true

            it.removeFromRentalPool()

            it.isInRentalPool shouldBe false
        }
    }
}