package com.stringconcat.kirillov.carsharing.application.rules

import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleEvents.VehicleBroken
import com.stringconcat.kirillov.carsharing.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.usecase.MockPutRideVehicleUseCase
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RemoveBrokenVehicleFromRentalPoolRuleTest {
    @Test
    fun `should remove broken vehicle from rental pool`() {
        val usecase = MockPutRideVehicleUseCase()
        val rule = RemoveBrokenVehicleFromRentalPoolRule(putRideVehicle = usecase)
        val vehicleId = randomMaintenanceVehicleId()

        rule.handle(event = VehicleBroken(vehicleId))

        usecase should {
            it.receivedRideVehicleId shouldBe RideVehicleId(vehicleId.value)
            it.receivedInRentalPool shouldBe false
        }
    }
}