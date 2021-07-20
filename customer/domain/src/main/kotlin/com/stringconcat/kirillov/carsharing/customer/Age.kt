package com.stringconcat.kirillov.carsharing.customer

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.customer.AgeCreationErrors.BirthDateMoreThanCurrentDate
import com.stringconcat.kirillov.carsharing.customer.AgeCreationErrors.IllegalYearsCount
import java.time.LocalDate
import java.time.Period

data class Age internal constructor(val yearsCount: Int) : ValueObject, Comparable<Age> {
    companion object {
        fun from(yearsCount: Int): Either<IllegalYearsCount, Age> =
            if (yearsCount < 0) {
                IllegalYearsCount.left()
            } else {
                Age(yearsCount).right()
            }

        fun from(birthDate: LocalDate, currentDate: LocalDate): Either<BirthDateMoreThanCurrentDate, Age> =
            if (birthDate > currentDate) {
                BirthDateMoreThanCurrentDate.left()
            } else {
                val lifePeriod = Period.between(birthDate, currentDate)

                Age(yearsCount = lifePeriod.years).right()
            }
    }

    override fun compareTo(other: Age): Int = yearsCount.compareTo(other.yearsCount)
}

object AgeCreationErrors {
    object IllegalYearsCount : BusinessError
    object BirthDateMoreThanCurrentDate : BusinessError
}