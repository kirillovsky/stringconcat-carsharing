package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.domain.randomCapacity
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.InvalidAddVehicleToBalanceParameters
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.RegistrationPlateDate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.VehicleModeData
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import org.junit.jupiter.api.Test

internal class AddVehicleToBalanceRequestTest {
    private val randomVehicleModeData = randomVehicleModel().run { VehicleModeData(maker, name) }
    private val randomRegistrationPlateData = registrationPlate().run {
        RegistrationPlateDate(number, regionCode, series)
    }
    private val randomVinData = vin().code
    private val randomCapacityData = randomCapacity().value

    @Test
    fun `request should contains properties for further adding vehicle to balance`() {
        val model = randomVehicleModel()
        val registrationPlate = registrationPlate()
        val vin = vin()
        val capacity = randomCapacity()

        val request = AddVehicleToBalanceRequest.from(
            modelData = VehicleModeData(model.maker, model.name),
            registrationPlateData = RegistrationPlateDate(
                registrationPlate.number,
                registrationPlate.regionCode,
                registrationPlate.series
            ),
            vinData = vin.code,
            capacityData = capacity.value
        )

        request shouldBeRight AddVehicleToBalanceRequest(model, registrationPlate, vin, capacity)
    }

    @Test
    fun `should map vehicle model creation business error`() {
        val result = AddVehicleToBalanceRequest.from(
            modelData = VehicleModeData(maker = "", name = " "),
            randomRegistrationPlateData,
            randomVinData,
            randomCapacityData
        )

        result shouldBeLeft InvalidAddVehicleToBalanceParameters("Invalid model")
    }

    @Test
    fun `should map registration plate creation business error`() {
        val result = AddVehicleToBalanceRequest.from(
            randomVehicleModeData,
            registrationPlateData = RegistrationPlateDate(
                number = "invalid number",
                regionCode = "invalid regionCode",
                series = "invalid series"
            ),
            randomVinData,
            randomCapacityData
        )

        result shouldBeLeft InvalidAddVehicleToBalanceParameters("Invalid registrationPlate")
    }

    @Test
    fun `should map vin creation business error`() {
        val result = AddVehicleToBalanceRequest.from(
            randomVehicleModeData,
            randomRegistrationPlateData,
            vinData = "invalid vin data",
            randomCapacityData
        )

        result shouldBeLeft InvalidAddVehicleToBalanceParameters("Invalid vin")
    }

    @Test
    fun `should map capacity creation business error`() {
        val result = AddVehicleToBalanceRequest.from(
            randomVehicleModeData,
            randomRegistrationPlateData,
            randomVinData,
            capacityData = -2
        )

        result shouldBeLeft InvalidAddVehicleToBalanceParameters("Invalid capacity")
    }
}