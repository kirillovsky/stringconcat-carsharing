package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

data class VehicleModel(val name: String, val maker: String) {
    companion object {
        fun from(name: String, maker: String): Result<VehicleModel> =
            when {
                name.isEmptyOrBlank() -> failure(EmptyOrBlankName)
                maker.isEmptyOrBlank() -> failure(EmptyOrBlankMaker)
                else -> success(VehicleModel(name, maker))
            }
    }

    object EmptyOrBlankName : BusinessError()
    object EmptyOrBlankMaker : BusinessError()
}

private fun String.isEmptyOrBlank() = isEmpty() || isBlank()