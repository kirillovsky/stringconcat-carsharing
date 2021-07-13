package com.stringconcat.kirillov.carsharing.customer

import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject

data class CustomerId(val value: Long) : ValueObject

fun interface CustomerIdGenerator {
    fun generate(): CustomerId
}