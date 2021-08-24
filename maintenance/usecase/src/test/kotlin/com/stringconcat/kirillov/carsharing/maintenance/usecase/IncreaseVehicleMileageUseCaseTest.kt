package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.maintenanceVehicle
import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleCoveredMileageIncreased
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class IncreaseVehicleMileageUseCaseTest {
    @Test
    fun `should increase mileage on finding vehicle`() {
        val existingVehicleId = randomMaintenanceVehicleId()
        val initialCoveredDistance = 100_000.0.toKilometers()
        val vehicle = maintenanceVehicle(id = existingVehicleId, coveredMileage = initialCoveredDistance)
        val persister = InMemoryMaintenanceVehicleRepository()
        val usecase = IncreaseVehicleMileageUseCase(
            persister = persister,
            vehicleExtractor = InMemoryMaintenanceVehicleRepository().apply {
                put(existingVehicleId, vehicle)
            }
        )
        val addendum = 500.0.toKilometers()

        val result = usecase.execute(existingVehicleId, addendum)

        result.shouldBeRight()
        persister[existingVehicleId] should {
            it.shouldNotBeNull()
            it.coveredMileage shouldBe (initialCoveredDistance + addendum)
            it.popEvents().shouldContainExactly(
                VehicleCoveredMileageIncreased(vehicleId = existingVehicleId, added = addendum)
            )
        }
    }

    @Test
    fun `shouldn't increase mileage if vehicle not found`() {
        val repo = InMemoryMaintenanceVehicleRepository()
        val useCase = IncreaseVehicleMileageUseCase(
            persister = repo,
            vehicleExtractor = repo
        )
        val notFoundVehicleId = randomMaintenanceVehicleId()

        val result = useCase.execute(id = notFoundVehicleId, addendum = randomDistance())

        result shouldBeLeft IncreaseVehicleMileageUseCaseError.VehicleNotFound
        repo[notFoundVehicleId].shouldBeNull()
    }
}