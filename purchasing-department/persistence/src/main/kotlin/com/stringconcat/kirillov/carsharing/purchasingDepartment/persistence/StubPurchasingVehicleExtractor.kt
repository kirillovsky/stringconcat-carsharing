package com.stringconcat.kirillov.carsharing.purchasingDepartment.persistence

import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle
import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicleId
import com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase.PurchasingVehicleExtractor

class StubPurchasingVehicleExtractor : PurchasingVehicleExtractor {
    override fun getAll(): List<PurchasingVehicle> = emptyList()

    override fun getById(id: PurchasingVehicleId): PurchasingVehicle? = null
}