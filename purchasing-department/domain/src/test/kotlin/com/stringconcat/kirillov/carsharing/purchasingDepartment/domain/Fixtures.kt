package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import java.time.LocalDate
import kotlin.random.Random.Default.nextLong

fun purchasingVehicleId() = PurchasingVehicleId(value = nextLong())
fun purchasingVehicle(
    registrationPlate: RegistrationPlate = registrationPlate(),
    vin: Vin = vin(),
) = PurchasingVehicle(
    id = purchasingVehicleId(),
    model = vehicleModel(),
    registrationPlate,
    vin,
    purchaseDate = LocalDate.now(),
    capacity = Capacity.five()
)