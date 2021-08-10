package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import arrow.core.Either
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.Companion.addVehicleToBalance
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameRegistrationPlate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameVin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleId
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleIdGenerator

class AddVehicleToBalanceUseCase(
    private val idGenerator: PurchasingVehicleIdGenerator,
    private val purchasingVehicleExtractor: PurchasingVehicleExtractor,
    private val purchasingVehiclePersister: PurchasingVehiclePersister,
) {
    fun execute(request: AddVehicleToBalanceRequest): Either<AddVehicleToBalanceUseCaseError, PurchasingVehicleId> =
        addVehicleToBalance(
            idGenerator = idGenerator,
            model = request.model,
            registrationPlate = request.registrationPlate,
            vin = request.vin,
            capacity = request.capacity,
            existingVehicles = purchasingVehicleExtractor.getAll()
        ).map {
            purchasingVehiclePersister.save(it)

            it.id
        }.mapLeft { it.toUseCaseError() }
}

private fun CreatePurchasingVehicleError.toUseCaseError(): AddVehicleToBalanceUseCaseError = when (this) {
    is AlreadyExistsWithSameRegistrationPlate -> AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameRegistrationPlate
    is AlreadyExistsWithSameVin -> AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameVin
}

sealed class AddVehicleToBalanceUseCaseError(val message: String) {
    object AlreadyExistsWithSameRegistrationPlate : AddVehicleToBalanceUseCaseError(
        message = "Already exists vehicle with same registration plate"
    )

    object AlreadyExistsWithSameVin : AddVehicleToBalanceUseCaseError("Already exists vehicle with same vin")
}