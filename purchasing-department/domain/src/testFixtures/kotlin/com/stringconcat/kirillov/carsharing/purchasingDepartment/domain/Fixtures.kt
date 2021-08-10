package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import arrow.core.getOrHandle
import com.github.javafaker.Faker
import com.stringconcat.kirillov.carsharing.commons.types.error.failOnBusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.registrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomVehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.vin

private val faker = Faker()

fun randomPurchasingVehicleId() = PurchasingVehicleId(value = faker.number().randomNumber())

fun purchasingVehicle(
    registrationPlate: RegistrationPlate = registrationPlate(),
    vin: Vin = vin(),
) = PurchasingVehicle
    .addVehicleToBalance(
        idGenerator = { randomPurchasingVehicleId() },
        model = randomVehicleModel(),
        registrationPlate,
        vin,
        capacity = randomCapacity(),
        existingVehicles = emptyList()
    ).getOrHandle(::failOnBusinessError).apply {
        popEvents()
    }

fun randomCapacity() = Capacity
    .from(
        value = faker.number().randomDigitNotZero()
    ).getOrHandle(::failOnBusinessError)