package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.customer.DriverLicenseNumberError.IllegalNumber
import com.stringconcat.kirillov.carsharing.customer.DriverLicenseNumberError.IllegalSeries

private val SERIES_FORMAT = "[1-9][0-9] [0-9]{2}".toRegex()
private val NUMBER_FORMAT = "[0-9]{6}".toRegex()

data class DriverLicenseNumber(val series: String, val number: String) : ValueObject {
    companion object {
        fun from(series: String, number: String): Either<DriverLicenseNumberError, DriverLicenseNumber> {
            return when {
                SERIES_FORMAT.notMatches(series) -> IllegalSeries.left()
                NUMBER_FORMAT.notMatches(number) || number == "000000" -> IllegalNumber.left()
                else -> DriverLicenseNumber(series, number).right()
            }
        }
    }
}

sealed class DriverLicenseNumberError : BusinessError {
    object IllegalSeries : DriverLicenseNumberError()
    object IllegalNumber : DriverLicenseNumberError()
}