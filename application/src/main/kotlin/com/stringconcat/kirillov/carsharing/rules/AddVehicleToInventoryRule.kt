package com.stringconcat.kirillov.carsharing.rules

import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.stringconcat.kirillov.carsharing.listeners.DomainEventListener
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventory
import com.stringconcat.kirillov.carsharing.maintenance.usecase.AddVehicleToInventoryRequest
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingDepartmentEvents.VehicleAddedToPurchasingBalance
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.PurchasingVehicleExtractor
import java.math.BigDecimal.ZERO

class AddVehicleToInventoryRule(
    private val addVehicleToInventory: AddVehicleToInventory,
    private val purchasingVehicleExtractor: PurchasingVehicleExtractor
) : DomainEventListener<VehicleAddedToPurchasingBalance> {
    override fun eventType() = VehicleAddedToPurchasingBalance::class

    override fun handle(event: VehicleAddedToPurchasingBalance) {
        purchasingVehicleExtractor.getById(event.vehicleId).rightIfNotNull {
            "Unable to find purchasingVehicle by id - ${event.vehicleId}"
        }.flatMap {
            AddVehicleToInventoryRequest.from(
                id = event.vehicleId.value,
                modelData = it.model.run { AddVehicleToInventoryRequest.VehicleModelData(maker, name) },
                registrationPlateData = it.registrationPlate.run {
                    AddVehicleToInventoryRequest.RegistrationPlateData(number, regionCode, series)
                },
                vinCode = it.vin.code,
                coveredMileageValue = ZERO
            )
        }.map(addVehicleToInventory::execute).mapLeft {
            print("AddVehicleToInventoryRule. Unable to create addVehicleToInventoryRequest cause: $it")
        }
    }
}