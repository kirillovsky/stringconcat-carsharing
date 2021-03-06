package com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects

import arrow.core.getOrHandle
import com.github.javafaker.Faker
import com.stringconcat.kirillov.carsharing.commons.types.error.failOnBusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.RegistrationPlate
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.VehicleModel
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Vin

private val faker = Faker()

fun randomVehicleModel(): VehicleModel {
    val maker = faker.resolve("vehicle.makes")

    return VehicleModel.from(
        maker = maker,
        name = faker.resolve("vehicle.models_by_make.$maker")
    ).getOrHandle(::failOnBusinessError)
}

fun registrationPlate(
    series: String = faker.regexify("[АВЕКМНОРСТУХ]{3}"),
    number: String = faker.numerify("1##"),
    regionCode: String = faker.numerify("1#"),
) = RegistrationPlate
    .from(series, number, regionCode)
    .getOrHandle(::failOnBusinessError)

fun vin(code: String = faker.numerify("KMHJU##VCBU######")) = Vin
    .from(code)
    .getOrHandle(::failOnBusinessError)

fun randomDistance() = faker.randomDouble(fractionalPartSize = 1).toKilometers()

fun Double.toKilometers() = Distance
    .ofKilometers(toBigDecimal())
    .getOrHandle(::failOnBusinessError)

fun randomPrice() = faker.randomDouble(fractionalPartSize = 2).toPrice()

fun Double.toPrice() = Price
    .from(toBigDecimal())
    .getOrHandle(::failOnBusinessError)

private fun Faker.randomDouble(fractionalPartSize: Int = 1, minValue: Int = 1, maxValue: Int = 10_000) =
    number().randomDouble(fractionalPartSize, minValue, maxValue)