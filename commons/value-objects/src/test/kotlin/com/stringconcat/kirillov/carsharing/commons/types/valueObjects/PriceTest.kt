package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PriceTest {
    @Test
    fun `price should contains value`() {
        val expectedValue = "1040.00".toBigDecimal()

        val price = Price.from(value = expectedValue)

        price shouldBeRight {
            it.value shouldBe expectedValue
        }
    }

    @Test
    fun `shouldn't create price with negative value`() {
        val result = Price.from(value = (-0.05).toBigDecimal())

        result shouldBeLeft {
            it shouldBe NegativePriceValueError
        }
    }

    @Test
    fun `price should contains value ceiling to 2 decimal places`() {
        val price = Price.from(value = 1000.663.toBigDecimal())

        price shouldBeRight {
            it.value shouldBe 1000.67.toBigDecimal()
        }
    }

    @Test
    fun `price should be equals to another with same value`() {
        Price(value = 0.01.toBigDecimal()) shouldBe Price(value = "0.01".toBigDecimal())
    }

    @Test
    fun `price should be multiplied to big decimal value`() {
        val price = Price(value = 10.33.toBigDecimal()) * 0.333.toBigDecimal()

        price shouldBeRight {
            3.44.toPrice()
        }
    }

    @Test
    fun `price shouldn't be multiplied to negative value`() {
        val result = randomPrice() * (-19.2).toBigDecimal()

        result shouldBeLeft NegativePriceValueError
    }
}