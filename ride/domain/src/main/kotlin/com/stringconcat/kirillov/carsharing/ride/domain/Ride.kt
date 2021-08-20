package com.stringconcat.kirillov.carsharing.ride.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.base.AggregateRoot
import com.stringconcat.kirillov.carsharing.commons.types.error.BusinessError
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Price
import com.stringconcat.kirillov.carsharing.ride.domain.RideStartingError.CustomerIsRejected
import com.stringconcat.kirillov.carsharing.ride.domain.RideStartingError.VehicleAlreadyInRent
import com.stringconcat.kirillov.carsharing.ride.domain.RideStartingError.VehicleNotInRentalPool
import com.stringconcat.kirillov.carsharing.ride.domain.RideStatus.FINISHED
import com.stringconcat.kirillov.carsharing.ride.domain.RideStatus.PAID
import com.stringconcat.kirillov.carsharing.ride.domain.RideStatus.STARTED
import java.time.OffsetDateTime

class Ride internal constructor(
    id: RideId,
    val customerId: RideCustomerId,
    val vehicleId: RideVehicleId,
    val startDateTime: OffsetDateTime,
) : AggregateRoot<RideId>(id) {
    var status: RideStatus = STARTED
        internal set
    lateinit var coveredDistance: Distance
        internal set
    lateinit var finishDateTime: OffsetDateTime
        internal set
    lateinit var paidPrice: Price
        internal set

    fun isFinished(): Boolean = status == FINISHED || status == PAID

    fun finish(finishDateTime: OffsetDateTime, coveredDistance: Distance): Either<RideAlreadyFinishedError, Unit> {
        if (status != STARTED) return RideAlreadyFinishedError.left()

        status = FINISHED
        this.finishDateTime = finishDateTime
        this.coveredDistance = coveredDistance

        addEvent(RideFinishedEvent(rideId = id))
        return Unit.right()
    }

    fun pay(price: Price): Either<RideNotInFinishStatusError, Unit> {
        if (status != FINISHED) return RideNotInFinishStatusError.left()

        status = PAID
        paidPrice = price
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
            customer.isRejected -> CustomerIsRejected.left()
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
    object CustomerIsRejected : RideStartingError()
    object VehicleNotInRentalPool : RideStartingError()
    object VehicleAlreadyInRent : RideStartingError()
}

object RideAlreadyFinishedError : BusinessError
object RideNotInFinishStatusError : BusinessError