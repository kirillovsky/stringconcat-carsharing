package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.ActuallyDoesNotExists
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.AlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.BirthDateMoreThanRegistrationDate
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.NotMaturedEnough
import java.time.LocalDate

class Customer internal constructor(
    id: CustomerId,
    val fullName: FullName,
    val birthDate: LocalDate,
    val driverLicenseNumber: DriverLicenseNumber,
) : AggregateRoot<CustomerId>(id) {
    var isRejected: Boolean = false
        internal set

    fun reject() {
        if (isRejected) return

        isRejected = true

        addEvent(CustomerRejected(id))
    }

    companion object {
        fun registerCustomer(
            idGenerator: CustomerIdGenerator,
            registrationDate: LocalDate,
            birthDate: LocalDate,
            maturityAge: Age,
            customerAlreadyRegistered: CustomerAlreadyRegistered,
            customerActuallyExists: CustomerActuallyExists,
            fullName: FullName,
            driverLicenseNumber: DriverLicenseNumber,
        ): Either<CustomerRegistrationError, Customer> {
            val age = Age.from(birthDate, registrationDate)

            return when {
                birthDate > registrationDate -> BirthDateMoreThanRegistrationDate.left()
                age.exists { it < maturityAge } -> NotMaturedEnough.left()
                customerAlreadyRegistered.check(fullName, birthDate) -> AlreadyRegistered.left()
                customerActuallyExists.check(fullName, birthDate).not() -> ActuallyDoesNotExists.left()
                else -> Customer(
                    id = idGenerator.generate(),
                    fullName = fullName,
                    birthDate = birthDate,
                    driverLicenseNumber = driverLicenseNumber
                ).apply {
                    addEvent(CustomerRegistered(customerId = id))
                }.right()
            }
        }
    }
}

sealed class CustomerRegistrationError : BusinessError {
    object NotMaturedEnough : CustomerRegistrationError()
    object AlreadyRegistered : CustomerRegistrationError()
    object ActuallyDoesNotExists : CustomerRegistrationError()
    object BirthDateMoreThanRegistrationDate : CustomerRegistrationError()
}