package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject

class RideVehicle(id: RideVehicleId) : AggregateRoot<RideVehicleId>(id) {
    var isInRentalPool: Boolean = true
        internal set

    fun addToRentalPool() {
        isInRentalPool = true
    }

    fun removeFromRentalPool() {
        isInRentalPool = false
    }
}

data class RideVehicleId(val value: Long) : ValueObject