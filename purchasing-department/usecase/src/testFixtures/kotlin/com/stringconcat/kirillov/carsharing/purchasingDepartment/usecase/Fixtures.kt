package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import arrow.core.getOrHandle
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleId
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.randomCapacity
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.RegistrationPlateDate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.VehicleModeData

class FakePurchasingVehiclePersister : HashMap<PurchasingVehicleId, PurchasingVehicle>(), PurchasingVehiclePersister {
    override fun save(vehicle: PurchasingVehicle) {
        this[vehicle.id] = vehicle
    }
}

fun addVehicleToBalanceRequest(
    registrationPlate: RegistrationPlate = registrationPlate(),
    vin: Vin = vin(),
) = AddVehicleToBalanceRequest.from(
    modelData = randomVehicleModel().run { VehicleModeData(maker, name) },
    registrationPlateData = registrationPlate.run { RegistrationPlateDate(number, regionCode, series) },
    vinData = vin.code,
    capacityData = randomCapacity().value
).getOrHandle { error(it.message) }

class FakePurchasingVehicleExtractor : PurchasingVehicleExtractor, HashMap<PurchasingVehicleId, PurchasingVehicle>() {
    override fun getAll(): List<PurchasingVehicle> = values.toList()

    override fun getById(id: PurchasingVehicleId): PurchasingVehicle? = get(id)
}