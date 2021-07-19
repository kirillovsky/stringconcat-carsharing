package com.stringconcat.kirillov.carsharing.ride

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RideCustomerTest {
    @Test
    fun `ride customer should contains id and is not verified`() {
        val expectedId = rideCustomerId()

        val customer = RideCustomer(id = expectedId)

        customer should {
            it.id shouldBe expectedId
            it.isVerified shouldBe false
            it.popEvents() shouldBe emptyList()
        }
    }

    @Test
    fun `ride customer can be verified`() {
        rideCustomer(verified = false) should {
            it.isVerified shouldBe false

            it.verify()

            it.isVerified shouldBe true
        }
    }

    @Test
    fun `ride customer shouldn't be verified after rejection`() {
        rideCustomer(verified = true) should {
            it.isVerified shouldBe true

            it.reject()

            it.isVerified shouldBe false
        }
    }
}