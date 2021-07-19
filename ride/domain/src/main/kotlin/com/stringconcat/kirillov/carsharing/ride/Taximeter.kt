package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price

fun interface Taximeter {
    fun calculatePrice(ride: Ride): Price
}