package com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.usecase

import arrow.core.getOrHandle
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.fixtures.purchasingDepartment.domain.randomCapacity
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleId
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.RegistrationPlateDate
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.AddVehicleToBalanceRequest.VehicleModeData
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.PurchasingVehicleExtractor
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.PurchasingVehiclePersister

class InMemoryPurchasingVehicleRepository :
    MutableMap<PurchasingVehicleId, PurchasingVehicle> by HashMap(),
    PurchasingVehiclePersister,
    PurchasingVehicleExtractor {
    override fun save(vehicle: PurchasingVehicle) {
        this[vehicle.id] = vehicle
    }

    override fun getAll(): List<PurchasingVehicle> = values.toList()

    override fun getById(id: PurchasingVehicleId): PurchasingVehicle? = this[id]
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