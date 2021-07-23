package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateRegistrationPlateError.InvalidNumberFormat
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateRegistrationPlateError.InvalidRegionCodeFormat
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.CreateRegistrationPlateError.InvalidSeriesFormat

private val NUMBER_FORMAT_PATTERN = "^[0-9]{3}$".toRegex()
private val SERIES_FORMAT_PATTERN = "^[АВЕКМНОРСТУХ]{3}$".toRegex()
private val REGION_CODE_FORMAT_PATTERN = "^[1-9]?([0-9]{2})$".toRegex()

data class RegistrationPlate internal constructor(
    val series: String,
    val number: String,
    val regionCode: String,
) : ValueObject {
    companion object {
        fun from(
            series: String,
            number: String,
            regionCode: String,
        ): Either<CreateRegistrationPlateError, RegistrationPlate> {
            val normalizedSeries = series.uppercase()

            return when {
                number == "000" -> InvalidNumberFormat.left()
                number notMatches NUMBER_FORMAT_PATTERN -> InvalidNumberFormat.left()
                regionCode == "00" -> InvalidRegionCodeFormat.left()
                regionCode notMatches REGION_CODE_FORMAT_PATTERN -> InvalidRegionCodeFormat.left()
                normalizedSeries notMatches SERIES_FORMAT_PATTERN -> InvalidSeriesFormat.left()
                else -> RegistrationPlate(normalizedSeries, number, regionCode).right()
            }
        }
    }
}

sealed class CreateRegistrationPlateError : BusinessError {
    object InvalidNumberFormat : CreateRegistrationPlateError()
    object InvalidSeriesFormat : CreateRegistrationPlateError()
    object InvalidRegionCodeFormat : CreateRegistrationPlateError()
}

private infix fun CharSequence.notMatches(regex: Regex): Boolean = !matches(regex)