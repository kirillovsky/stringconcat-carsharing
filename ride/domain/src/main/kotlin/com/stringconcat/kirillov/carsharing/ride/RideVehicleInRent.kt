package com.stringconcat.kirillov.carsharing.ride

fun interface RideVehicleInRent {
    fun check(vehicleId: RideVehicleId): Boolean
}
