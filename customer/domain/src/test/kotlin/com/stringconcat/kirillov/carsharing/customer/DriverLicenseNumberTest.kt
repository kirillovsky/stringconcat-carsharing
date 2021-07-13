package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.customer.DriverLicenseNumberError.IllegalNumber
import com.stringconcat.kirillov.carsharing.customer.DriverLicenseNumberError.IllegalSeries
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DriverLicenseNumberTest {
    private val correctSeries = "77 77"
    private val correctNumber = "123456"

    @Test
    fun `driver license number should contains series and number`() {
        val driverLicenseNumber = DriverLicenseNumber.from(series = correctSeries, number = correctNumber)

        driverLicenseNumber shouldBeRight {
            it.series shouldBe correctSeries
            it.number shouldBe correctNumber
        }
    }

    @Test
    fun `driver license number should contains space separator between two parts in series`() {
        val driverLicenseNumber = DriverLicenseNumber.from(series = "7777", number = correctNumber)

        driverLicenseNumber shouldBeLeft {
            it shouldBe IllegalSeries
        }
    }

    @Test
    fun `driver license number shouldn't contains non-digit symbol in series except space separator`() {
        val driverLicenseNumber = DriverLicenseNumber.from(series = "A1 #,", number = correctNumber)

        driverLicenseNumber shouldBeLeft {
            it shouldBe IllegalSeries
        }
    }

    @Test
    fun `driver license number shouldn't contains 00 as first series part`() {
        val driverLicenseNumber = DriverLicenseNumber.from(series = "00 78", number = correctNumber)

        driverLicenseNumber shouldBeLeft {
            it shouldBe IllegalSeries
        }
    }
    
    @Test
    fun `driver license number shouldn't contains non-digits in number`() {
        val driverLicenseNumber = DriverLicenseNumber.from(series = correctSeries, number = "D2!456")

        driverLicenseNumber shouldBeLeft {
            it shouldBe IllegalNumber
        }
    }

    @Test
    fun `driver license number should contains only 6 digits in number`() {
        val driverLicenseNumber = DriverLicenseNumber.from(series = correctSeries, number = "0123456")

        driverLicenseNumber shouldBeLeft {
            it shouldBe IllegalNumber
        }
    }

    @Test
    fun `driver license number shouldn't contains 000000 as number`() {
        val driverLicenseNumber = DriverLicenseNumber.from(series = correctSeries, number = "000000")

        driverLicenseNumber shouldBeLeft {
            it shouldBe IllegalNumber
        }
    }
}