package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleId
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.randomCapacity

class FakePurchasingVehiclePersister : HashMap<PurchasingVehicleId, PurchasingVehicle>(), PurchasingVehiclePersister {
    override fun save(vehicle: PurchasingVehicle) {
        this[vehicle.id] = vehicle
    }
}

fun addVehicleToBalanceRequest(
    registrationPlate: RegistrationPlate = registrationPlate(),
    vin: Vin = vin(),
) = AddVehicleToBalanceRequest(
    model = randomVehicleModel(),
    registrationPlate = registrationPlate,
    vin = vin,
    capacity = randomCapacity()
)