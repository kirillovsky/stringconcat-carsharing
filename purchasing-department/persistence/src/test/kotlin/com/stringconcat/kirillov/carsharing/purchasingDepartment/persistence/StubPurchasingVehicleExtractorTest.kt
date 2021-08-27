package com.stringconcat.kirillov.carsharing.purchasingDepartment.persistence

import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.domain.randomPurchasingVehicleId
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import org.junit.jupiter.api.Test

internal class StubPurchasingVehicleExtractorTest {
    private val extractor = StubPurchasingVehicleExtractor()

    @Test
    fun `should always return empty purchasingVehicle list`() {
        extractor.getAll().shouldBeEmpty()
    }

    @Test
    fun `shouldn't find purchasingVehicle always`() {
        extractor.getById(id = randomPurchasingVehicleId()).shouldBeNull()
    }
}