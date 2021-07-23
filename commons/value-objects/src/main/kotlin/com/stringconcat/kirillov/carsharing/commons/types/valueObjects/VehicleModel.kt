package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVehicleModelError.EmptyOrBlankMaker
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVehicleModelError.EmptyOrBlankName

data class VehicleModel internal constructor(
    val name: String,
    val maker: String,
) : ValueObject {
    companion object {
        fun from(name: String, maker: String): Either<CreateVehicleModelError, VehicleModel> =
            when {
                name.isEmptyOrBlank() -> EmptyOrBlankName.left()
                maker.isEmptyOrBlank() -> EmptyOrBlankMaker.left()
                else -> VehicleModel(name, maker).right()
            }
    }
}

sealed class CreateVehicleModelError : BusinessError {
    object EmptyOrBlankName : CreateVehicleModelError()
    object EmptyOrBlankMaker : CreateVehicleModelError()
}

private fun String.isEmptyOrBlank() = isEmpty() || isBlank()