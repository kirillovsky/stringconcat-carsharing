package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import java.time.LocalDate

object PurchasingVehicleRestorer {
    fun restore(
        id: PurchasingVehicleId,
        model: VehicleModel,
        registrationPlate: RegistrationPlate,
        vin: Vin,
        purchaseDate: LocalDate,
        capacity: Capacity,
    ): PurchasingVehicle = PurchasingVehicle(
        id,
        model,
        registrationPlate,
        vin,
        purchaseDate,
        capacity,
    )
}