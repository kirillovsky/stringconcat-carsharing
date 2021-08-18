package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either

interface RegisterCustomer {
    fun execute(request: RegisterCustomerRequest): Either<RegisterCustomerUseCaseError, CustomerId>
}

sealed class RegisterCustomerUseCaseError(val message: String) {
    object NotMaturedEnough : RegisterCustomerUseCaseError("customer not matured enough")
    object AlreadyRegistered : RegisterCustomerUseCaseError("customer already registered")
    object ActuallyDoesNotExists : RegisterCustomerUseCaseError("customer actually does not exists")
    object BirthDateMoreThanRegistrationDate : RegisterCustomerUseCaseError(
        message = "customer birth date more than registration date"
    )
}