package com.stringconcat.kirillov.carsharing.customer.persistence

import com.stringconcat.kirillov.carsharing.fixtures.customer.domain.fullName
import io.kotest.matchers.nulls.shouldBeNull
import java.time.LocalDate.EPOCH
import org.junit.jupiter.api.Test

internal class StubCustomerExtractorTest {
    @Test
    fun `shouldn't find any customers always`() {
        val extractor = StubCustomerExtractor()

        extractor.getBy(fullName = fullName(), birthDate = EPOCH).shouldBeNull()
    }
}