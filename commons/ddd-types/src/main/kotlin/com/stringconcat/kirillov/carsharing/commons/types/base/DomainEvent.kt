package com.stringconcat.kirillov.carsharing.commons.types.base

import java.time.OffsetDateTime
import java.util.UUID

abstract class DomainEvent {
    val id = EventId.generate()
}

data class EventId internal constructor(val uuid: UUID, val createdTimestamp: OffsetDateTime) {
    companion object {
        fun generate(): EventId = EventId(UUID.randomUUID(), OffsetDateTime.now())
    }
}