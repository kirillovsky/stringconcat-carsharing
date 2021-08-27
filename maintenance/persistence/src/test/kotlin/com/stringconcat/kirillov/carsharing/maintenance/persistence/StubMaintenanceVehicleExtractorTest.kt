package com.stringconcat.kirillov.carsharing.maintenance.persistence

import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.randomMaintenanceVehicleId
import io.kotest.matchers.nulls.shouldBeNull
import org.junit.jupiter.api.Test

internal class StubMaintenanceVehicleExtractorTest {
    @Test
    fun `shouldn't find any maintenance vehicle always`() {
        val extractor = StubMaintenanceVehicleExtractor()

        extractor.getById(id = randomMaintenanceVehicleId()).shouldBeNull()
    }
}