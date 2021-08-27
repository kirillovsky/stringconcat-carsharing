package com.stringconcat.kirillov.carsharing.ride.domain

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.toPrice
import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.finishedRide
import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.startedRide
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class FixedRateTaximeterTest {
    private val randomRateTaximeter = FixedRateTaximeter(ratePerDistance = randomPrice())

    @Test
    fun `fixed rate taximeter should calculate ride price via ride distance and rate price`() {
        val taximeter = FixedRateTaximeter(ratePerDistance = 11.21.toPrice())
        val ride = finishedRide(coveredDistance = 5.2.toKilometers())

        val resultPrice = taximeter.calculatePrice(ride)

        resultPrice shouldBeRight {
            it shouldBe (11.21 * 5.2).toPrice()
        }
    }

    @Test
    fun `fixed rate taximeter shouldn't calculate price for not finished price`() {
        val ride = startedRide()

        val resultPrice = randomRateTaximeter.calculatePrice(ride)

        resultPrice shouldBeLeft {
            it shouldBe CalculationRidePriceError
        }
    }
}