package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleRepaired
import com.stringconcat.kirillov.carsharing.maintenance.domain.maintenanceVehicle
import com.stringconcat.kirillov.carsharing.maintenance.domain.randomMaintenanceVehicleId
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RepairVehicleUseCaseTest {
    @Test
    fun `should repair broken on finding vehicle`() {
        val persister = FakeMaintenanceVehiclePersister()
        val brokenVehicleId = randomMaintenanceVehicleId()
        val brokenVehicle = maintenanceVehicle(id = brokenVehicleId, broken = true)
        val useCase = RepairVehicleUseCase(
            persister = persister,
            vehicleExtractor = FakeMaintenanceVehicleExtractor().apply {
                put(brokenVehicleId, brokenVehicle)
            }
        )

        val result = useCase.execute(brokenVehicleId)

        result.shouldBeRight()
        persister[brokenVehicleId].should {
            it.shouldNotBeNull()
            it.broken shouldBe false
            it.popEvents().shouldContainExactly(
                VehicleRepaired(vehicleId = brokenVehicleId)
            )
        }
    }

    @Test
    fun `shouldn't repair vehicle if vehicle wasn't found`() {
        val persister = FakeMaintenanceVehiclePersister()
        val usecase = RepairVehicleUseCase(
            persister = persister,
            vehicleExtractor = FakeMaintenanceVehicleExtractor()
        )

        val notFoundVehicleId = randomMaintenanceVehicleId()
        val result = usecase.execute(id = notFoundVehicleId)

        persister[notFoundVehicleId].shouldBeNull()
        result shouldBeLeft RepairVehicleUseCaseError.VehicleNotFound
    }
}