package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toPrice
import com.stringconcat.kirillov.carsharing.ride.RideStatus.FINISHED
import com.stringconcat.kirillov.carsharing.ride.RideStatus.STARTED
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class FixedRateTaximeterTest {
    private val randomRateTaximeter = FixedRateTaximeter(ratePerDistance = randomPrice())

    @Test
    fun `fixed rate taximeter should calculate ride price via ride distance and rate price`() {
        val taximeter = FixedRateTaximeter(ratePerDistance = 11.21.toPrice())
        val ride = ride(status = FINISHED, coveredDistance = 5.2.toKilometers())

        val resultPrice = taximeter.calculatePrice(ride)

        resultPrice shouldBeRight {
            it shouldBe (11.21 * 5.2).toPrice()
        }
    }

    @Test
    fun `fixed rate taximeter shouldn't calculate price for not finished price`() {
        val ride = ride(status = STARTED)

        val resultPrice = randomRateTaximeter.calculatePrice(ride)

        resultPrice shouldBeLeft {
            it shouldBe CalculationRidePriceError
        }
    }

    @Test
    fun `fixed rate taximeter should throw exception if ride finished but not nave covered distance`() {
        val illegalRide = ride(status = FINISHED, coveredDistance = null)

        val resultPrice = randomRateTaximeter.calculatePrice(illegalRide)

        resultPrice shouldBeLeft {
            it shouldBe CalculationRidePriceError
        }
    }
}