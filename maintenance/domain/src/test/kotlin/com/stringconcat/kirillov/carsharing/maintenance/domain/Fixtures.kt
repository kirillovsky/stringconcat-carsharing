package com.stringconcat.kirillov.carsharing.maintenance.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import kotlin.random.Random.Default.nextLong

fun maintenanceVehicleId() = MaintenanceVehicleId(value = nextLong())

fun maintenanceVehicle(
    id: MaintenanceVehicleId,
    coveredMileage: Distance = randomDistance(),
    broken: Boolean = false,
) = MaintenanceVehicle(
    id,
    model = vehicleModel(),
    vin = vin(),
    coveredMileage = coveredMileage,
).also {
    it.broken = broken
}