package com.stringconcat.kirillov.carsharing.ride.usecase.ride

import com.stringconcat.kirillov.carsharing.ride.RideStartedEvent
import com.stringconcat.kirillov.carsharing.ride.RideStatus.STARTED
import com.stringconcat.kirillov.carsharing.ride.RideVehicleInRent
import com.stringconcat.kirillov.carsharing.ride.randomRideCustomerId
import com.stringconcat.kirillov.carsharing.ride.randomRideId
import com.stringconcat.kirillov.carsharing.ride.randomRideVehicleId
import com.stringconcat.kirillov.carsharing.ride.rideCustomer
import com.stringconcat.kirillov.carsharing.ride.rideVehicle
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideCustomerRepository
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideRepository
import com.stringconcat.kirillov.carsharing.ride.usecase.InMemoryRideVehicleRepository
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.StartRideUseCaseError.CustomerIsRejected
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.StartRideUseCaseError.CustomerNotFound
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.StartRideUseCaseError.VehicleAlreadyInRent
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.StartRideUseCaseError.VehicleNotFound
import com.stringconcat.kirillov.carsharing.ride.usecase.ride.StartRideUseCaseError.VehicleNotInRentalPool
import io.kotest.assertions.arrow.either.shouldBeLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import java.time.OffsetDateTime.now
import org.junit.jupiter.api.Test

internal class StartRideUseCaseTest {
    private val rideId = randomRideId()
    private val noOneVehiclesInRent = RideVehicleInRent { false }
    private val customerId = randomRideCustomerId()
    private val vehicleId = randomRideVehicleId()

    @Test
    fun `should start ride`() {
        val persister = InMemoryRideRepository()
        val usecase = StartRideUseCase(
            idGenerator = { rideId },
            vehicleInRent = noOneVehiclesInRent,
            customerExtractor = InMemoryRideCustomerRepository().apply {
                put(customerId, rideCustomer(id = customerId, rejected = false))
            },
            vehicleExtractor = InMemoryRideVehicleRepository().apply {
                put(vehicleId, rideVehicle(id = vehicleId, inRentalPool = true))
            },
            ridePersister = persister
        )
        val startDateTime = now()

        val result = usecase.execute(
            StartRideRequest(customerId, vehicleId, startDateTime = startDateTime)
        )

        result shouldBeRight rideId
        persister[rideId] should {
            it.shouldNotBeNull()
            it.id shouldBe rideId
            it.customerId shouldBe customerId
            it.vehicleId shouldBe vehicleId
            it.startDateTime shouldBe startDateTime
            it.status shouldBe STARTED
            it.popEvents().shouldContainExactly(RideStartedEvent(rideId = rideId))
        }
    }

    @Test
    fun `shouldn't start ride if customer not found`() {
        val persister = InMemoryRideRepository()
        val usecase = StartRideUseCase(
            idGenerator = { rideId },
            vehicleInRent = noOneVehiclesInRent,
            customerExtractor = InMemoryRideCustomerRepository(),
            vehicleExtractor = InMemoryRideVehicleRepository().apply {
                put(vehicleId, rideVehicle(id = vehicleId, inRentalPool = true))
            },
            ridePersister = persister
        )

        val result = usecase.execute(
            StartRideRequest(customerId, vehicleId, startDateTime = now())
        )

        result shouldBeLeft CustomerNotFound
        persister[rideId].shouldBeNull()
    }

    @Test
    fun `shouldn't start ride if vehicle not found`() {
        val persister = InMemoryRideRepository()
        val usecase = StartRideUseCase(
            idGenerator = { rideId },
            vehicleInRent = noOneVehiclesInRent,
            customerExtractor = InMemoryRideCustomerRepository().apply {
                put(customerId, rideCustomer(id = customerId, rejected = false))
            },
            vehicleExtractor = InMemoryRideVehicleRepository(),
            ridePersister = persister
        )

        val result = usecase.execute(
            StartRideRequest(customerId, vehicleId, startDateTime = now())
        )

        result shouldBeLeft VehicleNotFound
        persister[rideId].shouldBeNull()
    }

    @Test
    fun `shouldn't start ride if vehicle in not rental pool`() {
        val persister = InMemoryRideRepository()
        val nonRentalVehicle = rideVehicle(inRentalPool = false)
        val usecase = StartRideUseCase(
            idGenerator = { rideId },
            vehicleInRent = noOneVehiclesInRent,
            customerExtractor = InMemoryRideCustomerRepository().apply {
                put(customerId, rideCustomer(id = customerId, rejected = false))
            },
            vehicleExtractor = InMemoryRideVehicleRepository().apply {
                put(nonRentalVehicle.id, nonRentalVehicle)
            },
            ridePersister = persister
        )

        val result = usecase.execute(
            StartRideRequest(customerId, vehicleId = nonRentalVehicle.id, startDateTime = now())
        )

        result shouldBeLeft VehicleNotInRentalPool
        persister[rideId].shouldBeNull()
    }

    @Test
    fun `shouldn't start ride if customer rejected`() {
        val persister = InMemoryRideRepository()
        val rejectedCustomer = rideCustomer(rejected = true)
        val usecase = StartRideUseCase(
            idGenerator = { rideId },
            vehicleInRent = noOneVehiclesInRent,
            customerExtractor = InMemoryRideCustomerRepository().apply {
                put(rejectedCustomer.id, rejectedCustomer)
            },
            vehicleExtractor = InMemoryRideVehicleRepository().apply {
                put(vehicleId, rideVehicle(id = vehicleId, inRentalPool = true))
            },
            ridePersister = persister
        )

        val result = usecase.execute(
            StartRideRequest(customerId = rejectedCustomer.id, vehicleId, startDateTime = now())
        )

        result shouldBeLeft CustomerIsRejected
        persister[rideId].shouldBeNull()
    }

    @Test
    fun `shouldn't start ride if vehicle already in rent`() {
        val persister = InMemoryRideRepository()
        val vehicleAlreadyInRent = RideVehicleInRent { true }
        val usecase = StartRideUseCase(
            idGenerator = { rideId },
            vehicleInRent = vehicleAlreadyInRent,
            customerExtractor = InMemoryRideCustomerRepository().apply {
                put(customerId, rideCustomer(id = customerId, rejected = false))
            },
            vehicleExtractor = InMemoryRideVehicleRepository().apply {
                put(vehicleId, rideVehicle(id = vehicleId, inRentalPool = true))
            },
            ridePersister = persister
        )

        val result = usecase.execute(
            StartRideRequest(customerId, vehicleId, startDateTime = now())
        )

        result shouldBeLeft VehicleAlreadyInRent
        persister[rideId].shouldBeNull()
    }
}