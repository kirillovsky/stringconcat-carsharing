package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingDepartmentEvents.VehicleAddedToPurchasingBalance
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.purchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.randomCapacity
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.randomPurchasingVehicleId
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class AddVehicleToBalanceUseCaseTest {
    @Test
    fun `should add vehicle to balance`() {
        val vehicleId = randomPurchasingVehicleId()
        val model = randomVehicleModel()
        val registrationPlate = registrationPlate()
        val vin = vin()
        val capacity = randomCapacity()
        val persister = FakePurchasingVehiclePersister()

        val result = AddVehicleToBalanceUseCase(
            idGenerator = { vehicleId },
            vehicleExtractor = FakePurchasingVehicleExtractor(),
            vehiclePersister = persister
        ).execute(
            AddVehicleToBalanceRequest(
                model = model,
                registrationPlate = registrationPlate,
                vin = vin,
                capacity = capacity
            )
        )

        result shouldBeRight {
            it shouldBe vehicleId
        }
        persister[vehicleId] should {
            it.shouldNotBeNull()
            it.id shouldBe vehicleId
            it.vin shouldBe vin
            it.registrationPlate shouldBe registrationPlate
            it.model shouldBe model
            it.capacity shouldBe capacity
            it.popEvents().shouldContainExactly(VehicleAddedToPurchasingBalance(vehicleId))
        }
    }

    @Test
    fun `shouldn't add vehicle to balance if exists ones with same registration plate`() {
        val persister = FakePurchasingVehiclePersister()
        val existingRegistrationPlate = registrationPlate()

        val result = AddVehicleToBalanceUseCase(
            idGenerator = { randomPurchasingVehicleId() },
            vehicleExtractor = FakePurchasingVehicleExtractor().apply {
                put(
                    randomPurchasingVehicleId(), purchasingVehicle(registrationPlate = existingRegistrationPlate)
                )
            },
            vehiclePersister = persister
        ).execute(
            addVehicleToBalanceRequest(registrationPlate = existingRegistrationPlate)
        )

        result shouldBeLeft {
            it shouldBe AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameRegistrationPlate
        }
        persister.shouldBeEmpty()
    }

    @Test
    fun `shouldn't add vehicle to balance if exists ones with same vin`() {
        val persister = FakePurchasingVehiclePersister()
        val existingVin = vin()

        val result = AddVehicleToBalanceUseCase(
            idGenerator = { randomPurchasingVehicleId() },
            vehicleExtractor = FakePurchasingVehicleExtractor().apply {
                put(
                    randomPurchasingVehicleId(), purchasingVehicle(vin = existingVin)
                )
            },
            vehiclePersister = persister
        ).execute(
            addVehicleToBalanceRequest(vin = existingVin)
        )

        result shouldBeLeft {
            it shouldBe AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameVin
        }
        persister.shouldBeEmpty()
    }
}