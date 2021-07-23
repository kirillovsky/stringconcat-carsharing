package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import java.time.OffsetDateTime
import kotlin.random.Random.Default.nextLong

fun rideCustomer(rejected: Boolean = false) =
    RideCustomer(id = rideCustomerId()).apply {
        isRejected = rejected
    }

fun rideCustomerId() = RideCustomerId(value = nextLong())

fun rideVehicle(inRentalPool: Boolean = false) =
    RideVehicle(id = rideVehicleId()).apply {
        isInRentalPool = inRentalPool
    }

fun rideVehicleId() = RideVehicleId(value = nextLong())

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

fun rideId() = RideId(value = nextLong())