package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject

data class RideId(val value: Long) : ValueObject
fun interface RideIdGenerator {
    fun generate(): RideId
}