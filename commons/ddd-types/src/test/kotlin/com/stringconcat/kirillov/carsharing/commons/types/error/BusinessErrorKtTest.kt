package com.stringconcat.kirillov.carsharing.commons.types.error

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BusinessErrorKtTest {
    @Test
    fun `failOn should throw illegal state exception with with business error text`() {
        val expectedErrorText = "TextErrorText"
        val error = object : BusinessError {
            override fun toString() = expectedErrorText
        }
        val exception = assertThrows<IllegalStateException> {
            failOnBusinessError(error)
        }

        exception.message shouldBe expectedErrorText
    }
}