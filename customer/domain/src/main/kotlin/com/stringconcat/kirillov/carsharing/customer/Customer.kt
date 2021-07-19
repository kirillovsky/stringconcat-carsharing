package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.ActuallyDoesNotExists
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.AlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.NotMaturedEnough
import com.stringconcat.kirillov.carsharing.customer.CustomerStatus.REGISTERED
import com.stringconcat.kirillov.carsharing.customer.CustomerStatus.REJECTED
import com.stringconcat.kirillov.carsharing.customer.CustomerStatus.VERIFIED
import java.time.LocalDate

class Customer internal constructor(
    id: CustomerId,
    val fullName: FullName,
    val birthDate: LocalDate,
    val driverLicenseNumber: DriverLicenseNumber,
) : AggregateRoot<CustomerId>(id) {
    var status: CustomerStatus = REGISTERED
        internal set

    fun reject() {
        if (status == REJECTED) return

        status = REJECTED

        addEvent(CustomerRejected(id))
    }

    fun verify() {
        if (status == VERIFIED) return

        status = VERIFIED

        addEvent(CustomerVerified(id))
    }

    companion object {
        fun registerCustomer(
            idGenerator: CustomerIdGenerator,
            registrationDate: LocalDate,
            customerAlreadyRegistered: CustomerAlreadyRegistered,
            customerActuallyExists: CustomerActuallyExists,
            fullName: FullName,
            birthDate: LocalDate,
            driverLicenseNumber: DriverLicenseNumber,
        ): Either<CustomerRegistrationError, Customer> {
            return when {
                isCustomerMatureEnough(birthDate, registrationDate).not() -> NotMaturedEnough.left()
                customerAlreadyRegistered.check(fullName, birthDate) -> AlreadyRegistered.left()
                customerActuallyExists.check(fullName, birthDate).not() -> ActuallyDoesNotExists.left()
                else -> Customer(
                    id = idGenerator.generate(),
                    fullName = fullName,
                    birthDate = birthDate,
                    driverLicenseNumber = driverLicenseNumber
                ).apply {
                    addEvent(CustomerRegistered(value = id))
                }.right()
            }
        }
    }
}

private const val MATURITY_AGE = 21L

private fun isCustomerMatureEnough(customerBirthDate: LocalDate, registrationDate: LocalDate): Boolean =
    customerBirthDate.plusYears(MATURITY_AGE) < registrationDate

sealed class CustomerRegistrationError : BusinessError {
    object NotMaturedEnough : CustomerRegistrationError()
    object AlreadyRegistered : CustomerRegistrationError()
    object ActuallyDoesNotExists : CustomerRegistrationError()
}

enum class CustomerStatus {
    REGISTERED, REJECTED, VERIFIED
}