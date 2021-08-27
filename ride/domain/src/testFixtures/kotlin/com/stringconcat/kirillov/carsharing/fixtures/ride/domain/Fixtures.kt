package com.stringconcat.kirillov.carsharing.fixtures.ride.domain

import arrow.core.Either
import arrow.core.getOrHandle
import com.stringconcat.kirillov.carsharing.commons.types.error.failOnBusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.ride.domain.CalculationRidePriceError
import com.stringconcat.kirillov.carsharing.ride.domain.Ride
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId
import com.stringconcat.kirillov.carsharing.ride.domain.RideId
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicle
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.domain.Taximeter
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