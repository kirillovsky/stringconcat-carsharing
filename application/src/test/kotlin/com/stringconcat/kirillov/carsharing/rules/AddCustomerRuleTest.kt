package com.stringconcat.kirillov.carsharing.rules

import com.stringconcat.kirillov.carsharing.customer.CustomerRegistered
import com.stringconcat.kirillov.carsharing.customer.randomCustomerId
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId
import com.stringconcat.kirillov.carsharing.ride.usecase.MockPutRideCustomerUseCase
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AddCustomerRuleTest {
    @Test
    fun `should add customer to ride context`() {
        val customerId = randomCustomerId()
        val putRideCustomer = MockPutRideCustomerUseCase()
        val rule = AddCustomerRule(putRideCustomer = putRideCustomer)

        rule.handle(event = CustomerRegistered(customerId))

        putRideCustomer should {
            it.receivedRideCustomerId shouldBe RideCustomerId(customerId.value)
            it.receivedNeedsToRejected shouldBe false
        }
    }
}