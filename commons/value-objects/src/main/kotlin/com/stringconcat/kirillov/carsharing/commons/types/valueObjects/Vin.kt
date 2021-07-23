package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVinError.IllegalCharacterContains
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateVinError.InvalidCodeLength

private const val VIN_CODE_LENGTH = 17
private val VIN_CODE_ILLEGAL_CHARS = "[^0-9A-HJ-NPR-Za-hj-npr-z]".toRegex()

data class Vin internal constructor(val code: String) : ValueObject {
    companion object {
        fun from(code: String): Either<CreateVinError, Vin> {
            val normalizedCode = code.uppercase()

            return when {
                normalizedCode.length != VIN_CODE_LENGTH -> InvalidCodeLength.left()
                normalizedCode.contains(VIN_CODE_ILLEGAL_CHARS) -> IllegalCharacterContains.left()
                else -> Vin(code = normalizedCode).right()
            }
        }
    }
}

sealed class CreateVinError : BusinessError {
    object InvalidCodeLength : CreateVinError()
    object IllegalCharacterContains : CreateVinError()
}
