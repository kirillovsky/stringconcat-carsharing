package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleAddedToMaintenanceInventory
import com.stringconcat.kirillov.carsharing.maintenance.domain.maintenanceVehicle
import com.stringconcat.kirillov.carsharing.maintenance.domain.randomMaintenanceVehicleId
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AddVehicleToInventoryHandlerTest {
    @Test
    fun `should add vehicle to inventory`() {
        val vehicleId = randomMaintenanceVehicleId()
        val model = randomVehicleModel()
        val vin = vin()
        val coveredMileage = randomDistance()
        val registrationPlate = registrationPlate()
        val persister = FakeMaintenanceVehiclePersister()

        val id = AddVehicleToInventoryHandler(
            vehiclePersister = persister,
            vehicleExtractor = FakeMaintenanceVehicleExtractor()
        ).execute(
            AddVehicleToInventoryRequest(
                id = vehicleId,
                model = model,
                vin = vin,
                coveredMileage = coveredMileage,
                registrationPlate = registrationPlate
            )
        )

        id shouldBe vehicleId
        persister[vehicleId] should {
            it.shouldNotBeNull()
            it.id shouldBe vehicleId
            it.vin shouldBe vin
            it.model shouldBe model
            it.coveredMileage shouldBe coveredMileage
            it.registrationPlate shouldBe registrationPlate
            it.broken shouldBe false
            it.popEvents().shouldContainExactly(VehicleAddedToMaintenanceInventory(vehicleId))
        }
    }

    @Test
    fun `shouldn't create vehicle if it already exists`() {
        val persister = FakeMaintenanceVehiclePersister()
        val existingVehicle = maintenanceVehicle()

        val id = AddVehicleToInventoryHandler(
            vehiclePersister = persister,
            vehicleExtractor = FakeMaintenanceVehicleExtractor().apply {
                put(existingVehicle.id, existingVehicle)
            }
        ).execute(
            request = AddVehicleToInventoryRequest(
                id = existingVehicle.id,
                model = existingVehicle.model,
                vin = existingVehicle.vin,
                coveredMileage = existingVehicle.coveredMileage,
                registrationPlate = existingVehicle.registrationPlate
            )
        )

        id shouldBe existingVehicle.id
        persister.shouldBeEmpty()
    }
}