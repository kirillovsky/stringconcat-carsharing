package com.stringconcat.kirillov.carsharing.customer.domain

import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.ActuallyDoesNotExists
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.AlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.BirthDateMoreThanRegistrationDate
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.NotMaturedEnough
import com.stringconcat.kirillov.carsharing.fixtures.customer.domain.asYearsAge
import com.stringconcat.kirillov.carsharing.fixtures.customer.domain.customer
import com.stringconcat.kirillov.carsharing.fixtures.customer.domain.driverLicenseNumber
import com.stringconcat.kirillov.carsharing.fixtures.customer.domain.fullName
import com.stringconcat.kirillov.carsharing.fixtures.customer.domain.randomCustomerId
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDate.MAX
import java.time.LocalDate.now
import java.time.Month.JULY
import org.junit.jupiter.api.Test

internal class CustomerTest {
    private val allCustomerAreNotRegistered = CustomerAlreadyRegistered { _, _ -> false }
    private val allCustomerActuallyExists = CustomerActuallyExists { _, _ -> true }
    private val maturedYetBirthDate = LocalDate.of(1992, JULY, 9)
    private val twentyOneYearsOld = 21.asYearsAge()

    @Test
    fun `should register customer`() {
        val id = randomCustomerId()
        val fullName = fullName()
        val driverLicenseNumber = driverLicenseNumber()

        val customer = Customer.registerCustomer(
            idGenerator = { id },
            registrationDate = now(),
            maturityAge = twentyOneYearsOld,
            customerAlreadyRegistered = allCustomerAreNotRegistered,
            customerActuallyExists = allCustomerActuallyExists,
            fullName = fullName,
            birthDate = maturedYetBirthDate,
            driverLicenseNumber = driverLicenseNumber
        )

        customer shouldBeRight {
            it.id shouldBe id
            it.fullName shouldBe fullName
            it.birthDate shouldBe maturedYetBirthDate
            it.driverLicenseNumber shouldBe driverLicenseNumber
            it.isRejected = false
            it.popEvents().shouldContainExactly(CustomerRegistered(customerId = id))
        }
    }

    @Test
    fun `shouldn't register customer if birthDate more than registrationDate`() {
        val registrationDate = now()
        val birthDate = MAX

        val customer = Customer.registerCustomer(
            idGenerator = { randomCustomerId() },
            registrationDate = registrationDate,
            birthDate = birthDate,
            maturityAge = twentyOneYearsOld,
            customerAlreadyRegistered = allCustomerAreNotRegistered,
            customerActuallyExists = allCustomerActuallyExists,
            fullName = fullName(),
            driverLicenseNumber = driverLicenseNumber()
        )

        customer shouldBeLeft {
            it shouldBe BirthDateMoreThanRegistrationDate
        }
    }

    @Test
    fun `shouldn't register customer which not matured enough`() {
        val notMaturedYetBirthDate = LocalDate.of(2000, JULY, 16)
        val currentDate = LocalDate.of(2021, JULY, 17)
        val twentyTwoYearsOld = 22.asYearsAge()

        val customer = Customer.registerCustomer(
            idGenerator = { randomCustomerId() },
            registrationDate = currentDate,
            birthDate = notMaturedYetBirthDate,
            maturityAge = twentyTwoYearsOld,
            customerAlreadyRegistered = allCustomerAreNotRegistered,
            customerActuallyExists = allCustomerActuallyExists,
            fullName = fullName(),
            driverLicenseNumber = driverLicenseNumber()
        )

        customer shouldBeLeft {
            it shouldBe NotMaturedEnough
        }
    }

    @Test
    fun `shouldn't register customer which already registered`() {
        val customer = Customer.registerCustomer(
            idGenerator = { randomCustomerId() },
            registrationDate = now(),
            birthDate = maturedYetBirthDate,
            maturityAge = twentyOneYearsOld,
            customerAlreadyRegistered = { _, _ -> true },
            customerActuallyExists = allCustomerActuallyExists,
            fullName = fullName(),
            driverLicenseNumber = driverLicenseNumber()
        )

        customer shouldBeLeft {
            it shouldBe AlreadyRegistered
        }
    }

    @Test
    fun `shouldn't register customer which actually does not exists`() {
        val customerActuallyDoesNotExists = CustomerActuallyExists { _, _ -> false }

        val customer = Customer.registerCustomer(
            idGenerator = { randomCustomerId() },
            registrationDate = now(),
            birthDate = maturedYetBirthDate,
            maturityAge = twentyOneYearsOld,
            customerAlreadyRegistered = allCustomerAreNotRegistered,
            customerActuallyExists = customerActuallyDoesNotExists,
            fullName = fullName(),
            driverLicenseNumber = driverLicenseNumber()
        )

        customer shouldBeLeft {
            it shouldBe ActuallyDoesNotExists
        }
    }

    @Test
    fun `customer can be rejected`() {
        val id = randomCustomerId()
        val customer = customer(id, rejected = false)

        customer.reject()

        customer should {
            it.isRejected shouldBe true
            it.popEvents().shouldContainExactly(CustomerRejected(customerId = id))
        }
    }

    @Test
    fun `customer should be rejected twice`() {
        val customer = customer(rejected = true)

        customer.reject()

        customer should {
            it.isRejected shouldBe true
            it.popEvents() shouldBe emptyList()
        }
    }
}