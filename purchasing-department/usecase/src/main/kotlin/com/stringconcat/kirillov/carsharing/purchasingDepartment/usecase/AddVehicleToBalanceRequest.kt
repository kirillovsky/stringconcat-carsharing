package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateRegistrationPlateError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVehicleModelError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVinError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.Capacity
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.Capacity.IllegalCapacityValueError
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.InvalidVehicleParameters

data class AddVehicleToBalanceRequest internal constructor(
    val model: VehicleModel,
    val registrationPlate: RegistrationPlate,
    val vin: Vin,
    val capacity: Capacity,
) {
    companion object {
        fun from(
            modelData: VehicleModeData,
            registrationPlateData: RegistrationPlateDate,
            vinData: String,
            capacityData: Int,
        ): Either<InvalidVehicleParameters, AddVehicleToBalanceRequest> {
            return tupled(
                modelData.run { VehicleModel.from(name, maker) },
                registrationPlateData.run { RegistrationPlate.from(series, number, regionCode) },
                Vin.from(vinData),
                Capacity.from(capacityData)
            ).map { (model, registrationPlate, vin, capacity) ->
                AddVehicleToBalanceRequest(model, registrationPlate, vin, capacity)
            }.mapLeft { it.toErrorMessage() }
        }
    }

    class VehicleModeData(val maker: String, val name: String)

    class RegistrationPlateDate(val number: String, val regionCode: String, val series: String)
    data class InvalidVehicleParameters(val message: String)
}

private fun BusinessError.toErrorMessage(): InvalidVehicleParameters =
    when (this) {
        is CreateVehicleModelError -> InvalidVehicleParameters("Invalid model")
        is CreateRegistrationPlateError -> InvalidVehicleParameters("Invalid registrationPlate")
        is CreateVinError -> InvalidVehicleParameters("Invalid vin")
        is IllegalCapacityValueError -> InvalidVehicleParameters("Invalid capacity")
        else -> error("Unexpected business error received - $this")
    }