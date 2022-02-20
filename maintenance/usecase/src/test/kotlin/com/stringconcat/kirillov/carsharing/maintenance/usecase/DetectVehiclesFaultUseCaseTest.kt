package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.maintenanceVehicle
import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DetectVehiclesFaultUseCaseTest {
    @Test
    fun `should detect fault on finding maintenance vehicle`() {
        val existingMaintenanceVehicleId = randomMaintenanceVehicleId()
        val maintenanceVehicle = maintenanceVehicle(id = existingMaintenanceVehicleId, broken = false)
        val persister = InMemoryMaintenanceVehicleRepository()
        val usecase = DetectVehiclesFaultUseCase(
            persister = persister,
            vehicleExtractor = InMemoryMaintenanceVehicleRepository().apply {
                put(existingMaintenanceVehicleId, maintenanceVehicle)
            }
        )

        val result = usecase.execute(existingMaintenanceVehicleId)

        result.shouldBeRight()
        persister[existingMaintenanceVehicleId] should {
            it.shouldNotBeNull()
            it.broken shouldBe true
            it.popEvents().shouldContainExactly(
                MaintenanceVehicleEvents.VehicleBroken(vehicleId = existingMaintenanceVehicleId)
            )
        }
    }

    @Test
    fun `shouldn't detect fault if vehicle wasn't found`() {
        val repo = InMemoryMaintenanceVehicleRepository()
        val usecase = DetectVehiclesFaultUseCase(
            persister = repo,
            vehicleExtractor = repo
        )
        val notFoundVehicleId = randomMaintenanceVehicleId()

        val result = usecase.execute(id = notFoundVehicleId)

        repo[notFoundVehicleId].shouldBeNull()
        result shouldBeLeft DetectVehiclesFaultUseCaseError.VehicleNotFound
    }
}