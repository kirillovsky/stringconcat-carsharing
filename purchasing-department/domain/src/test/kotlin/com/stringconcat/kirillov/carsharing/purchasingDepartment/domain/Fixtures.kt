package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.github.javafaker.Faker
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin
import java.time.LocalDate

private val faker = Faker()

fun purchasingVehicleId() = PurchasingVehicleId(value = faker.number().randomNumber())

fun purchasingVehicle(
    registrationPlate: RegistrationPlate = registrationPlate(),
    vin: Vin = vin(),
) = PurchasingVehicle(
    id = purchasingVehicleId(),
    model = vehicleModel(),
    registrationPlate,
    vin,
    purchaseDate = LocalDate.now(),
    capacity = randomCapacity()
)

fun randomCapacity(): Capacity = Capacity(value = faker.number().numberBetween(1, 5))

fun Int.toCapacity() = Capacity(value = this)