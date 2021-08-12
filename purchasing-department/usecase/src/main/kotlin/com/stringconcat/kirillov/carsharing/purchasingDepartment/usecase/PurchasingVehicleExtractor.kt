package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle

fun interface PurchasingVehicleExtractor {
    fun getAll(): List<PurchasingVehicle>
}