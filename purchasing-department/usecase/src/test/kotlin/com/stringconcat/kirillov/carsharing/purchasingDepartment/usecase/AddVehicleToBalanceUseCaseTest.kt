package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.domain.purchasingVehicle
import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.domain.randomCapacity
import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.domain.randomPurchasingVehicleId
import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.usecase.InMemoryPurchasingVehicleRepository
import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.usecase.addVehicleToBalanceRequest
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingDepartmentEvents.VehicleAddedToPurchasingBalance
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
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
        val repo = InMemoryPurchasingVehicleRepository()

        val result = AddVehicleToBalanceUseCase(
            idGenerator = { vehicleId },
            vehicleExtractor = repo,
            vehiclePersister = repo
        ).execute(
            AddVehicleToBalanceRequest(
                model = model,
                registrationPlate = registrationPlate,
                vin = vin,
                capacity = capacity
            )
        )

        result shouldBeRight vehicleId
        repo[vehicleId] should {
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
        val persister = InMemoryPurchasingVehicleRepository()
        val existingRegistrationPlate = registrationPlate()

        val result = AddVehicleToBalanceUseCase(
            idGenerator = { randomPurchasingVehicleId() },
            vehicleExtractor = InMemoryPurchasingVehicleRepository().apply {
                put(
                    randomPurchasingVehicleId(), purchasingVehicle(registrationPlate = existingRegistrationPlate)
                )
            },
            vehiclePersister = persister
        ).execute(
            addVehicleToBalanceRequest(registrationPlate = existingRegistrationPlate)
        )

        result shouldBeLeft AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameRegistrationPlate
        persister.shouldBeEmpty()
    }

    @Test
    fun `shouldn't add vehicle to balance if exists ones with same vin`() {
        val persister = InMemoryPurchasingVehicleRepository()
        val existingVin = vin()

        val result = AddVehicleToBalanceUseCase(
            idGenerator = { randomPurchasingVehicleId() },
            vehicleExtractor = InMemoryPurchasingVehicleRepository().apply {
                put(
                    randomPurchasingVehicleId(), purchasingVehicle(vin = existingVin)
                )
            },
            vehiclePersister = persister
        ).execute(
            addVehicleToBalanceRequest(vin = existingVin)
        )

        result shouldBeLeft AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameVin
        persister.shouldBeEmpty()
    }
}