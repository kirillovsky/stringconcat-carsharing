package com.stringconcat.kirillov.carsharing.commons.types.valueObjects

import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

private val NUMBER_FORMAT_PATTERN = "^[0-9]{3}$".toRegex()
private val SERIES_FORMAT_PATTERN = "^[АВЕКМНОРСТУХ]{3}$".toRegex()
private val REGION_CODE_FORMAT_PATTERN = "^[1-9]?([0-9]{2})$".toRegex()

data class RegistrationPlate internal constructor(
    val series: String,
    val number: String,
    val regionCode: String,
) : ValueObject {
    companion object {
        fun from(series: String, number: String, regionCode: String): Result<RegistrationPlate> {
            val normalizedSeries = series.uppercase()

            return when {
                number == "000" -> failure(InvalidNumberFormat)
                number notMatches NUMBER_FORMAT_PATTERN -> failure(InvalidNumberFormat)
                regionCode == "00" -> failure(InvalidRegionCodeFormat)
                regionCode notMatches REGION_CODE_FORMAT_PATTERN -> failure(InvalidRegionCodeFormat)
                normalizedSeries notMatches SERIES_FORMAT_PATTERN -> failure(InvalidSeriesFormat)
                else -> success(RegistrationPlate(normalizedSeries, number, regionCode))
            }
        }
    }

    object InvalidNumberFormat : BusinessError()
    object InvalidSeriesFormat : BusinessError()
    object InvalidRegionCodeFormat : BusinessError()
}

private infix fun CharSequence.notMatches(regex: Regex): Boolean = !matches(regex)