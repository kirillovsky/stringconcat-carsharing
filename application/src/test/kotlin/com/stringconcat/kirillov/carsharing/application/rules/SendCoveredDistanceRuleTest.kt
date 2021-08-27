package com.stringconcat.kirillov.carsharing.application.rules

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.Distance
import com.stringconcat.kirillov.carsharing.maintenance.domain.MaintenanceVehicleId
import com.stringconcat.kirillov.carsharing.maintenance.usecase.IncreaseVehicleMileage
import com.stringconcat.kirillov.carsharing.maintenance.usecase.IncreaseVehicleMileageUseCaseError
import com.stringconcat.kirillov.carsharing.maintenance.usecase.IncreaseVehicleMileageUseCaseError.VehicleNotFound
import com.stringconcat.kirillov.carsharing.ride.domain.RideFinishedEvent
import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.finishedRide
import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.randomRideId
import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.startedRide
import com.stringconcat.kirillov.carsharing.fixtures.ride.usecase.InMemoryRideRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SendCoveredDistanceRuleTest {
    @Test
    fun `should increase maintenance vehicle covered mileage by ride covered distance`() {
        val usecase = MockIncreaseVehicleMileageUseCase(result = Unit.right())
        val ride = finishedRide()
        val rideExtractor = InMemoryRideRepository().apply {
            put(ride.id, ride)
        }
        val rule = SendCoveredDistanceRule(
            increaseVehicleMileage = usecase,
            rideExtractor = rideExtractor
        )

        rule.handle(RideFinishedEvent(rideId = ride.id))

        usecase should {
            it.requestedId shouldBe MaintenanceVehicleId(ride.id.value)
            it.requestedAddendum shouldBe ride.coveredDistance
        }
    }

    @Test
    fun `shouldn't increase maintenance vehicle covered mileage if ride not found`() {
        val usecase = MockIncreaseVehicleMileageUseCase(result = Unit.right())
        val rule = SendCoveredDistanceRule(
            increaseVehicleMileage = usecase,
            rideExtractor = InMemoryRideRepository()
        )
        val rideId = randomRideId()

        val exception = shouldThrow<IllegalStateException> {
            rule.handle(RideFinishedEvent(rideId = rideId))
        }

        exception.message shouldBe "Unable to find ride by id=$rideId"
        usecase should {
            it.requestedId.shouldBeNull()
            it.requestedAddendum.shouldBeNull()
        }
    }

    @Test
    fun `shouldn't increase maintenance vehicle covered mileage if ride status is not finished`() {
        val usecase = MockIncreaseVehicleMileageUseCase(result = Unit.right())
        val startedRide = startedRide()
        val rule = SendCoveredDistanceRule(
            increaseVehicleMileage = usecase,
            rideExtractor = InMemoryRideRepository().apply {
                put(startedRide.id, startedRide)
            }
        )

        val exception = shouldThrow<IllegalStateException> {
            rule.handle(RideFinishedEvent(rideId = startedRide.id))
        }

        exception.message shouldBe "Found ride(id=${startedRide.id}) is not finished"
        usecase should {
            it.requestedId.shouldBeNull()
            it.requestedAddendum.shouldBeNull()
        }
    }

    @Test
    fun `should map increase vehicle mileage usecase error`() {
        val usecaseError = VehicleNotFound
        val usecase = MockIncreaseVehicleMileageUseCase(result = usecaseError.left())
        val finishedRide = finishedRide()
        val rule = SendCoveredDistanceRule(
            increaseVehicleMileage = usecase,
            rideExtractor = InMemoryRideRepository().apply {
                put(finishedRide.id, finishedRide)
            }
        )

        val exception = shouldThrow<IllegalStateException> {
            rule.handle(RideFinishedEvent(rideId = finishedRide.id))
        }

        exception.message shouldBe usecaseError.message
        usecase should {
            it.requestedId shouldBe MaintenanceVehicleId(finishedRide.id.value)
            it.requestedAddendum shouldBe finishedRide.coveredDistance
        }
    }
}

private class MockIncreaseVehicleMileageUseCase(
    private val result: Either<IncreaseVehicleMileageUseCaseError, Unit>
) : IncreaseVehicleMileage {
    var requestedId: MaintenanceVehicleId? = null
    var requestedAddendum: Distance? = null

    override fun execute(
        id: MaintenanceVehicleId,
        addendum: Distance
    ): Either<IncreaseVehicleMileageUseCaseError, Unit> {
        requestedId = id
        requestedAddendum = addendum

        return result
    }
}