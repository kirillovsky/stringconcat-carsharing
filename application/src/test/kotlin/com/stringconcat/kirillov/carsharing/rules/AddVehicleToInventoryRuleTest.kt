package com.stringconcat.kirillov.carsharing.rules

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventory
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventoryRequest
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingDepartmentEvents.VehicleAddedToPurchasingBalance
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.purchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.randomPurchasingVehicleId
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.FakePurchasingVehicleExtractor
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AddVehicleToInventoryRuleTest {
    @Test
    fun `handle should add vehicle to maintenance inventory with extracted purchasing vehicle params`() {
        val fakeAddVehicleToInventoryUseCase = FakeAddVehicleToInventoryUseCase()
        val purchasingVehicleId = randomPurchasingVehicleId()
        val purchasingVehicle = purchasingVehicle(id = purchasingVehicleId)
        val rule = AddVehicleToInventoryRule(
            addVehicleToInventory = fakeAddVehicleToInventoryUseCase,
            purchasingVehicleExtractor = FakePurchasingVehicleExtractor().apply {
                put(purchasingVehicleId, purchasingVehicle)
            }
        )

        rule.handle(VehicleAddedToPurchasingBalance(vehicleId = purchasingVehicleId))

        fakeAddVehicleToInventoryUseCase.receivedRequest should {
            it.shouldNotBeNull()
            it.vin shouldBe purchasingVehicle.vin
            it.id shouldBe MaintenanceVehicleId(purchasingVehicleId.value)
            it.registrationPlate shouldBe purchasingVehicle.registrationPlate
            it.model shouldBe purchasingVehicle.model
            it.coveredMileage shouldBe 0.0.toKilometers()
        }
    }

    @Test
    fun `handle shouldn't add vehicle to maintenance inventorye if unable to extract purchasing vehicle`() {
        val fakeAddVehicleToInventoryUseCase = FakeAddVehicleToInventoryUseCase()
        val rule = AddVehicleToInventoryRule(
            addVehicleToInventory = fakeAddVehicleToInventoryUseCase,
            purchasingVehicleExtractor = FakePurchasingVehicleExtractor()
        )

        rule.handle(VehicleAddedToPurchasingBalance(vehicleId = randomPurchasingVehicleId()))

        fakeAddVehicleToInventoryUseCase.receivedRequest.shouldBeNull()
    }
}

private class FakeAddVehicleToInventoryUseCase : AddVehicleToInventory {
    var receivedRequest: AddVehicleToInventoryRequest? = null

    override fun execute(request: AddVehicleToInventoryRequest): MaintenanceVehicleId {
        receivedRequest = request
        return randomMaintenanceVehicleId()
    }
}