package com.stringconcat.kirillov.carsharing.application.rules

import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.fixtures.ride.usecase.MockPutRideVehicleUseCase
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleAddedToMaintenanceInventory
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AddNewVehicleToRentalPoolTest {
    @Test
    fun `should add new vehicle to rental pool`() {
        val putRideVehicle = MockPutRideVehicleUseCase()
        val rule = AddNewVehicleToRentalPool(putRideVehicle = putRideVehicle)
        val vehicleId = randomMaintenanceVehicleId()

        rule.handle(event = VehicleAddedToMaintenanceInventory(vehicleId))

        putRideVehicle should {
            it.receivedRideVehicleId shouldBe RideVehicleId(vehicleId.value)
            it.receivedInRentalPool shouldBe true
        }
    }
}