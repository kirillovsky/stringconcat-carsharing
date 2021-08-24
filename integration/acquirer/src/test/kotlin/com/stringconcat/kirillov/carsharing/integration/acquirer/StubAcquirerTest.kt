package com.stringconcat.kirillov.carsharing.integration.acquirer

import com.stringconcat.kirillov.carsharing.fixtures.commons.types.valueObjects.randomPrice
import com.stringconcat.kirillov.carsharing.fixtures.ride.domain.randomRideCustomerId
import io.kotest.assertions.throwables.shouldNotThrow
import org.junit.jupiter.api.Test

internal class StubAcquirerTest {
    @Test
    fun `stub acquirer shouldn't throw exception on debit any price from any customer`() {
        shouldNotThrow<Throwable> {
            StubAcquirer.debitPriceFromCustomer(id = randomRideCustomerId(), price = randomPrice())
        }
    }
}