package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance.NegativeDistanceValueError
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.toKilometers
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DistanceTest {
    @Test
    fun `should create distance with value in kilometers`() {
        val expectedValueInKilometers = 1040.0.toBigDecimal()

        val distance = Distance.ofKilometers(value = expectedValueInKilometers)

        distance.shouldBeRight().kilometers shouldBe expectedValueInKilometers
    }

    @Test
    fun `should ceil passed value to one decimal places`() {
        val distance = Distance.ofKilometers(value = 1040.356.toBigDecimal())

        distance.shouldBeRight().kilometers shouldBe 1040.4.toBigDecimal()
    }

    @Test
    fun `shouldn't create distance with negative value`() {
        val distance = Distance.ofKilometers(value = (-12.31).toBigDecimal())

        distance.shouldBeLeft(NegativeDistanceValueError)
    }

    @Test
    fun `distance should be added with another distance`() {
        val distance1 = 5.1.toKilometers()
        val distance2 = 3.4.toKilometers()

        distance1 + distance2 should {
            it.kilometers shouldBe 8.5.toBigDecimal()
        }
    }
}