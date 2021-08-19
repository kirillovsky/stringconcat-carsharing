package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.ride.RidePaidEvent
import com.stringconcat.kirillov.carsharing.ride.RideStatus.PAID
import com.stringconcat.kirillov.carsharing.ride.RideStatus.STARTED
import com.stringconcat.kirillov.carsharing.ride.finishedRide
import com.stringconcat.kirillov.carsharing.ride.paidRide
import com.stringconcat.kirillov.carsharing.ride.randomRideId
import com.stringconcat.kirillov.carsharing.ride.startedRide
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideRepository
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.PayRideUseCaseError.RideNotFound
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.PayRideUseCaseError.RideNotInFinishStatusError
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

internal class PayRideUseCaseTest {
    private val rideId = randomRideId()
    private val price = randomPrice()

    @Test
    fun `should pay finished ride`() {
        val persister = InMemoryRideRepository()
        val usecase = PayRideUseCase(
            extractor = InMemoryRideRepository().apply {
                put(rideId, finishedRide(id = rideId))
            },
            persister = persister
        )

        val result = usecase.execute(PayRideRequest(rideId, price))

        result.shouldBeRight()
        persister[rideId] should {
            it.shouldNotBeNull()
            it.status shouldBe PAID
            it.paidPrice shouldBe price
            it.popEvents().shouldContainExactly(RidePaidEvent(rideId = rideId))
        }
    }

    @Test
    fun `shouldn't pay ride if ride not found`() {
        val persister = InMemoryRideRepository()
        val usecase = PayRideUseCase(
            extractor = InMemoryRideRepository(),
            persister = persister
        )

        val result = usecase.execute(PayRideRequest(rideId, price))

        result shouldBeLeft RideNotFound
        persister[rideId].shouldBeNull()
    }

    @TestFactory
    fun `shouldn't pay ride if ride not in finished status`(): List<DynamicTest> =
        listOf(
            STARTED to { startedRide(rideId) },
            PAID to { paidRide(rideId) }
        ).map { (status, rideProvider) ->
            dynamicTest("shouldn't pay ride if ride is $status") {
                val ride = rideProvider()
                val persister = InMemoryRideRepository()
                val usecase = PayRideUseCase(
                    extractor = InMemoryRideRepository().apply {
                        put(rideId, ride)
                    },
                    persister = persister
                )

                val result = usecase.execute(PayRideRequest(rideId, price))

                result shouldBeLeft RideNotInFinishStatusError
                persister[rideId].shouldBeNull()
            }
        }
}