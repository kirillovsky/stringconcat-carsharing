package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import arrow.core.Either

fun vehicleModel(): VehicleModel {
    val result = VehicleModel.from(
        name = "Toyota",
        maker = "Camry"
    )

    check(result is Either.Right)

    return result.b
}

fun registrationPlate(
    series: String = "ууу",
    number: String = "001",
    regionCode: String = "01"
): RegistrationPlate {
    val result = RegistrationPlate.from(series, number, regionCode)

    check(result is Either.Right)

    return result.b
}

fun vin(code: String = "KMHJU81VCBU266113"): Vin {
    val result = Vin.from(code)

    check(result is Either.Right)

    return result.b
}

fun Double.toKilometers(): Distance {
    val result = Distance.ofKilometers(this.toBigDecimal())

    check(result is Either.Right)

    return result.b
}

fun Double.toPrice(): Price {
    val result = Price.from(this.toBigDecimal())

    check(result is Either.Right)

    return result.b
}