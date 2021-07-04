package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.Capacity.Companion.five
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameRegistrationPlate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameVin
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate.now
import org.junit.jupiter.api.Test

internal class PurchasingVehicleTest {
    @Test
    fun `addVehicleToBalance should create vehicle in context with special event`() {
        val expectedId = purchasingVehicleId()
        val expectedCapacity = five()
        val expectedModel = vehicleModel()
        val expectedRegistrationPlate = registrationPlate()
        val expectedVin = vin()

        val vehicle = PurchasingVehicle.addVehicleToBalance(
            idGenerator = { expectedId },
            vehicleExistsByRegistrationPlate = NotExistsByRegistrationPlate,
            vehicleExistsByVin = NotExistsByVin,
            model = expectedModel,
            registrationPlate = expectedRegistrationPlate,
            vin = expectedVin,
            capacity = expectedCapacity
        )

        vehicle shouldBeRight {
            it.id shouldBe expectedId
            it.capacity shouldBe expectedCapacity
            it.model shouldBe expectedModel
            it.purchaseDate shouldBe now()
            it.registrationPlate shouldBe expectedRegistrationPlate
            it.vin shouldBe expectedVin
            it.popEvents().shouldContainExactly(VehicleAddedToPurchasingBalanceEvent(expectedId))
        }
    }

    @Test
    fun `addVehicleToBalance shouldn't create vehicle with already exists by registrationPlate`() {
        val existsRegistrationPlate = registrationPlate()

        val vehicle = PurchasingVehicle.addVehicleToBalance(
            idGenerator = { purchasingVehicleId() },
            vehicleExistsByRegistrationPlate = { it == existsRegistrationPlate },
            vehicleExistsByVin = NotExistsByVin,
            model = vehicleModel(),
            registrationPlate = existsRegistrationPlate,
            vin = vin(),
            capacity = five()
        )

        vehicle shouldBeLeft {
            it shouldBe AlreadyExistsWithSameRegistrationPlate
        }
    }

    @Test
    fun `addVehicleToBalance shouldn't create vehicle with already exists by vin`() {
        val existsVin = vin()

        val vehicle = PurchasingVehicle.addVehicleToBalance(
            vehicleExistsByRegistrationPlate = NotExistsByRegistrationPlate,
            vehicleExistsByVin = { it == existsVin },
            idGenerator = { purchasingVehicleId() },
            model = vehicleModel(),
            registrationPlate = registrationPlate(),
            vin = existsVin,
            capacity = five()
        )

        vehicle shouldBeLeft {
            it shouldBe AlreadyExistsWithSameVin
        }
    }
}

private object NotExistsByRegistrationPlate : PurchasingVehicleExists.ByRegistrationPlate {
    override fun check(registrationPlate: RegistrationPlate) = false
}

private object NotExistsByVin : PurchasingVehicleExists.ByVin {
    override fun check(vin: Vin) = false
}