package com.stringconcat.kirillov.carsharing.maintenance.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import kotlin.random.Random.Default.nextLong

fun randomMaintenanceVehicleId() = MaintenanceVehicleId(value = nextLong())

fun maintenanceVehicle(
    id: MaintenanceVehicleId = randomMaintenanceVehicleId(),
    coveredMileage: Distance = randomDistance(),
    broken: Boolean = false,
) = MaintenanceVehicle
    .addVehicleToInventory(
        id,
        model = randomVehicleModel(),
        vin = vin(),
        registrationPlate = registrationPlate(),
        coveredMileage = coveredMileage,
    ).apply {
        if (broken) detectFault()

        popEvents()
    }