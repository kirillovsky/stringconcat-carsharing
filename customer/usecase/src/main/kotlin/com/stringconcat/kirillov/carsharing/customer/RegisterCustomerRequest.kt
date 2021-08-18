package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.customer.RegisterCustomerRequest.InvalidRegisterCustomerParameters
import java.time.LocalDate

data class RegisterCustomerRequest(
    val registrationDate: LocalDate,
    val birthDate: LocalDate,
    val fullName: FullName,
    val driverLicenseNumber: DriverLicenseNumber
) {
    companion object {
        fun from(
            registrationDate: LocalDate,
            birthDate: LocalDate,
            fullNameData: FullNameData,
            driverLicenseNumberData: DriverLicenseNumberData
        ): Either<InvalidRegisterCustomerParameters, RegisterCustomerRequest> =
            tupled(
                fullNameData.run {
                    FullName.from(firstName, middleName, secondName)
                },
                driverLicenseNumberData.run {
                    DriverLicenseNumber.from(series, number)
                }
            ).map { (fullName, driverLicenseNumber) ->
                RegisterCustomerRequest(registrationDate, birthDate, fullName, driverLicenseNumber)
            }.mapLeft {
                it.toErrorMessage()
            }
    }

    class DriverLicenseNumberData(val series: String, val number: String)
    class FullNameData(val firstName: String, val middleName: String?, val secondName: String)

    data class InvalidRegisterCustomerParameters(val message: String)
}

private fun BusinessError.toErrorMessage(): InvalidRegisterCustomerParameters =
    when (this) {
        is CreateFullNameError -> InvalidRegisterCustomerParameters("invalid full name")
        is DriverLicenseNumberError -> InvalidRegisterCustomerParameters("invalid driver license number")
        else -> error("Unable to map $this to error message")
    }