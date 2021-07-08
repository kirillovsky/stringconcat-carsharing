package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent

object PurchasingDepartmentEvents {
    data class VehicleAddedToPurchasingBalance(
        val vehicleId: PurchasingVehicleId,
    ) : DomainEvent()
}
