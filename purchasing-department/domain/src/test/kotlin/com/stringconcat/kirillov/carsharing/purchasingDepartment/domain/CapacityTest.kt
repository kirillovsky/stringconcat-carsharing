package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.Capacity.IllegalCapacityValueError
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class CapacityTest {
    @Test
    fun `capacity should have value property`() {
        val expectedValue = 5
        val capacity = Capacity.from(expectedValue)

        capacity shouldBeRight {
            it.value shouldBe expectedValue
        }
    }

    @ParameterizedTest(name = "capacity shouldn''t be less or equals to zero - ''{0}''")
    @ValueSource(ints = [-1, -3, 0])
    fun `capacity shouldn't be less or equals to zero`(illegalValue: Int) {
        val negativeCapacity = Capacity.from(illegalValue)

        negativeCapacity shouldBeLeft {
            it shouldBe IllegalCapacityValueError
        }
    }

    @Test
    fun `five should create capacity with value equals to 5`() {
        val capacity = Capacity.five()

        capacity.value shouldBe 5
    }
}