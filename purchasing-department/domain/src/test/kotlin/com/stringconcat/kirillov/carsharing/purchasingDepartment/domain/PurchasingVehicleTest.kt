package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.Capacity.Companion.five
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingDepartmentEvents.VehicleAddedToPurchasingBalance
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameRegistrationPlate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameVin
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate.now
import org.junit.jupiter.api.Test

internal class PurchasingVehicleTest {
    @Test
    fun `should add vehicle to balance`() {
        val expectedId = randomPurchasingVehicleId()
        val expectedCapacity = five()
        val expectedModel = randomVehicleModel()
        val expectedRegistrationPlate = registrationPlate()
        val expectedVin = vin()

        val vehicle = PurchasingVehicle.addVehicleToBalance(
            idGenerator = { expectedId },
            model = expectedModel,
            registrationPlate = expectedRegistrationPlate,
            vin = expectedVin,
            capacity = expectedCapacity,
            existingVehicles = emptyList()
        )

        vehicle shouldBeRight {
            it.id shouldBe expectedId
            it.capacity shouldBe expectedCapacity
            it.model shouldBe expectedModel
            it.purchaseDate shouldBe now()
            it.registrationPlate shouldBe expectedRegistrationPlate
            it.vin shouldBe expectedVin
            it.popEvents().shouldContainExactly(VehicleAddedToPurchasingBalance(expectedId))
        }
    }

    @Test
    fun `shouldn't add vehicle to balance if already exists another vehicle with same registration plate`() {
        val existsRegistrationPlate = registrationPlate()
        val oneVin = vin("MMHJU81VCBU266113")
        val anotherVin = vin("JJHJU81VCBU266113")

        val vehicle = PurchasingVehicle.addVehicleToBalance(
            idGenerator = { randomPurchasingVehicleId() },
            model = randomVehicleModel(),
            registrationPlate = existsRegistrationPlate,
            vin = oneVin,
            capacity = five(),
            existingVehicles = listOf(
                purchasingVehicle(registrationPlate = existsRegistrationPlate, vin = anotherVin)
            )
        )

        vehicle shouldBeLeft {
            it shouldBe AlreadyExistsWithSameRegistrationPlate
        }
    }

    @Test
    fun `shouldn't add vehicle to balance if already exists another vehicle with same vin`() {
        val existsVin = vin()
        val oneRegistrationPlate = registrationPlate(series = "ууу", number = "100", regionCode = "12")
        val anotherRegistrationPlate = registrationPlate(series = "уму", number = "750", regionCode = "179")

        val vehicle = PurchasingVehicle.addVehicleToBalance(
            idGenerator = { randomPurchasingVehicleId() },
            model = randomVehicleModel(),
            registrationPlate = oneRegistrationPlate,
            vin = existsVin,
            capacity = five(),
            existingVehicles = listOf(
                purchasingVehicle(vin = existsVin, registrationPlate = anotherRegistrationPlate)
            )
        )

        vehicle shouldBeLeft {
            it shouldBe AlreadyExistsWithSameVin
        }
    }
}