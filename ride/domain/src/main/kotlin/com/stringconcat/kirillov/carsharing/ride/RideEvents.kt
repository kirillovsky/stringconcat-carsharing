package com.stringconcat.kirillov.carsharing.ride

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent

data class RideFinishedEvent(val rideId: RideId) : DomainEvent()
data class RideStartedEvent(val rideId: RideId) : DomainEvent()
data class RidePaidEvent(val rideId: RideId) : DomainEvent()