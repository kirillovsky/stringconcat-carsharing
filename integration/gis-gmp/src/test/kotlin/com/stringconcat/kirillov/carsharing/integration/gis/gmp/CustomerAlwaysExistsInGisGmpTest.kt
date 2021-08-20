package com.stringconcat.kirillov.carsharing.integration.gis.gmp

import com.stringconcat.kirillov.carsharing.customer.domain.fullName
import io.kotest.matchers.shouldBe
import java.time.LocalDate.MIN
import org.junit.jupiter.api.Test

internal class CustomerAlwaysExistsInGisGmpTest {
    @Test
    fun `should return true for any fullName and birthDate`() {
        val result = CustomerAlwaysExistsInGisGmp.check(fullName = fullName(), birthDate = MIN)

        result shouldBe true
    }
}