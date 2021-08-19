package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomDistance
import com.stringconcat.kirillov.carsharing.ride.RideFinishedEvent
import com.stringconcat.kirillov.carsharing.ride.RideStatus
import com.stringconcat.kirillov.carsharing.ride.finishedRide
import com.stringconcat.kirillov.carsharing.ride.randomRideId
import com.stringconcat.kirillov.carsharing.ride.startedRide
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideRepository
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.FinishRideUseCaseError.RideAlreadyFinishedError
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.FinishRideUseCaseError.RideNotFound
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.LocalDate.EPOCH
import java.time.LocalTime.MIDNIGHT
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import org.junit.jupiter.api.Test

internal class FinishRideUseCaseTest {
    private val coveredDistance = randomDistance()
    private val finishedDateTime = OffsetDateTime.of(EPOCH, MIDNIGHT, UTC)

    @Test
    fun `should finish started ride`() {
        val startedRide = startedRide()
        val persister = InMemoryRideRepository()
        val usecase = FinishRideUseCase(
            extractor = InMemoryRideRepository().apply {
                put(startedRide.id, startedRide)
            },
            persister = persister
        )

        val result = usecase.execute(FinishRideRequest(startedRide.id, finishedDateTime, coveredDistance))

        result.shouldBeRight()
        persister[startedRide.id] should {
            it.shouldNotBeNull()
            it.status shouldBe RideStatus.FINISHED
            it.finishDateTime shouldBe finishedDateTime
            it.coveredDistance shouldBe coveredDistance
            it.popEvents().shouldContainExactly(RideFinishedEvent(rideId = startedRide.id))
        }
    }

    @Test
    fun `shouldn't finish ride if ride not found`() {
        val persister = InMemoryRideRepository()
        val usecase = FinishRideUseCase(
            extractor = InMemoryRideRepository(),
            persister = persister
        )
        val rideId = randomRideId()
        val result = usecase.execute(FinishRideRequest(rideId = rideId, finishedDateTime, coveredDistance))

        result shouldBeLeft RideNotFound
        persister[rideId].shouldBeNull()
    }

    @Test
    fun `shouldn't finish ride if ride already finished`() {
        val finishedRide = finishedRide()
        val persister = InMemoryRideRepository()
        val usecase = FinishRideUseCase(
            extractor = InMemoryRideRepository().apply {
                put(finishedRide.id, finishedRide)
            },
            persister = persister
        )
        val result = usecase.execute(FinishRideRequest(rideId = finishedRide.id, finishedDateTime, coveredDistance))

        result shouldBeLeft RideAlreadyFinishedError
        persister[finishedRide.id].shouldBeNull()
    }
}