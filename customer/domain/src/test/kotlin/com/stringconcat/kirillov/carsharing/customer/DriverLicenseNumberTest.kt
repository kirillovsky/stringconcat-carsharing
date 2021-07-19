package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.customer.DriverLicenseNumberError.IllegalNumber
import com.stringconcat.kirillov.carsharing.customer.DriverLicenseNumberError.IllegalSeries
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

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

    @TestFactory
    fun `driver license number shouldn't contains illegal series`(): List<DynamicTest> {
        val illegalSeriesCases = mapOf(
            "series without space separator" to "7777",
            "series without more than one separators" to "77  77",
            "series without more than one separators" to "7 77 7",
            "series with non-digits symbols" to "A1 11",
            "series with non-digits symbols" to "1! 11",
            "series with non-digits symbols" to "12 ,1",
            "00 as first series part" to "00 78",
        )

        return illegalSeriesCases.map { (caseName, illegalSeries) ->
            dynamicTest("driver license number shouldn't contains $caseName - '$illegalSeries'") {
                val driverLicenseNumber = DriverLicenseNumber.from(series = illegalSeries, number = correctNumber)

                driverLicenseNumber shouldBeLeft {
                    it shouldBe IllegalSeries
                }
            }
        }
    }

    @TestFactory
    fun `driver license number shouldn't contains illegal number`(): List<DynamicTest> {
        val illegalNumberCases = mapOf(
            "non-digits symbols" to "D23456",
            "non-digits symbols" to "!23456",
            "less or more 6 digits" to "01234",
            "less or more 6 digits" to "0123456",
            "only zero digits" to "000000"
        )

        return illegalNumberCases.map { (caseName, illegalNumber) ->
            dynamicTest("driver license number shouldn't contains $caseName - '$illegalNumber'") {
                val driverLicenseNumber = DriverLicenseNumber.from(series = correctSeries, number = illegalNumber)

                driverLicenseNumber shouldBeLeft {
                    it shouldBe IllegalNumber
                }
            }
        }
    }
}