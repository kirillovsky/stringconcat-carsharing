package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.base.ValueObject

class RideCustomer(id: RideCustomerId) : AggregateRoot<RideCustomerId>(id) {
    var isVerified = false
        internal set

    fun verify() {
        isVerified = true
    }

    fun reject() {
        isVerified = false
    }
}

data class RideCustomerId(val value: Long): ValueObject