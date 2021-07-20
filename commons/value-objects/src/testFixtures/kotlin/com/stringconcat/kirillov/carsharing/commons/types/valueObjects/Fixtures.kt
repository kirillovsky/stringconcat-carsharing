package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import arrow.core.Either
import com.github.javafaker.Faker

private val faker = Faker()

fun vehicleModel(): VehicleModel {
    val maker = faker.resolve("vehicle.makes")

    val result = VehicleModel.from(
        maker = maker,
        name = faker.resolve("vehicle.models_by_make.$maker")
    )

    check(result is Either.Right)

    return result.b
}

fun registrationPlate(
    series: String = faker.regexify("[АВЕКМНОРСТУХ]{3}"),
    number: String = faker.numerify("1##"),
    regionCode: String = faker.numerify("1#"),
): RegistrationPlate {
    val result = RegistrationPlate.from(series, number, regionCode)

    check(result is Either.Right)

    return result.b
}

fun vin(code: String = faker.numerify("KMHJU##VCBU######")): Vin {
    val result = Vin.from(code)

    check(result is Either.Right)

    return result.b
}

fun randomDistance() = faker.randomDouble(fractionalPartSize = 1).toKilometers()

fun Double.toKilometers(): Distance {
    val result = Distance.ofKilometers(this.toBigDecimal())

    check(result is Either.Right)

    return result.b
}

fun randomPrice() = faker.randomDouble(fractionalPartSize = 2).toPrice()

fun Double.toPrice(): Price {
    val result = Price.from(this.toBigDecimal())

    check(result is Either.Right)

    return result.b
}

private fun Faker.randomDouble(fractionalPartSize: Int = 1, minValue: Int = 1, maxValue: Int = 10_000) =
    number().randomDouble(fractionalPartSize, minValue, maxValue)