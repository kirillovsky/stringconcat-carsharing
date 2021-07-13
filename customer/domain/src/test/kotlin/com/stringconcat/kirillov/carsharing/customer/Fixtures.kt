package com.stringconcat.kirillov.carsharing.customer

import java.time.LocalDate.EPOCH
import kotlin.random.Random

fun fullName(
    firstName: String = "ВАСИЛИЙ",
    middleName: String? = null,
    secondName: String = "ПУПКИН",
) = FullName(firstName, middleName, secondName)

fun driverLicenseNumber(
    series: String = "77 77",
    number: String = "123456",
) = DriverLicenseNumber(series, number)

fun customerId() = CustomerId(value = Random.nextLong())

fun customer(
    id: CustomerId = customerId(),
    customerStatus: CustomerStatus,
) = Customer(
    id,
    fullName = fullName(),
    birthDate = EPOCH,
    driverLicenseNumber = driverLicenseNumber()
).apply {
    status = customerStatus
}