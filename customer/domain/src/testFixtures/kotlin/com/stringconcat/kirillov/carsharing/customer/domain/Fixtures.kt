package com.stringconcat.kirillov.carsharing.customer.domain

import arrow.core.getOrHandle
import com.github.javafaker.Faker
import com.stringconcat.kirillov.carsharing.commons.types.error.failOnBusinessError
import java.time.LocalDate.now
import java.util.Locale

private val faker = Faker(Locale.forLanguageTag("ru"))

fun fullName(
    firstName: String = faker.name().firstName(),
    middleName: String? = null,
    secondName: String = faker.name().lastName(),
) = FullName
    .from(firstName, middleName, secondName)
    .getOrHandle(::failOnBusinessError)

fun driverLicenseNumber(
    series: String = faker.numerify("3# ##"),
    number: String = faker.numerify("1#####"),
) = DriverLicenseNumber
    .from(series, number)
    .getOrHandle(::failOnBusinessError)

fun randomCustomerId() = CustomerId(value = faker.number().randomNumber())

fun customer(
    id: CustomerId = randomCustomerId(),
    rejected: Boolean = false,
): com.stringconcat.kirillov.carsharing.customer.domain.Customer {
    val age = randomAge()

    return com.stringconcat.kirillov.carsharing.customer.domain.Customer.registerCustomer(
        idGenerator = { id },
        fullName = fullName(),
        birthDate = now().minusYears(age.yearsCount.toLong()),
        driverLicenseNumber = driverLicenseNumber(),
        customerActuallyExists = { _, _ -> true },
        registrationDate = now(),
        maturityAge = age,
        customerAlreadyRegistered = { _, _ -> false }
    ).getOrHandle(::failOnBusinessError)
        .apply {
            if (rejected) reject()
            popEvents()
        }
}

fun randomAge(minAge: Int = 18, maxAge: Int = 100) = faker.number().numberBetween(maxAge, minAge).asYearsAge()

fun Int.asYearsAge() = com.stringconcat.kirillov.carsharing.customer.domain.Age
    .from(yearsCount = this)
    .getOrHandle(::failOnBusinessError)