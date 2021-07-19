package com.stringconcat.kirillov.carsharing.ride

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.CustomerIsNotVerified
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.VehicleAlreadyInRent
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.VehicleNotInRentalPool
import com.stringconcat.kirillov.carsharing.ride.RideStatus.FINISHED
import com.stringconcat.kirillov.carsharing.ride.RideStatus.PAID
import com.stringconcat.kirillov.carsharing.ride.RideStatus.STARTED
import java.time.OffsetDateTime

class Ride internal constructor(
    id: RideId,
    val customerId: RideCustomerId,
    val vehicleId: RideVehicleId,
    val startDateTime: OffsetDateTime,
) : AggregateRoot<RideId>(id) {
    var status: RideStatus = STARTED
        internal set
    var coveredDistance: Distance? = null
        internal set
    var finishDateTime: OffsetDateTime? = null
        internal set
    var paidPrice: Price? = null
        internal set

    fun finish(finishDateTime: OffsetDateTime, coveredDistance: Distance): Either<Any, Unit> {
        if (status != STARTED) return RideFinishingError.left()

        status = FINISHED
        this.finishDateTime = finishDateTime
        this.coveredDistance = coveredDistance

        addEvent(RideFinishedEvent(rideId = id))
        return Unit.right()
    }

    fun pay(taximeter: Taximeter): Either<Any, Unit> {
        if (status != FINISHED) return RidePaidError.left()

        status = PAID
        paidPrice = taximeter.calculatePrice(ride = this)

        addEvent(RidePaidEvent(rideId = id))

        return Unit.right()
    }

    companion object {
        fun startRide(
            vehicleInRent: RideVehicleInRent,
            idGenerator: RideIdGenerator,
            customer: RideCustomer,
            vehicle: RideVehicle,
            startDateTime: OffsetDateTime,
        ): Either<RideStartingError, Ride> = when {
            !customer.isVerified -> CustomerIsNotVerified.left()
            !vehicle.isInRentalPool -> VehicleNotInRentalPool.left()
            vehicleInRent.check(vehicle.id) -> VehicleAlreadyInRent.left()
            else -> Ride(
                id = idGenerator.generate(),
                customerId = customer.id,
                vehicleId = vehicle.id,
                startDateTime
            ).apply {
                addEvent(RideStartedEvent(rideId = id))
            }.right()
        }
    }
}

enum class RideStatus {
    STARTED, FINISHED, PAID
}

sealed class RideStartingError : BusinessError {
    object CustomerIsNotVerified : RideStartingError()
    object VehicleNotInRentalPool : RideStartingError()
    object VehicleAlreadyInRent : RideStartingError()
}

object RideFinishingError : BusinessError
object RidePaidError : BusinessError