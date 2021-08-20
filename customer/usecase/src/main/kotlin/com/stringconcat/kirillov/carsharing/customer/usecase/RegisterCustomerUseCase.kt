package com.stringconcat.kirillov.carsharing.customer.usecase

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.customer.domain.Age
import com.stringconcat.kirillov.carsharing.customer.domain.Customer
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerActuallyExists
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerAlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerId
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerIdGenerator
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.ActuallyDoesNotExists
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.AlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.BirthDateMoreThanRegistrationDate
import com.stringconcat.kirillov.carsharing.customer.domain.CustomerRegistrationError.NotMaturedEnough

class RegisterCustomerUseCase(
    private val maturityAge: Age,
    private val idGenerator: CustomerIdGenerator,
    private val customerAlreadyRegistered: CustomerAlreadyRegistered,
    private val customerActuallyExists: CustomerActuallyExists,
    val customerPersister: CustomerPersister
) : RegisterCustomer {
    override fun execute(request: RegisterCustomerRequest): Either<RegisterCustomerUseCaseError, CustomerId> {
        return Customer.registerCustomer(
            idGenerator,
            registrationDate = request.registrationDate,
            birthDate = request.birthDate,
            maturityAge,
            customerAlreadyRegistered,
            customerActuallyExists,
            fullName = request.fullName,
            driverLicenseNumber = request.driverLicenseNumber
        ).map {
            customerPersister.save(it)

            it.id
        }.mapLeft {
            it.toErrorMessage()
        }
    }
}

private fun CustomerRegistrationError.toErrorMessage(): RegisterCustomerUseCaseError =
    when (this) {
        is NotMaturedEnough -> RegisterCustomerUseCaseError.NotMaturedEnough
        is AlreadyRegistered -> RegisterCustomerUseCaseError.AlreadyRegistered
        is ActuallyDoesNotExists -> RegisterCustomerUseCaseError.ActuallyDoesNotExists
        is BirthDateMoreThanRegistrationDate -> RegisterCustomerUseCaseError.BirthDateMoreThanRegistrationDate
    }