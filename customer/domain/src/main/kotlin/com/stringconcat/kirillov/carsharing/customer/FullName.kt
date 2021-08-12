package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.customer.CreateFullNameError.InvalidFirstName
import com.stringconcat.kirillov.carsharing.customer.CreateFullNameError.InvalidMiddleName
import com.stringconcat.kirillov.carsharing.customer.CreateFullNameError.InvalidSecondName

private const val CYRILLIC_SYMBOLS_GROUP = "[А-ЯЁ]"
private val FIRST_NAME_PATTERN = "$CYRILLIC_SYMBOLS_GROUP+".toRegex()
private val MIDDLE_NAME_PATTERN = "$CYRILLIC_SYMBOLS_GROUP+".toRegex()
private val SECOND_NAME_PATTERN = "$CYRILLIC_SYMBOLS_GROUP+(-$CYRILLIC_SYMBOLS_GROUP+)?".toRegex()

data class FullName internal constructor(
    val firstName: String,
    val middleName: String?,
    val secondName: String,
) : ValueObject {
    companion object {
        fun from(
            firstName: String,
            middleName: String? = null,
            secondName: String,
        ): Either<CreateFullNameError, FullName> {
            val normalizedFirstName = firstName.normalize()
            val normalizedMiddleName = middleName?.normalize()
            val normalizedSecondName = secondName.normalize()

            return when {
                FIRST_NAME_PATTERN.notMatches(normalizedFirstName) -> InvalidFirstName.left()
                normalizedMiddleName != null && MIDDLE_NAME_PATTERN.notMatches(normalizedMiddleName) ->
                    InvalidMiddleName.left()
                SECOND_NAME_PATTERN.notMatches(normalizedSecondName) -> InvalidSecondName.left()
                else -> FullName(normalizedFirstName, normalizedMiddleName, normalizedSecondName).right()
            }
        }
    }
}

private fun String.normalize() = trim().uppercase()

sealed class CreateFullNameError : BusinessError {
    object InvalidFirstName : CreateFullNameError()
    object InvalidMiddleName : CreateFullNameError()
    object InvalidSecondName : CreateFullNameError()
}