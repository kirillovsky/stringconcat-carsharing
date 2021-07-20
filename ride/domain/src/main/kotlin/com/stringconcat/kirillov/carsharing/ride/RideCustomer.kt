package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject

class RideCustomer(id: RideCustomerId) : AggregateRoot<RideCustomerId>(id) {
    var isRejected: Boolean = false
        internal set

    fun reject() {
        isRejected = true
    }
}

data class RideCustomerId(val value: Long) : ValueObject