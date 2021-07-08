package com.stringconcat.kirillov.carsharing.maintenance.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin

fun maintenanceVehicleId() = MaintenanceVehicleId(value = 100501L)

fun maintenanceVehicle(
    id: MaintenanceVehicleId,
    coveredMileage: Distance = 1.0.toKilometers(),
    broken: Boolean = false
) = MaintenanceVehicle(
    id,
    model = vehicleModel(),
    vin = vin(),
    coveredMileage = coveredMileage,
).also {
    it.broken = broken
}