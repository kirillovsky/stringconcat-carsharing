package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError

class Capacity(val value: Int) : ValueObject {
    companion object {
        fun from(value: Int): Either<IllegalCapacityValueError, Capacity> =
            if (value <= 0) IllegalCapacityValueError.left()
            else Capacity(value).right()

        fun five(): Capacity = Capacity(value = 5)
    }

    object IllegalCapacityValueError : BusinessError
}