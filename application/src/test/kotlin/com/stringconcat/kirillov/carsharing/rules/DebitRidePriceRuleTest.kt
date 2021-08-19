package com.stringconcat.kirillov.carsharing.rules

import arrow.core.left
import arrow.core.right
import com.stringconcat.kirillov.carsharing.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.integration.acquirer.MockAcquirer
import com.stringconcat.kirillov.carsharing.ride.CalculationRidePriceError
import com.stringconcat.kirillov.carsharing.ride.RideFinishedEvent
import com.stringconcat.kirillov.carsharing.ride.StubTaximeter
import com.stringconcat.kirillov.carsharing.ride.finishedRide
import com.stringconcat.kirillov.carsharing.ride.randomRideId
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DebitRidePriceRuleTest {
    @Test
    fun `should debit calculated by taximeter price from customer`() {
        val price = randomPrice()
        val taximeter = StubTaximeter(result = price.right())
        val acquirer = MockAcquirer()
        val ride = finishedRide()
        val rule = DebitRidePriceRule(
            rideExtractor = InMemoryRideRepository().apply {
                put(ride.id, ride)
            },
            taximeter = taximeter,
            acquirer = acquirer
        )

        rule.handle(RideFinishedEvent(rideId = ride.id))

        taximeter.receivedRide shouldBe ride
        acquirer should {
            it.receivedPrice shouldBe price
            it.receivedId shouldBe ride.customerId
        }
    }

    @Test
    fun `shouldn't debit ride price if ride not found`() {
        val acquirer = MockAcquirer()
        val taximeter = StubTaximeter(result = randomPrice().right())
        val rule = DebitRidePriceRule(
            rideExtractor = InMemoryRideRepository(),
            taximeter = taximeter,
            acquirer = acquirer
        )
        val rideId = randomRideId()

        val error = shouldThrow<IllegalStateException> {
            rule.handle(RideFinishedEvent(rideId = rideId))
        }

        error.message shouldBe "Unable to find ride by id=$rideId"
        taximeter.receivedRide.shouldBeNull()
        acquirer should {
            it.receivedPrice.shouldBeNull()
            it.receivedId.shouldBeNull()
        }
    }

    @Test
    fun `should map taximeter's error on calculating price`() {
        val ride = finishedRide()
        val taximeter = StubTaximeter(result = CalculationRidePriceError.left())
        val acquirer = MockAcquirer()
        val rule = DebitRidePriceRule(
            rideExtractor = InMemoryRideRepository().apply {
                put(ride.id, ride)
            },
            taximeter = taximeter,
            acquirer = acquirer
        )

        val error = shouldThrow<IllegalStateException> {
            rule.handle(RideFinishedEvent(rideId = ride.id))
        }

        error.message shouldBe "Unable to calculate price for ride(id=${ride.id})"
        taximeter.receivedRide shouldBe ride
        acquirer should {
            it.receivedPrice.shouldBeNull()
            it.receivedId.shouldBeNull()
        }
    }
}