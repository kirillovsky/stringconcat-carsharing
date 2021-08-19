package com.stringconcat.kirillov.carsharing.ride

import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toPrice
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.CustomerIsRejected
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.VehicleAlreadyInRent
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.VehicleNotInRentalPool
import com.stringconcat.kirillov.carsharing.ride.RideStatus.FINISHED
import com.stringconcat.kirillov.carsharing.ride.RideStatus.PAID
import com.stringconcat.kirillov.carsharing.ride.RideStatus.STARTED
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime.MAX
import java.time.OffsetDateTime.now
import org.junit.jupiter.api.Test

internal class RideTest {
    private val someIdGenerator = RideIdGenerator { randomRideId() }
    private val validCustomer = rideCustomer(rejected = false)
    private val validVehicle = rideVehicle(inRentalPool = true)

    private val noOneVehicleInRent = RideVehicleInRent { false }

    @Test
    fun `should start ride`() {
        val expectedId = randomRideId()
        val expectedStartDateTime = now()

        val ride = Ride.startRide(
            vehicleInRent = noOneVehicleInRent,
            idGenerator = { expectedId },
            customer = validCustomer,
            vehicle = validVehicle,
            startDateTime = expectedStartDateTime
        )

        ride shouldBeRight {
            it.id shouldBe expectedId
            it.customerId shouldBe validCustomer.id
            it.vehicleId shouldBe validVehicle.id
            it.startDateTime shouldBe expectedStartDateTime
            it.status shouldBe STARTED
            it.popEvents().shouldContainExactly(RideStartedEvent(rideId = expectedId))
        }
    }

    @Test
    fun `shouldn't start ride if customer is not verified`() {
        val result = Ride.startRide(
            vehicleInRent = noOneVehicleInRent,
            idGenerator = someIdGenerator,
            customer = rideCustomer(rejected = true),
            vehicle = validVehicle,
            startDateTime = now()
        )

        result shouldBeLeft {
            it shouldBe CustomerIsRejected
        }
    }

    @Test
    fun `shouldn't start ride if vehicle not in rental pool`() {
        val result = Ride.startRide(
            vehicleInRent = noOneVehicleInRent,
            idGenerator = someIdGenerator,
            customer = validCustomer,
            vehicle = rideVehicle(inRentalPool = false),
            startDateTime = now()
        )

        result shouldBeLeft {
            it shouldBe VehicleNotInRentalPool
        }
    }

    @Test
    fun `shouldn't start ride if vehicle already in rent`() {
        val rentalVehicleId = validVehicle.id
        val result = Ride.startRide(
            vehicleInRent = { it == rentalVehicleId },
            idGenerator = someIdGenerator,
            customer = validCustomer,
            vehicle = validVehicle,
            startDateTime = now()
        )

        result shouldBeLeft {
            it shouldBe VehicleAlreadyInRent
        }
    }

    @Test
    fun `started ride can be finished`() {
        val expectedRideId = randomRideId()
        val ride = startedRide(id = expectedRideId)
        val expectedEndDateTime = now()
        val expectedCoveredDistance = 101.0.toKilometers()

        val operationResult = ride.finish(
            finishDateTime = expectedEndDateTime,
            coveredDistance = expectedCoveredDistance
        )

        operationResult.shouldBeRight()
        ride should {
            it.status shouldBe FINISHED
            it.finishDateTime shouldBe expectedEndDateTime
            it.coveredDistance shouldBe expectedCoveredDistance
            it.popEvents().shouldContainExactly(RideFinishedEvent(rideId = expectedRideId))
        }
    }

    @Test
    fun `ride shouldn't be finished if it already finished`() {
        val endDateTime = MAX
        val coveredDistance = 1.0.toKilometers()
        val ride = finishedRide(
            finishedDateTime = endDateTime,
            coveredDistance = coveredDistance,
        )

        val operationResult = ride.finish(finishDateTime = now(), coveredDistance = randomDistance())

        operationResult.shouldBeLeft(RideFinishingError)
        ride should {
            it.status shouldBe FINISHED
            it.finishDateTime shouldBe endDateTime
            it.coveredDistance shouldBe coveredDistance
            it.popEvents() shouldBe emptyList()
        }
    }

    @Test
    fun `ride shouldn't be finished if it already paid`() {
        val endDateTime = MAX
        val coveredDistance = 1.0.toKilometers()
        val paidPrice = randomPrice()
        val ride = paidRide(
            finishedDateTime = endDateTime,
            coveredDistance = coveredDistance,
            paidPrice = paidPrice
        )

        val operationResult = ride.finish(finishDateTime = now(), coveredDistance = randomDistance())

        operationResult.shouldBeLeft(RideFinishingError)
        ride should {
            it.status shouldBe PAID
            it.finishDateTime shouldBe endDateTime
            it.coveredDistance shouldBe coveredDistance
            it.paidPrice shouldBe paidPrice
            it.popEvents() shouldBe emptyList()
        }
    }

    @Test
    fun `finished price can be paid`() {
        val expectedRideId = randomRideId()
        val expectedPaidPrice = 102.10.toPrice()
        val ride = finishedRide(id = expectedRideId)
        val taximeter = StubTaximeter(result = expectedPaidPrice.right())
        val operationResult = ride.pay(taximeter)

        operationResult.shouldBeRight()
        ride should {
            it.status shouldBe PAID
            it.paidPrice shouldBe expectedPaidPrice
            it.popEvents().shouldContainExactly(RidePaidEvent(rideId = expectedRideId))
        }
        taximeter.receivedRide shouldBe ride
    }

    @Test
    fun `price shouldn't be paid on calculation price error`() {
        val finishDateTime = now()
        val coveredDistance = randomDistance()
        val ride = finishedRide(finishedDateTime = finishDateTime, coveredDistance = coveredDistance)
        val taximeter = StubTaximeter(result = CalculationRidePriceError.left())
        val operationResult = ride.pay(taximeter)

        operationResult.shouldBeLeft(RidePaidError)
        ride should {
            it.status shouldBe FINISHED
            it.finishDateTime shouldBe finishDateTime
            it.coveredDistance shouldBe coveredDistance
            it.popEvents() shouldBe emptyList()
        }
        taximeter.receivedRide shouldBe ride
    }

    @Test
    fun `started ride shouldn't be paid`() {
        val ride = startedRide()
        val taximeter = StubTaximeter(result = randomPrice().right())
        val operationResult = ride.pay(taximeter)

        operationResult.shouldBeLeft(RidePaidError)
        ride should {
            it.status shouldBe STARTED
            it.popEvents() shouldBe emptyList()
        }
        taximeter.receivedRide.shouldBeNull()
    }

    @Test
    fun `paid ride shouldn't will pay again`() {
        val initialPaidPrice = 300.0.toPrice()
        val ride = paidRide(paidPrice = initialPaidPrice)
        val taximeter = StubTaximeter(result = randomPrice().right())
        val operationResult = ride.pay(taximeter)

        operationResult.shouldBeLeft(RidePaidError)
        ride should {
            it.status shouldBe PAID
            it.paidPrice shouldBe initialPaidPrice
            it.popEvents() shouldBe emptyList()
        }
        taximeter.receivedRide.shouldBeNull()
    }

    @Test
    fun `isFinished should be true for paid ride`() {
        paidRide().isFinished() shouldBe true
    }

    @Test
    fun `isFinished should be true for finished ride`() {
        finishedRide().isFinished() shouldBe true
    }
}