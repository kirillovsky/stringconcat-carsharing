package com.stringconcat.kirillov.carsharing.listeners

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent
import kotlin.reflect.KClass

interface DomainEventListener<T : DomainEvent> {
    fun eventType(): KClass<T>

    fun handle(event: T)
}