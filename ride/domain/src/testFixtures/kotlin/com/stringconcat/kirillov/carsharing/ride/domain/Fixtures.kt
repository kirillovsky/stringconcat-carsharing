package com.stringconcat.kirillov.carsharing.ride.domain

import arrow.core.Either
import arrow.core.getOrHandle
import com.stringconcat.kirillov.carsharing.commons.types.error.failOnBusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomPrice
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import kotlin.random.Random.Default.nextLong

fun rideCustomer(
    id: RideCustomerId = randomRideCustomerId(),
    rejected: Boolean = false
) = RideCustomer(id).apply {
    if (rejected) reject()
}

fun randomRideCustomerId() = RideCustomerId(value = nextLong())

fun rideVehicle(
    id: RideVehicleId = randomRideVehicleId(),
    inRentalPool: Boolean = false
) = RideVehicle(id).apply {
    if (!inRentalPool) removeFromRentalPool()
}

fun randomRideVehicleId() = RideVehicleId(value = nextLong())

fun startedRide(
    id: RideId = randomRideId(),
    vehicleId: RideVehicleId = randomRideVehicleId()
) = Ride
    .startRide(
        idGenerator = { id },
        vehicleInRent = { false },
        customer = rideCustomer(rejected = false),
        vehicle = rideVehicle(id = vehicleId, inRentalPool = true),
        startDateTime = now()
    ).getOrHandle(::failOnBusinessError).apply {
        popEvents()
    }

fun finishedRide(
    id: RideId = randomRideId(),
    finishedDateTime: OffsetDateTime = now(),
    coveredDistance: Distance = randomDistance(),
) = startedRide(id).apply {
    finish(finishedDateTime, coveredDistance).getOrHandle(::failOnBusinessError)

    popEvents()
}

fun paidRide(
    id: RideId = randomRideId(),
    finishedDateTime: OffsetDateTime = now(),
    coveredDistance: Distance = randomDistance(),
    paidPrice: Price = randomPrice(),
) = finishedRide(
    id = id,
    finishedDateTime = finishedDateTime,
    coveredDistance = coveredDistance,
).apply {
    pay(paidPrice).getOrHandle(::failOnBusinessError)

    popEvents()
}

fun randomRideId() = RideId(value = nextLong())

class StubTaximeter(private val result: Either<CalculationRidePriceError, Price>) : Taximeter {
    var receivedRide: Ride? = null
        private set

    override fun calculatePrice(ride: Ride): Either<CalculationRidePriceError, Price> {
        receivedRide = ride
        return result
    }
}