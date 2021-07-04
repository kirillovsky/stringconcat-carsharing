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

fun registrationPlate(): RegistrationPlate {
    val result = RegistrationPlate.from(
        series = "УУУ",
        number = "001",
        regionCode = "01"
    )

    check(result is Either.Right)

    return result.b
}

fun vin(): Vin {
    val result = Vin.from("KMHJU81VCBU266113")

    check(result is Either.Right)

    return result.b
}