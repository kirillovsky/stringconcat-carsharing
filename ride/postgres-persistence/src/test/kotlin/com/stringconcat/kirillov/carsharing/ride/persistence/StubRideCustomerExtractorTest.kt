package com.stringconcat.kirillov.carsharing.ride.persistence

import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.randomRideCustomerId
import io.kotest.matchers.nulls.shouldBeNull
import org.junit.jupiter.api.Test

internal class StubRideCustomerExtractorTest {
    @Test
    fun `shouldn't find any ride customers always`() {
        val extractor = StubRideCustomerExtractor()

        extractor.getById(id = randomRideCustomerId()).shouldBeNull()
    }
}