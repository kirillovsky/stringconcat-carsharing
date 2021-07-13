package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.ActuallyDoesNotExists
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.AlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.NotMaturedEnough
import com.stringconcat.kirillov.carsharing.customer.CustomerStatus.REGISTERED
import com.stringconcat.kirillov.carsharing.customer.CustomerStatus.REJECTED
import com.stringconcat.kirillov.carsharing.customer.CustomerStatus.VERIFIED
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDate.EPOCH
import java.time.Month.JULY
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE

internal class CustomerTest {
    private val allCustomerAreMaturedEnough = CustomerIsMaturedEnough { true }
    private val allCustomerAreNotRegistered = CustomerAlreadyRegistered { _, _ -> false }
    private val allCustomerActuallyExists = CustomerActuallyExists { _, _ -> true }

    @Test
    fun `registerCustomer should create customer in context`() {
        val id = customerId()
        val fullName = fullName()
        val birthDate = LocalDate.of(1992, JULY, 9)
        val driverLicenseNumber = driverLicenseNumber()

        val customer = Customer.registerCustomer(
            idGenerator = { id },
            customerIsMaturedEnough = allCustomerAreMaturedEnough,
            customerAlreadyRegistered = allCustomerAreNotRegistered,
            customerActuallyExists = allCustomerActuallyExists,
            fullName = fullName,
            birthDate = birthDate,
            driverLicenseNumber = driverLicenseNumber
        )

        customer shouldBeRight {
            it.id shouldBe id
            it.fullName shouldBe fullName
            it.birthDate shouldBe birthDate
            it.driverLicenseNumber shouldBe driverLicenseNumber
            it.status shouldBe REGISTERED
            it.popEvents().shouldContainExactly(CustomerRegistered(value = id))
        }
    }

    @Test
    fun `registerCustomer shouldn't create customer which not matured enough`() {
        val customerNotMaturedEnough = CustomerIsMaturedEnough { false }

        val customer = Customer.registerCustomer(
            idGenerator = { customerId() },
            customerIsMaturedEnough = customerNotMaturedEnough,
            customerAlreadyRegistered = allCustomerAreNotRegistered,
            customerActuallyExists = allCustomerActuallyExists,
            fullName = fullName(),
            birthDate = EPOCH,
            driverLicenseNumber = driverLicenseNumber()
        )

        customer shouldBeLeft {
            it shouldBe NotMaturedEnough
        }
    }

    @Test
    fun `registerCustomer shouldn't create customer which already registered`() {
        val customerAlreadyRegistered = CustomerAlreadyRegistered { _, _ -> true }

        val customer = Customer.registerCustomer(
            idGenerator = { customerId() },
            customerIsMaturedEnough = allCustomerAreMaturedEnough,
            customerAlreadyRegistered = customerAlreadyRegistered,
            customerActuallyExists = allCustomerActuallyExists,
            fullName = fullName(),
            birthDate = EPOCH,
            driverLicenseNumber = driverLicenseNumber()
        )

        customer shouldBeLeft {
            it shouldBe AlreadyRegistered
        }
    }

    @Test
    fun `registerCustomer shouldn't create customer which actually does not exists`() {
        val customerActuallyDoesNotExists = CustomerActuallyExists { _, _ -> false }

        val customer = Customer.registerCustomer(
            idGenerator = { customerId() },
            customerIsMaturedEnough = allCustomerAreMaturedEnough,
            customerAlreadyRegistered = allCustomerAreNotRegistered,
            customerActuallyExists = customerActuallyDoesNotExists,
            fullName = fullName(),
            birthDate = EPOCH,
            driverLicenseNumber = driverLicenseNumber()
        )

        customer shouldBeLeft {
            it shouldBe ActuallyDoesNotExists
        }
    }

    @ParameterizedTest(name = "customer with status - {0} can be rejected")
    @EnumSource(value = CustomerStatus::class, mode = EXCLUDE, names = ["REJECTED"])
    fun `customer with status can be rejected`(customerStatus: CustomerStatus) {
        val id = customerId()
        val customer = customer(id, customerStatus)

        customer.reject()

        customer should {
            it.status shouldBe REJECTED
            it.popEvents().shouldContainExactly(CustomerRejected(value = id))
        }
    }

    @Test
    fun `customer should be rejected twice`() {
        val customer = customer(customerStatus = REJECTED)

        customer.reject()

        customer should {
            it.status shouldBe REJECTED
            it.popEvents() shouldBe emptyList()
        }
    }

    @ParameterizedTest(name = "customer with status - {0} can be verified")
    @EnumSource(value = CustomerStatus::class, mode = EXCLUDE, names = ["VERIFIED"])
    fun `customer with status can be verified`(customerStatus: CustomerStatus) {
        val id = customerId()
        val customer = customer(id, customerStatus)

        customer.verify()

        customer should {
            it.status shouldBe VERIFIED
            it.popEvents().shouldContainExactly(CustomerVerified(value = id))
        }
    }

    @Test
    fun `customer should be verified twice`() {
        val customer = customer(customerStatus = VERIFIED)

        customer.verify()

        customer should {
            it.status shouldBe VERIFIED
            it.popEvents() shouldBe emptyList()
        }
    }
}