package com.stringconcat.kirillov.carsharing.rules

import arrow.core.getOrHandle
import com.stringconcat.kirillov.carsharing.integration.acquirer.Acquirer
import com.stringconcat.kirillov.carsharing.listeners.DomainEventListener
import com.stringconcat.kirillov.carsharing.ride.domain.RideFinishedEvent
import com.stringconcat.kirillov.carsharing.ride.domain.Taximeter
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.RideExtractor

class DebitRidePriceRule(
    private val rideExtractor: RideExtractor,
    private val taximeter: Taximeter,
    private val acquirer: Acquirer
) : DomainEventListener<RideFinishedEvent> {
    override fun handle(event: RideFinishedEvent) {
        val rideId = event.rideId
        val ride = checkNotNull(rideExtractor.getBy(rideId)) {
            "Unable to find ride by id=$rideId"
        }

        val calculatedPrice = taximeter.calculatePrice(ride).getOrHandle {
            error("Unable to calculate price for ride(id=$rideId)")
        }
        acquirer.debitPriceFromCustomer(ride.customerId, calculatedPrice)
    }
}