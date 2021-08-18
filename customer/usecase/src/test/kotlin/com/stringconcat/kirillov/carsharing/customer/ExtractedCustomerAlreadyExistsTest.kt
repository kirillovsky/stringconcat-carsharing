package com.stringconcat.kirillov.carsharing.customer

import io.kotest.matchers.shouldBe
import java.time.LocalDate
import org.junit.jupiter.api.Test

internal class ExtractedCustomerAlreadyExistsTest {
    @Test
    fun `extracted customer shouldn't exists if extractor doesn't have customer with same fullName and birthDate`() {
        val notFoundFullName = fullName()
        val notFoundBirthDate = LocalDate.now()

        val result = ExtractedCustomerAlreadyExists(
            extractor = FakeCustomerExtractor()
        ).check(fullName = notFoundFullName, birthDate = notFoundBirthDate)

        result shouldBe false
    }

    @Test
    fun `extracted customer should exists if extractor have customer with same fullName and birthDate`() {
        val customer = customer()

        val result = ExtractedCustomerAlreadyExists(
            extractor = FakeCustomerExtractor(customer)
        ).check(fullName = customer.fullName, birthDate = customer.birthDate)

        result shouldBe true
    }
}