package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.customer.RegisterCustomerRequest.DriverLicenseNumberData
import com.stringconcat.kirillov.carsharing.customer.RegisterCustomerRequest.FullNameData
import com.stringconcat.kirillov.carsharing.customer.RegisterCustomerRequest.InvalidRegisterCustomerParameters
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Month.AUGUST
import org.junit.jupiter.api.Test

internal class RegisterCustomerRequestTest {
    private val registrationDate = LocalDate.now()
    private val birthDate = LocalDate.of(1992, AUGUST, 1)
    private val fullNameData = fullName().run { FullNameData(firstName, middleName, secondName) }
    private val driverLicenseNumberData = driverLicenseNumber().run { DriverLicenseNumberData(series, number) }

    @Test
    fun `request should contains params for further register customer`() {
        val fullName = fullName()
        val driverLicenseNumber = driverLicenseNumber()

        val request = RegisterCustomerRequest.from(
            registrationDate,
            birthDate,
            fullNameData = FullNameData(
                firstName = fullName.firstName,
                middleName = fullName.middleName,
                secondName = fullName.secondName
            ),
            driverLicenseNumberData = DriverLicenseNumberData(
                series = driverLicenseNumber.series,
                number = driverLicenseNumber.number

            )
        )

        request shouldBeRight RegisterCustomerRequest(registrationDate, birthDate, fullName, driverLicenseNumber)
    }

    @Test
    fun `shouldn't create request with invalid full name`() {
        val result = RegisterCustomerRequest.from(
            registrationDate,
            birthDate,
            fullNameData = FullNameData(
                firstName = "Invalid first name",
                middleName = "Invalid middle name",
                secondName = "Invalid second name"
            ),
            driverLicenseNumberData
        )

        result shouldBeLeft InvalidRegisterCustomerParameters("invalid full name")
    }

    @Test
    fun `shouldn't create request with invalid driver license number`() {
        val result = RegisterCustomerRequest.from(
            registrationDate,
            birthDate,
            fullNameData,
            driverLicenseNumberData = DriverLicenseNumberData(
                series = "Invalid series",
                number = "Invalid number"
            )
        )

        result shouldBeLeft InvalidRegisterCustomerParameters("invalid driver license number")
    }

    @Test
    fun `error message should be equals by message text`() {
        InvalidRegisterCustomerParameters("test") shouldBe InvalidRegisterCustomerParameters("test")
    }
}