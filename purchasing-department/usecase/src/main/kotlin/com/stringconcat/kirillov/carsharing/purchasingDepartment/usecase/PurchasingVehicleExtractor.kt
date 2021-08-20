package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleId

interface PurchasingVehicleExtractor {
    fun getAll(): List<PurchasingVehicle>

    fun getById(id: PurchasingVehicleId): PurchasingVehicle?
}