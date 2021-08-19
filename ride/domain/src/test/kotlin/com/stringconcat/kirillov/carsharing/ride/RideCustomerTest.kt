package com.stringconcat.kirillov.carsharing.ride

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RideCustomerTest {
    @Test
    fun `ride customer should contains id and is not verified`() {
        val expectedId = randomRideCustomerId()

        val customer = RideCustomer(id = expectedId)

        customer should {
            it.id shouldBe expectedId
            it.isRejected shouldBe false
            it.popEvents() shouldBe emptyList()
        }
    }

    @Test
    fun `ride customer shouldn't be verified after rejection`() {
        rideCustomer() should {
            it.isRejected shouldBe false

            it.reject()

            it.isRejected shouldBe true
        }
    }
}