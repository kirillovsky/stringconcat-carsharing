package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.Capacity.Companion.five
import io.kotest.matchers.shouldBe
import java.time.LocalDate.now
import org.junit.jupiter.api.Test

internal class PurchasingVehicleRestorerTest {
    @Test
    fun `vehicle restorer should restore vehicle entity from value objects`() {
        val expectedModel = vehicleModel()
        val expectedRegistrationPlate = registrationPlate()
        val expectedVin = vin()
        val expectedPurchaseDate = now()
        val expectedCapacity = five()
        val expectedId = purchasingVehicleId()

        val vehicle = PurchasingVehicleRestorer.restore(
            id = expectedId,
            model = expectedModel,
            registrationPlate = expectedRegistrationPlate,
            vin = expectedVin,
            purchaseDate = expectedPurchaseDate,
            capacity = expectedCapacity
        )

        with(vehicle) {
            id shouldBe expectedId
            model shouldBe expectedModel
            registrationPlate shouldBe expectedRegistrationPlate
            vin shouldBe expectedVin
            purchaseDate shouldBe expectedPurchaseDate
            capacity shouldBe expectedCapacity
        }
    }
}