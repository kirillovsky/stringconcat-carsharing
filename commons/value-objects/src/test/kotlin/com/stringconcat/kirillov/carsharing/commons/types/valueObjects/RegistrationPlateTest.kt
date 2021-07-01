package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate.InvalidNumberFormat
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate.InvalidRegionCodeFormat
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate.InvalidSeriesFormat
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

internal class RegistrationPlateTest {
    private val validSeries = "МММ"
    private val validNumber = "001"
    private val validRegistrationCode = "01"

    @Test
    fun `registration plate should contains series, number and regionCode`() {
        val plate = RegistrationPlate.from(
            series = validSeries,
            number = validNumber,
            regionCode = validRegistrationCode
        )

        plate.shouldBeSuccess {
            it?.series shouldBe validSeries
            it?.number shouldBe validNumber
            it?.regionCode shouldBe validRegistrationCode
        }
    }

    @Test
    fun `registration plates number shouldn't contains non-digit symbols`() {
        val nonDigitsNumberPlate = RegistrationPlate.from(
            validSeries,
            number = "1Е2",
            validRegistrationCode
        )

        nonDigitsNumberPlate.shouldBeFailure {
            it shouldBe InvalidNumberFormat
        }
    }

    @Test
    fun `registration plates number shouldn't contains more of less than 3 digits`() {
        val twoDigitsNumberPlate = RegistrationPlate.from(
            validSeries,
            number = "12",
            validRegistrationCode
        )
        val fourDigitsNumberPlate = RegistrationPlate.from(
            validSeries,
            number = "1234",
            validRegistrationCode
        )

        twoDigitsNumberPlate.shouldBeFailure {
            it shouldBe InvalidNumberFormat
        }
        fourDigitsNumberPlate.shouldBeFailure {
            it shouldBe InvalidNumberFormat
        }
    }

    @Test
    fun `registration plates number shouldn't be 000`() {
        val zeroNumberPlate = RegistrationPlate.from(
            validSeries,
            number = "000",
            validRegistrationCode
        )

        zeroNumberPlate.shouldBeFailure {
            it shouldBe InvalidNumberFormat
        }
    }

    @Test
    fun `registration plate should be case agnostic by serial`() {
        val onePlate = RegistrationPlate.from(
            series = validSeries.uppercase(),
            validNumber,
            validRegistrationCode
        )
        val anotherPlate = RegistrationPlate.from(
            series = validSeries.lowercase(),
            validNumber,
            validRegistrationCode
        )

        onePlate.getOrThrow() shouldBe anotherPlate.getOrThrow()
    }

    @Test
    fun `registration plate series shouldn't contains non-russian letters chars`() {
        val invalidSeriesPlate = RegistrationPlate.from(
            series = "1#D",
            validNumber,
            validRegistrationCode
        )

        invalidSeriesPlate.shouldBeFailure {
            it shouldBe InvalidSeriesFormat
        }
    }

    @Test
    fun `registration plates series shouldn't contains more of less than 3 russian letters`() {
        val twoLettersSeriesPlate = RegistrationPlate.from(
            series = "ММ",
            validNumber,
            validRegistrationCode
        )
        val fourDigitsNumberPlate = RegistrationPlate.from(
            series = "ММММ",
            validNumber,
            validRegistrationCode
        )

        twoLettersSeriesPlate.shouldBeFailure {
            it shouldBe InvalidSeriesFormat
        }
        fourDigitsNumberPlate.shouldBeFailure {
            it shouldBe InvalidSeriesFormat
        }
    }

    @TestFactory
    fun `registration plate series shouldn't contains illegal russian letters`(): List<DynamicTest> {
        val legalLetters = listOf('А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х')
        val illegalLetters = CharRange('А', 'Я') - legalLetters

        return illegalLetters.map { illegalLetter ->
            dynamicTest("registration plate series shouldn't contains illegal russian letter - $illegalLetter") {
                val legalSeriesPlate = RegistrationPlate.from(
                    series = "М${illegalLetter}У",
                    validNumber,
                    validRegistrationCode
                )

                legalSeriesPlate.shouldBeFailure {
                    it shouldBe InvalidSeriesFormat
                }
            }
        }
    }

    @Test
    fun `registration plate regionCode shouldn't contains non-digits`() {
        val invalidPlate = RegistrationPlate.from(
            validSeries,
            validNumber,
            regionCode = "D1 "
        )

        invalidPlate.shouldBeFailure {
            it shouldBe InvalidRegionCodeFormat
        }
    }

    @Test
    fun `registration plate regionCode should contains 2 or 3 digits`() {
        val oneDigitRegionCodePlate = RegistrationPlate.from(
            validSeries,
            validNumber,
            regionCode = "1"
        )
        val fourDigitRegionCodePlate = RegistrationPlate.from(
            validSeries,
            validNumber,
            regionCode = "1234"
        )

        oneDigitRegionCodePlate.shouldBeFailure {
            it shouldBe InvalidRegionCodeFormat
        }
        fourDigitRegionCodePlate.shouldBeFailure {
            it shouldBe InvalidRegionCodeFormat
        }
    }

    @Test
    fun `registration plate 2 digits regionCode shouldn't be 00`() {
        val illegalPlate = RegistrationPlate.from(
            validSeries,
            validNumber,
            regionCode = "00"
        )

        illegalPlate.shouldBeFailure {
            it shouldBe InvalidRegionCodeFormat
        }
    }

    @Test
    fun `registration plate 3 digits regionCode shouldn't start with 0`() {
        val illegalPlate = RegistrationPlate.from(
            validSeries,
            validNumber,
            regionCode = "091"
        )

        illegalPlate.shouldBeFailure {
            it shouldBe InvalidRegionCodeFormat
        }
    }
}