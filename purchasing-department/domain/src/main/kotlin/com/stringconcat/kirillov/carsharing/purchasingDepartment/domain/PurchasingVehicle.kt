package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameRegistrationPlate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameVin
import java.time.LocalDate

class PurchasingVehicle internal constructor(
    id: PurchasingVehicleId,
    val model: VehicleModel,
    val registrationPlate: RegistrationPlate,
    val vin: Vin,
    val purchaseDate: LocalDate,
    val capacity: Capacity,
) : AggregateRoot<PurchasingVehicleId>(id) {
    sealed class CreatePurchasingVehicleError : BusinessError {
        object AlreadyExistsWithSameRegistrationPlate : CreatePurchasingVehicleError()
        object AlreadyExistsWithSameVin : CreatePurchasingVehicleError()
    }

    companion object {
        fun addVehicleToBalance(
            idGenerator: PurchasingVehicleIdGenerator,
            vehicleExistsByRegistrationPlate: PurchasingVehicleExists.ByRegistrationPlate,
            vehicleExistsByVin: PurchasingVehicleExists.ByVin,
            model: VehicleModel,
            registrationPlate: RegistrationPlate,
            vin: Vin,
            capacity: Capacity,
        ): Either<CreatePurchasingVehicleError, PurchasingVehicle> {
            val id = idGenerator.generate()

            return when {
                vehicleExistsByRegistrationPlate.check(registrationPlate) -> {
                    AlreadyExistsWithSameRegistrationPlate.left()
                }
                vehicleExistsByVin.check(vin) -> AlreadyExistsWithSameVin.left()
                else -> PurchasingVehicle(
                    id,
                    model,
                    registrationPlate,
                    vin,
                    purchaseDate = LocalDate.now(),
                    capacity
                ).apply {
                    addEvent(VehicleAddedToPurchasingBalanceEvent(id))
                }.right()
            }
        }
    }
}