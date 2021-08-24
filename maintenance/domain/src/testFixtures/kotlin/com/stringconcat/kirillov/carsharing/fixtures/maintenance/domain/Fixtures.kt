package com.stringconcat.kirillov.carsharing.fixtures.maintenance.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.vin
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicle
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId
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