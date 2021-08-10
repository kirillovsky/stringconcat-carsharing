package com.stringconcat.kirillov.carsharing.purchasingDepartment.usecase

import com.stringconcat.kirillov.carsharing.purchasingDepartment.domain.PurchasingVehicle

interface PurchasingVehiclePersister {
    fun save(vehicle: PurchasingVehicle)
}