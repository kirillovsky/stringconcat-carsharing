package com.stringconcat.kirillov.carsharing.maintenance.domain

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.maintenanceVehicle
import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleAddedToMaintenanceInventory
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleBroken
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleCoveredMileageIncreased
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleRepaired
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MaintenanceVehicleTest {
    @Test
    fun `should add vehicle to inventory`() {
        val expectedId = randomMaintenanceVehicleId()
        val expectedModel = randomVehicleModel()
        val expectedVin = vin()
        val expectedMileage = 1.0.toKilometers()
        val expectedRegistrationPlate = registrationPlate()

        val vehicle = MaintenanceVehicle.addVehicleToInventory(
            id = expectedId,
            model = expectedModel,
            vin = expectedVin,
            registrationPlate = expectedRegistrationPlate,
            coveredMileage = expectedMileage
        )

        vehicle should {
            it.id shouldBe expectedId
            it.model shouldBe expectedModel
            it.vin shouldBe expectedVin
            it.coveredMileage shouldBe expectedMileage
            it.broken shouldBe false
            it.registrationPlate shouldBe expectedRegistrationPlate
            it.popEvents().shouldContainExactly(
                VehicleAddedToMaintenanceInventory(vehicleId = expectedId)
            )
        }
    }

    @Test
    fun `should increase vehicle own mileage`() {
        val id = randomMaintenanceVehicleId()
        val additionalMileage = 4.0.toKilometers()
        val vehicle = maintenanceVehicle(id, coveredMileage = 5.0.toKilometers())

        vehicle.increaseMileage(additional = additionalMileage)

        vehicle should {
            it.coveredMileage shouldBe 9.0.toKilometers()
            it.popEvents().shouldContainExactly(
                VehicleCoveredMileageIncreased(vehicleId = id, added = additionalMileage)
            )
        }
    }

    @Test
    fun `should detect fault`() {
        val id = randomMaintenanceVehicleId()
        val vehicle = maintenanceVehicle(id)

        vehicle.detectFault()

        vehicle should {
            it.broken shouldBe true
            it.popEvents().shouldContainExactly(
                VehicleBroken(vehicleId = id)
            )
        }
    }

    @Test
    fun `vehicle shouldn be broken twice`() {
        val id = randomMaintenanceVehicleId()
        val vehicle = maintenanceVehicle(id, broken = true)

        vehicle.detectFault()

        vehicle should {
            it.broken shouldBe true
            it.popEvents() shouldContainExactly emptyList()
        }
    }

    @Test
    fun `should repair broken vehicle`() {
        val id = randomMaintenanceVehicleId()
        val vehicle = maintenanceVehicle(id, broken = true)

        vehicle.repair()

        vehicle should {
            it.broken shouldBe false
            it.popEvents().shouldContainExactly(
                VehicleRepaired(vehicleId = id)
            )
        }
    }

    @Test
    fun `shouldn't repair non-broken vehicle`() {
        val id = randomMaintenanceVehicleId()
        val vehicle = maintenanceVehicle(id, broken = false)

        vehicle.repair()

        vehicle should {
            it.broken shouldBe false
            it.popEvents() shouldContainExactly emptyList()
        }
    }
}