package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import com.github.javafaker.Faker
import java.time.LocalDate.EPOCH
import java.util.Locale

private val faker = Faker(Locale.forLanguageTag("ru"))

fun fullName(
    firstName: String = faker.name().firstName(),
    middleName: String? = null,
    secondName: String = faker.name().lastName(),
) = FullName(firstName, middleName, secondName)

fun driverLicenseNumber(
    series: String = faker.numerify("3# ##"),
    number: String = faker.numerify("1#####"),
) = DriverLicenseNumber(series, number)

fun customerId() = CustomerId(value = faker.number().randomNumber())

fun customer(
    id: CustomerId = customerId(),
    rejected: Boolean = false,
) = Customer(
    id,
    fullName = fullName(),
    birthDate = EPOCH,
    driverLicenseNumber = driverLicenseNumber()
).apply {
    isRejected = rejected
}

fun Int.asYearsAge(): Age {
    val result = Age.from(yearsCount = this)

    check(result is Either.Right)

    return result.b
}