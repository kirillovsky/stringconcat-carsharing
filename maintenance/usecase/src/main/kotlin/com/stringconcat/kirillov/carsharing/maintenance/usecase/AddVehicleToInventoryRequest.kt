package com.stringconcat.kirillov.carsharing.maintenance.usecase

import arrow.core.Either
import arrow.core.extensions.either.apply.tupled
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateRegistrationPlateError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVehicleModelError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVinError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance.NegativeDistanceValueError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventoryRequest.InvalidVehicleParameters
import java.math.BigDecimal

data class AddVehicleToInventoryRequest internal constructor(
    val id: MaintenanceVehicleId,
    val model: VehicleModel,
    val vin: Vin,
    val coveredMileage: Distance,
    val registrationPlate: RegistrationPlate
) {
    companion object {
        fun from(
            id: Long,
            modelData: VehicleModelData,
            registrationPlateData: RegistrationPlateData,
            vinCode: String,
            coveredMileageValue: BigDecimal
        ): Either<InvalidVehicleParameters, AddVehicleToInventoryRequest> =
            tupled(
                VehicleModel.from(modelData.name, modelData.maker),
                registrationPlateData.run { RegistrationPlate.from(series, number, regionCode) },
                Vin.from(vinCode),
                Distance.ofKilometers(coveredMileageValue)
            ).map { (model, registrationPlate, vin, coveredDistance) ->
                AddVehicleToInventoryRequest(
                    MaintenanceVehicleId(id),
                    model,
                    vin,
                    coveredDistance,
                    registrationPlate
                )
            }.mapLeft(BusinessError::toErrorMessage)
    }

    class VehicleModelData(val maker: String, val name: String)

    class RegistrationPlateData(val number: String, val regionCode: String, val series: String)
    data class InvalidVehicleParameters(val message: String)
}

private fun BusinessError.toErrorMessage(): InvalidVehicleParameters =
    when (this) {
        is CreateVehicleModelError -> InvalidVehicleParameters("Invalid model")
        is CreateRegistrationPlateError -> InvalidVehicleParameters("Invalid registrationPlate")
        is CreateVinError -> InvalidVehicleParameters("Invalid vin")
        is NegativeDistanceValueError -> InvalidVehicleParameters("Invalid coveredDistance")
        else -> error("Unexpected business error received - $this")
    }