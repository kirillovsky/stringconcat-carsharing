package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.Companion.addVehicleToBalance
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameRegistrationPlate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle.CreatePurchasingVehicleError.AlreadyExistsWithSameVin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleIdGenerator

class AddVehicleToBalanceUseCase(
    private val idGenerator: PurchasingVehicleIdGenerator,
    private val vehicleExtractor: PurchasingVehicleExtractor,
    private val vehiclePersister: PurchasingVehiclePersister,
) : AddVehicleToBalance {
    override fun execute(request: AddVehicleToBalanceRequest) =
        addVehicleToBalance(
            idGenerator = idGenerator,
            model = request.model,
            registrationPlate = request.registrationPlate,
            vin = request.vin,
            capacity = request.capacity,
            existingVehicles = vehicleExtractor.getAll()
        ).map {
            vehiclePersister.save(it)

            it.id
        }.mapLeft(CreatePurchasingVehicleError::toUseCaseError)
}

private fun CreatePurchasingVehicleError.toUseCaseError(): AddVehicleToBalanceUseCaseError = when (this) {
    is AlreadyExistsWithSameRegistrationPlate -> AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameRegistrationPlate
    is AlreadyExistsWithSameVin -> AddVehicleToBalanceUseCaseError.AlreadyExistsWithSameVin
}