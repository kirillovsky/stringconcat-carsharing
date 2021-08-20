package com.stringconcat.kirillov.carsharing.customer

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.Month.AUGUST
import java.time.Month.MARCH
import org.junit.jupiter.api.Test

internal class RegisterCustomerUseCaseTest {
    private val customerId = randomCustomerId()
    private val maturityAge = 18.asYearsAge()
    private val birthDate = LocalDate.of(1992, AUGUST, 1)
    private val registrationDate: LocalDate = now()
    private val fullName = fullName()
    private val driverLicenseNumber = driverLicenseNumber()
    private val noOneCustomerAreRegistered = CustomerAlreadyRegistered { _, _ -> false }
    private val allCustomersAreActuallyExists = CustomerActuallyExists { _, _ -> true }

    @Test
    fun `should register customer`() {
        val persister = InMemoryCustomerRepository()
        val usecase = RegisterCustomerUseCase(
            idGenerator = { customerId },
            maturityAge = maturityAge,
            customerAlreadyRegistered = noOneCustomerAreRegistered,
            customerActuallyExists = allCustomersAreActuallyExists,
            customerPersister = persister
        )

        val result = usecase.execute(
            RegisterCustomerRequest(registrationDate, birthDate, fullName, driverLicenseNumber)
        )

        result shouldBeRight customerId
        persister[customerId] should {
            it.shouldNotBeNull()
            it.id shouldBe customerId
            it.birthDate shouldBe birthDate
            it.fullName shouldBe fullName
            it.isRejected shouldBe false
            it.popEvents().shouldContainExactly(CustomerRegistered(customerId = customerId))
        }
    }

    @Test
    fun `shouldn't register customer if it not matured enough`() {
        val persister = InMemoryCustomerRepository()
        val usecase = RegisterCustomerUseCase(
            idGenerator = { customerId },
            maturityAge = maturityAge,
            customerAlreadyRegistered = noOneCustomerAreRegistered,
            customerActuallyExists = allCustomersAreActuallyExists,
            customerPersister = persister
        )

        val result = usecase.execute(
            RegisterCustomerRequest(registrationDate, birthDate = now(), fullName, driverLicenseNumber)
        )

        persister[customerId].shouldBeNull()
        result shouldBeLeft RegisterCustomerUseCaseError.NotMaturedEnough
    }

    @Test
    fun `shouldn't register customer if customer with same fullName and birthDate already registered`() {
        val persister = InMemoryCustomerRepository()
        val existingCustomerFullName = fullName
        val existingCustomerBirthDate = LocalDate.of(1999, AUGUST, 13)
        val usecase = RegisterCustomerUseCase(
            idGenerator = { customerId },
            maturityAge = maturityAge,
            customerAlreadyRegistered = { fullName, birthDate ->
                fullName == existingCustomerFullName && birthDate == existingCustomerBirthDate
            },
            customerActuallyExists = allCustomersAreActuallyExists,
            customerPersister = persister
        )

        val result = usecase.execute(
            request = RegisterCustomerRequest(
                registrationDate,
                birthDate = existingCustomerBirthDate,
                fullName = existingCustomerFullName,
                driverLicenseNumber
            )
        )

        persister[customerId].shouldBeNull()
        result shouldBeLeft RegisterCustomerUseCaseError.AlreadyRegistered
    }

    @Test
    fun `shouldn't create customer if it is not actually exists`() {
        val persister = InMemoryCustomerRepository()
        val notExistsCustomerBirthDate = LocalDate.of(2000, MARCH, 2)
        val notExistsCustomerFullName = fullName()
        val usecase = RegisterCustomerUseCase(
            maturityAge,
            idGenerator = { customerId },
            customerAlreadyRegistered = noOneCustomerAreRegistered,
            customerActuallyExists = { fullName, birthDate ->
                birthDate != notExistsCustomerBirthDate || fullName != notExistsCustomerFullName
            },
            customerPersister = persister
        )

        val result = usecase.execute(
            request = RegisterCustomerRequest(
                registrationDate,
                birthDate = notExistsCustomerBirthDate,
                fullName = notExistsCustomerFullName,
                driverLicenseNumber
            )
        )

        persister[customerId].shouldBeNull()
        result shouldBeLeft RegisterCustomerUseCaseError.ActuallyDoesNotExists
    }

    @Test
    fun `shouldn't create customer if birthDate more than registration date`() {
        val persister = InMemoryCustomerRepository()
        val moreBirthDate = LocalDate.of(2000, AUGUST, 1)
        val lessRegistrationDate = LocalDate.of(1993, AUGUST, 1)
        val usecase = RegisterCustomerUseCase(
            maturityAge,
            idGenerator = { customerId },
            customerAlreadyRegistered = noOneCustomerAreRegistered,
            customerActuallyExists = allCustomersAreActuallyExists,
            customerPersister = persister
        )

        val result = usecase.execute(
            request = RegisterCustomerRequest(
                registrationDate = lessRegistrationDate,
                birthDate = moreBirthDate,
                fullName,
                driverLicenseNumber
            )
        )

        persister[customerId].shouldBeNull()
        result shouldBeLeft RegisterCustomerUseCaseError.BirthDateMoreThanRegistrationDate
    }
}