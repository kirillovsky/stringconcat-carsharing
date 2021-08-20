package com.stringconcat.kirillov.carsharing.ride.usecase.customer

import com.stringconcat.kirillov.carsharing.ride.rideCustomer
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideCustomerRepository
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PutRideCustomerHandlerTest {
    @Test
    fun `should put ride customer`() {
        val repo = InMemoryRideCustomerRepository()
        val usecase = PutRideCustomerHandler(persister = repo)
        val customer = rideCustomer(rejected = false)

        usecase.execute(customer.id, customer.isRejected)

        repo[customer.id] should {
            it.shouldNotBeNull()
            it.id shouldBe customer.id
            it.isRejected shouldBe customer.isRejected
        }
    }

    @Test
    fun `should reject put ride customer only if needs`() {
        val repo = InMemoryRideCustomerRepository()
        val usecase = PutRideCustomerHandler(persister = repo)
        val customer = rideCustomer(rejected = false)

        usecase.execute(customer.id, needsToReject = true)

        repo[customer.id] should {
            it.shouldNotBeNull()
            it.id shouldBe customer.id
            it.isRejected shouldBe true
        }
    }
}