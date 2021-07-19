package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import java.time.OffsetDateTime

fun rideCustomer(verified: Boolean = false) =
    RideCustomer(id = rideCustomerId()).apply {
        isVerified = verified
    }

fun rideCustomerId() = RideCustomerId(value = 101500L)

fun rideVehicle(inRentalPool: Boolean = false) =
    RideVehicle(id = rideVehicleId()).apply {
        isInRentalPool = inRentalPool
    }

fun rideVehicleId() = RideVehicleId(value = 111500L)

fun ride(
    id: RideId = rideId(),
    status: RideStatus = RideStatus.STARTED,
    endDateTime: OffsetDateTime? = null,
    coveredDistance: Distance? = null,
    paidPrice: Price? = null
) = Ride(
    id,
    customerId = rideCustomerId(),
    vehicleId = rideVehicleId(),
    startDateTime = OffsetDateTime.now()
).also {
    it.status = status
    it.finishDateTime = endDateTime
    it.coveredDistance = coveredDistance
    it.paidPrice = paidPrice
}

fun rideId() = RideId(value = 1005002L)