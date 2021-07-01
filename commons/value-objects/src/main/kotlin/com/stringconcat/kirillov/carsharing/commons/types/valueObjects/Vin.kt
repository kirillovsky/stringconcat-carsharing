package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

private const val VIN_CODE_LENGTH = 17
private val VIN_CODE_ILLEGAL_CHARS = "[^0-9A-HJ-NPR-Za-hj-npr-z]".toRegex()

data class Vin internal constructor(val code: String) : ValueObject {
    companion object {
        fun from(code: String): Result<Vin> {
            val normalizedCode = code.uppercase()

            return when {
                normalizedCode.length != VIN_CODE_LENGTH -> failure(InvalidCodeLength)
                normalizedCode.contains(VIN_CODE_ILLEGAL_CHARS) -> failure(IllegalCharacterContains)
                else -> success(Vin(code = normalizedCode))
            }
        }
    }

    object InvalidCodeLength : BusinessError()
    object IllegalCharacterContains : BusinessError()
}