package com.stringconcat.kirillov.carsharing.ride

import arrow.core.getOrHandle
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.error.failOnBusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomPrice
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import kotlin.random.Random.Default.nextLong

fun rideCustomer(rejected: Boolean = false) =
    RideCustomer(id = rideCustomerId()).apply {
        if (rejected) reject()
    }

fun rideCustomerId() = RideCustomerId(value = nextLong())

fun rideVehicle(inRentalPool: Boolean = false) =
    RideVehicle(id = rideVehicleId()).apply {
        if (!inRentalPool) removeFromRentalPool()
    }

fun rideVehicleId() = RideVehicleId(value = nextLong())

fun startedRide(
    id: RideId = rideId(),
) = Ride
    .startRide(
        idGenerator = { id },
        vehicleInRent = { false },
        customer = rideCustomer(rejected = false),
        vehicle = rideVehicle(inRentalPool = true),
        startDateTime = now()
    ).getOrHandle(::failOnBusinessError).apply {
        popEvents()
    }

fun finishedRide(
    id: RideId = rideId(),
    finishedDateTime: OffsetDateTime = now(),
    coveredDistance: Distance = randomDistance(),
) = startedRide(id).apply {
    finish(finishedDateTime, coveredDistance).getOrHandle(::failOnBusinessError)

    popEvents()
}

fun paidRide(
    finishedDateTime: OffsetDateTime = now(),
    coveredDistance: Distance = randomDistance(),
    paidPrice: Price = randomPrice(),
) = finishedRide(
    finishedDateTime = finishedDateTime,
    coveredDistance = coveredDistance,
).apply {
    pay(taximeter = { paidPrice.right() }).getOrHandle(::failOnBusinessError)

    popEvents()
}

fun rideId() = RideId(value = nextLong())