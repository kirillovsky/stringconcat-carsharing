package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject

data class PurchasingVehicleId(val value: Long) : ValueObject

fun interface PurchasingVehicleIdGenerator {
    fun generate(): PurchasingVehicleId
}