package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.customer.AgeCreationErrors.BirthDateMoreThanCurrentDate
import com.stringconcat.kirillov.carsharing.customer.AgeCreationErrors.IllegalYearsCount
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Month.JULY
import org.junit.jupiter.api.Test

internal class AgeTest {
    @Test
    fun `age should contains yearsCount`() {
        val expectedYearsCount = 21

        val age = Age.from(yearsCount = expectedYearsCount)

        age shouldBeRight {
            it.yearsCount shouldBe expectedYearsCount
        }
    }

    @Test
    fun `age shouldn't contains negative yearsCount`() {
        val result = Age.from(yearsCount = -10)

        result shouldBeLeft {
            it shouldBe IllegalYearsCount
        }
    }

    @Test
    fun `age should be create from birthDate and currentDate`() {
        val expectedAge = Age.from(yearsCount = 20)

        val age = Age.from(
            birthDate = LocalDate.of(2000, JULY, 20),
            currentDate = LocalDate.of(2021, JULY, 19)
        )

        age shouldBe expectedAge
    }

    @Test
    fun `age shouldn't be create if birthDate more than currentDate`() {
        val result = Age.from(
            birthDate = LocalDate.of(2021, JULY, 20),
            currentDate = LocalDate.of(2021, JULY, 19)
        )

        result shouldBeLeft {
            it shouldBe BirthDateMoreThanCurrentDate
        }
    }

    @Test
    fun `age should be compared to another date`() {
        val age = 12.asYearsAge()

        age shouldBeEqualComparingTo 12.asYearsAge()
        age shouldBeGreaterThan 10.asYearsAge()
        age shouldBeLessThan 14.asYearsAge()
    }
}