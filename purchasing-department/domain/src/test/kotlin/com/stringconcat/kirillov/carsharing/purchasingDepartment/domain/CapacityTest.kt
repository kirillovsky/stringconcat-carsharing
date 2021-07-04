package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.Capacity.IllegalCapacityValueError
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CapacityTest {
    @Test
    fun `capacity should have value property`() {
        val expectedValue = 5
        val capacity = Capacity.from(expectedValue)

        capacity shouldBeRight {
            it.value shouldBe expectedValue
        }
    }

    @Test
    fun `capacity shouldn't have negative value`() {
        val negativeCapacity = Capacity.from(-2)

        negativeCapacity shouldBeLeft {
            it shouldBe IllegalCapacityValueError
        }
    }

    @Test
    fun `capacity shouldn't have zero value`() {
        val zeroCapacity = Capacity.from(0)

        zeroCapacity shouldBeLeft {
            it shouldBe IllegalCapacityValueError
        }
    }

    @Test
    fun `five should create capacity with value equals to 5`() {
        val capacity = Capacity.five()

        capacity.value shouldBe 5
    }
}