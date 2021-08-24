package com.stringconcat.kirillov.carsharing.fixtures.ride.usecase

import com.stringconcat.kirillov.carsharing.ride.domain.Ride
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomer
import com.stringconcat.kirillov.carsharing.ride.domain.RideCustomerId
import com.stringconcat.kirillov.carsharing.ride.domain.RideId
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicle
import com.stringconcat.kirillov.carsharing.ride.domain.RideVehicleId
import com.stringconcat.kirillov.carsharing.ride.usecase.customer.PutRideCustomer
import com.stringconcat.kirillov.carsharing.ride.usecase.customer.RideCustomerExtractor
import com.stringconcat.kirillov.carsharing.ride.usecase.customer.RideCustomerPersister
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.RideExtractor
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.RidePersister
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.PutRideVehicle
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.RideVehicleExtractor
import com.stringconcat.kirillov.carsharing.ride.usecase.vehicle.RideVehiclePersister

class InMemoryRideCustomerRepository : RideCustomerExtractor, RideCustomerPersister,
    HashMap<RideCustomerId, RideCustomer>() {
    override fun getById(id: RideCustomerId): RideCustomer? = this[id]
    override fun save(customer: RideCustomer) {
        this[customer.id] = customer
    }
}

class InMemoryRideRepository : RideExtractor, RidePersister, HashMap<RideId, Ride>() {
    override fun getBy(id: RideId): Ride? = this[id]

    override fun getByVehicleId(vehicleId: RideVehicleId): Ride? =
        values.firstOrNull { it.vehicleId == vehicleId }

    override fun save(ride: Ride) {
        this[ride.id] = ride
    }
}

class InMemoryRideVehicleRepository : RideVehicleExtractor, RideVehiclePersister,
    HashMap<RideVehicleId, RideVehicle>() {
    override fun getById(id: RideVehicleId): RideVehicle? = this[id]
    override fun save(vehicle: RideVehicle) {
        this[vehicle.id] = vehicle
    }
}

class MockPutRideCustomerUseCase : PutRideCustomer {
    var receivedRideCustomerId: RideCustomerId? = null
        private set
    var receivedNeedsToRejected: Boolean? = null
        private set

    override fun execute(id: RideCustomerId, needsToReject: Boolean) {
        receivedRideCustomerId = id
        receivedNeedsToRejected = needsToReject
    }
}

class MockPutRideVehicleUseCase : PutRideVehicle {
    var receivedRideVehicleId: RideVehicleId? = null
        private set

    var receivedInRentalPool: Boolean? = null
        private set

    override fun execute(id: RideVehicleId, inRentalPool: Boolean) {
        receivedRideVehicleId = id
        receivedInRentalPool = inRentalPool
    }
}