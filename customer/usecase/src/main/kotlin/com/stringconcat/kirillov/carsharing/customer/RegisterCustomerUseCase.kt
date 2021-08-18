package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.ActuallyDoesNotExists
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.AlreadyRegistered
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.BirthDateMoreThanRegistrationDate
import com.stringconcat.kirillov.carsharing.customer.CustomerRegistrationError.NotMaturedEnough

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