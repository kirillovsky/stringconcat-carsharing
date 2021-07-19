package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toKilometers
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.toPrice
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.CustomerIsNotVerified
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.VehicleAlreadyInRent
import com.stringconcat.kirillov.carsharing.ride.RideStartingError.VehicleNotInRentalPool
import com.stringconcat.kirillov.carsharing.ride.RideStatus.FINISHED
import com.stringconcat.kirillov.carsharing.ride.RideStatus.PAID
import com.stringconcat.kirillov.carsharing.ride.RideStatus.STARTED
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Clock.fixed
import java.time.Clock.systemDefaultZone
import java.time.OffsetDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class RideTest {
    private val someIdGenerator = RideIdGenerator { rideId() }
    private val validCustomer = rideCustomer(verified = true)
    private val validVehicle = rideVehicle(inRentalPool = true)
    private val clock = systemDefaultZone()

    private val noOneVehicleInRent = RideVehicleInRent { false }

    @Test
    fun `should start ride`() {
        val expectedId = rideId()
        val expectedStartDateTime = OffsetDateTime.now()

        val ride = Ride.startRide(
            vehicleInRent = noOneVehicleInRent,
            idGenerator = { expectedId },
            customer = validCustomer,
            vehicle = validVehicle,
            clock = expectedStartDateTime.asFixedClock()
        )

        ride shouldBeRight {
            it.id shouldBe expectedId
            it.customerId shouldBe validCustomer.id
            it.vehicleId shouldBe validVehicle.id
            it.startDateTime shouldBe expectedStartDateTime
            it.status shouldBe STARTED
            it.endDateTime shouldBe null
            it.coveredDistance shouldBe null
            it.paidPrice shouldBe null
            it.popEvents().shouldContainExactly(RideStartedEvent(rideId = expectedId))
        }
    }

    @Test
    fun `shouldn't start ride if customer is not verified`() {
        val result = Ride.startRide(
            vehicleInRent = noOneVehicleInRent,
            idGenerator = someIdGenerator,
            customer = rideCustomer(verified = false),
            vehicle = validVehicle,
            clock = clock
        )

        result shouldBeLeft {
            it shouldBe CustomerIsNotVerified
        }
    }

    @Test
    fun `shouldn't start ride if vehicle not in rental pool`() {
        val result = Ride.startRide(
            vehicleInRent = noOneVehicleInRent,
            idGenerator = someIdGenerator,
            customer = validCustomer,
            vehicle = rideVehicle(inRentalPool = false),
            clock = clock
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
            clock
        )

        result shouldBeLeft {
            it shouldBe VehicleAlreadyInRent
        }
    }

    @Test
    fun `started ride can be finished`() {
        val expectedRideId = rideId()
        val ride = ride(id = expectedRideId, status = STARTED)
        val expectedEndDateTime = OffsetDateTime.MAX
        val expectedCoveredDistance = 101.0.toKilometers()

        val operationResult = ride.finish(
            clock = expectedEndDateTime.asFixedClock(),
            coveredDistance = expectedCoveredDistance
        )

        operationResult.shouldBeRight()
        ride should {
            it.status shouldBe FINISHED
            it.endDateTime shouldBe expectedEndDateTime
            it.coveredDistance shouldBe expectedCoveredDistance
            it.popEvents().shouldContainExactly(RideFinishedEvent(rideId = expectedRideId))
        }
    }

    @ParameterizedTest(name = "ride should not be finished if status already is {0}")
    @EnumSource(names = ["FINISHED", "PAID"])
    fun `ride should not be finished if status already is`(status: RideStatus) {
        val endDateTime = OffsetDateTime.MAX
        val coveredDistance = 1.0.toKilometers()
        val ride = ride(
            status = status,
            endDateTime = endDateTime,
            coveredDistance = coveredDistance
        )

        val operationResult = ride.finish(clock, coveredDistance = 111.0.toKilometers())

        operationResult.shouldBeLeft(RideFinishingError)
        ride should {
            it.status shouldBe status
            it.endDateTime shouldBe endDateTime
            it.coveredDistance shouldBe coveredDistance
            it.popEvents() shouldBe emptyList()
        }
    }

    @Test
    fun `finished price can be paid`() {
        val expectedRideId = rideId()
        val expectedPaidPrice = 102.10.toPrice()
        val ride = ride(
            id = expectedRideId,
            status = FINISHED
        )

        val operationResult = ride.pay(taximeter = { expectedPaidPrice })

        operationResult.shouldBeRight()
        ride should {
            it.status shouldBe PAID
            it.paidPrice shouldBe expectedPaidPrice
            it.popEvents().shouldContainExactly(RidePaidEvent(rideId = expectedRideId))
        }
    }

    @Test
    fun `started ride shouldn't be paid`() {
        val ride = ride(status = STARTED)

        val operationResult = ride.pay(taximeter = { 100500.0.toPrice() })

        operationResult.shouldBeLeft(RidePaidError)
        ride should {
            it.status shouldBe STARTED
            it.paidPrice shouldBe null
            it.popEvents() shouldBe emptyList()
        }
    }

    @Test
    fun `paid ride shouldn't will pay again`() {
        val initialPaidPrice = 300.0.toPrice()
        val ride = ride(status = PAID, paidPrice = initialPaidPrice)

        val operationResult = ride.pay(taximeter = { 100500.0.toPrice() })

        operationResult.shouldBeLeft(RidePaidError)
        ride should {
            it.status shouldBe PAID
            it.paidPrice shouldBe initialPaidPrice
            it.popEvents() shouldBe emptyList()
        }
    }
}

private fun OffsetDateTime.asFixedClock(): Clock =
    fixed(toInstant(), offset.normalized())