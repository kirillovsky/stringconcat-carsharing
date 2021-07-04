package com.stringconcat.kirillov.carsharing.purchasingDepartment.domain

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent

data class VehicleAddedToPurchasingBalanceEvent(
    val purchasingVehicleId: PurchasingVehicleId,
) : DomainEvent()
