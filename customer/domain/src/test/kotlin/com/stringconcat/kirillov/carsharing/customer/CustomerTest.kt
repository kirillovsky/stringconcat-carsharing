package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.ActuallyDoesNotExists
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.AlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.BirthDateMoreThanRegistrationDate
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
import java.time.LocalDate.MAX
import java.time.LocalDate.now
import java.time.Month.JULY
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE

internal class CustomerTest {
    private val allCustomerAreNotRegistered = CustomerAlreadyRegistered { _, _ -> false }
    private val allCustomerActuallyExists = CustomerActuallyExists { _, _ -> true }
    private val maturedYetBirthDate = LocalDate.of(1992, JULY, 9)
    private val twentyOneYearsOld = 21.asYearsAge()

    @Test
    fun `should register customer`() {
        val id = customerId()
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
            it.status shouldBe REGISTERED
            it.popEvents().shouldContainExactly(CustomerRegistered(value = id))
        }
    }

    @Test
    fun `shouldn't register customer if birthDate more than registrationDate`() {
        val registrationDate = now()
        val birthDate = MAX

        val customer = Customer.registerCustomer(
            idGenerator = { customerId() },
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
            idGenerator = { customerId() },
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
        val customerAlreadyRegistered = CustomerAlreadyRegistered { _, _ -> true }

        val customer = Customer.registerCustomer(
            idGenerator = { customerId() },
            registrationDate = now(),
            birthDate = maturedYetBirthDate,
            maturityAge = twentyOneYearsOld,
            customerAlreadyRegistered = customerAlreadyRegistered,
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
            idGenerator = { customerId() },
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