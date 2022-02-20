package com.stringconcat.kirillov.carsharing.maintenance.usecase

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain.randomMaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventoryRequest.InvalidVehicleParameters
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventoryRequest.RegistrationPlateData
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventoryRequest.VehicleModelData
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

internal class AddVehicleToInventoryRequestTest {
    private val randomRegistrationPlateData = registrationPlate().run {
        RegistrationPlateData(number, regionCode, series)
    }
    private val randomVehicleModelData = randomVehicleModel().run {
        VehicleModelData(maker, name)
    }

    @Test
    fun `request should contains properties for further adding vehicle to inventory`() {
        val id = randomMaintenanceVehicleId()
        val model = randomVehicleModel()
        val registrationPlate = registrationPlate()
        val vin = vin()
        val coveredMileage = randomDistance()

        val result = AddVehicleToInventoryRequest.from(
            id = id.value,
            modelData = VehicleModelData(model.maker, model.name),
            registrationPlateData = RegistrationPlateData(
                registrationPlate.number,
                registrationPlate.regionCode,
                registrationPlate.series
            ),
            vinCode = vin.code,
            coveredMileageValue = coveredMileage.kilometers
        )

        result shouldBeRight AddVehicleToInventoryRequest(id, model, vin, coveredMileage, registrationPlate)
    }

    @Test
    fun `should map model creating business errors`() {
        val result = AddVehicleToInventoryRequest.from(
            id = randomMaintenanceVehicleId().value,
            modelData = VehicleModelData(maker = "", name = " "),
            registrationPlateData = randomRegistrationPlateData,
            vinCode = vin().code,
            coveredMileageValue = randomDistance().kilometers
        )

        result shouldBeLeft InvalidVehicleParameters("Invalid model")
    }

    @Test
    fun `should map registrationPlate creating business error`() {
        val result = AddVehicleToInventoryRequest.from(
            id = randomMaintenanceVehicleId().value,
            modelData = randomVehicleModelData,
            registrationPlateData = RegistrationPlateData(
                number = "Invalid number",
                regionCode = "Invalid region",
                series = "Invalid series"
            ),
            vinCode = vin().code,
            coveredMileageValue = randomDistance().kilometers
        )

        result shouldBeLeft InvalidVehicleParameters("Invalid registrationPlate")
    }

    @Test
    fun `should map vin creating business error`() {
        val result = AddVehicleToInventoryRequest.from(
            id = randomMaintenanceVehicleId().value,
            modelData = randomVehicleModelData,
            registrationPlateData = randomRegistrationPlateData,
            vinCode = "Invalid vin code",
            coveredMileageValue = randomDistance().kilometers
        )

        result shouldBeLeft InvalidVehicleParameters("Invalid vin")
    }

    @Test
    fun `should map coveredMileage business error`() {
        val result = AddVehicleToInventoryRequest.from(
            id = randomMaintenanceVehicleId().value,
            modelData = randomVehicleModelData,
            registrationPlateData = randomRegistrationPlateData,
            vinCode = vin().code,
            coveredMileageValue = (-200).toBigDecimal()
        )

        result shouldBeLeft InvalidVehicleParameters("Invalid coveredDistance")
    }
}