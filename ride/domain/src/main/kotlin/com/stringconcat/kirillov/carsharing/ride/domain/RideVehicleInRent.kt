package com.stringconcat.kirillov.carsharing.ride.domain

fun interface RideVehicleInRent {
    fun check(vehicleId: RideVehicleId): Boolean
}
