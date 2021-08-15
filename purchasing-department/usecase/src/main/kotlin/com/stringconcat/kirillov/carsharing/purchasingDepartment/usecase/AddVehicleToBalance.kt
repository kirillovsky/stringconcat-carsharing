package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleId

interface AddVehicleToBalance {
    fun execute(request: AddVehicleToBalanceRequest): Either<AddVehicleToBalanceUseCaseError, PurchasingVehicleId>
}

sealed class AddVehicleToBalanceUseCaseError(val message: String) {
    object AlreadyExistsWithSameRegistrationPlate : AddVehicleToBalanceUseCaseError(
        message = "Already exists vehicle with same registration plate"
    )

    object AlreadyExistsWithSameVin : AddVehicleToBalanceUseCaseError("Already exists vehicle with same vin")
}