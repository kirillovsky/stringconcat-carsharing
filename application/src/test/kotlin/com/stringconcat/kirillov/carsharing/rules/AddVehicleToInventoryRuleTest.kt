package com.stringconcat.kirillov.carsharing.rules

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventory
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventoryRequest
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingDepartmentEvents.VehicleAddedToPurchasingBalance
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.purchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.randomPurchasingVehicleId
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.InMemoryPurchasingVehicleRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AddVehicleToInventoryRuleTest {
    @Test
    fun `should add vehicle to maintenance inventory with extracted purchasing vehicle params`() {
        val usecase = MockAddVehicleToInventoryUseCase()
        val purchasingVehicleId = randomPurchasingVehicleId()
        val purchasingVehicle = purchasingVehicle(id = purchasingVehicleId)
        val rule = AddVehicleToInventoryRule(
            addVehicleToInventory = usecase,
            purchasingVehicleExtractor = InMemoryPurchasingVehicleRepository().apply {
                put(purchasingVehicleId, purchasingVehicle)
            }
        )

        rule.handle(VehicleAddedToPurchasingBalance(vehicleId = purchasingVehicleId))

        usecase.receivedRequest should {
            it.shouldNotBeNull()
            it.vin shouldBe purchasingVehicle.vin
            it.id shouldBe MaintenanceVehicleId(purchasingVehicleId.value)
            it.registrationPlate shouldBe purchasingVehicle.registrationPlate
            it.model shouldBe purchasingVehicle.model
            it.coveredMileage shouldBe 0.0.toKilometers()
        }
    }

    @Test
    fun `shouldn't add vehicle to maintenance inventory if unable to extract purchasing vehicle`() {
        val usecase = MockAddVehicleToInventoryUseCase()
        val rule = AddVehicleToInventoryRule(
            addVehicleToInventory = usecase,
            purchasingVehicleExtractor = InMemoryPurchasingVehicleRepository()
        )
        val vehicleId = randomPurchasingVehicleId()

        val exception = shouldThrow<IllegalStateException> {
            rule.handle(VehicleAddedToPurchasingBalance(vehicleId = vehicleId))
        }

        usecase.receivedRequest.shouldBeNull()
        exception.message shouldBe "Unable to find purchasing vehicle by id=$vehicleId"
    }
}

private class MockAddVehicleToInventoryUseCase : AddVehicleToInventory {
    var receivedRequest: AddVehicleToInventoryRequest? = null

    override fun execute(request: AddVehicleToInventoryRequest): MaintenanceVehicleId {
        receivedRequest = request
        return randomMaintenanceVehicleId()
    }
}