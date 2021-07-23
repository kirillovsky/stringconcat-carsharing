package com.stringconcat.kirillov.carsharing.commons.types.base

abstract class DomainEntity<T : Any> protected constructor(val id: T) {
    private var events: MutableList<DomainEvent> = mutableListOf()

    protected fun addEvent(event: DomainEvent) {
        events.add(event)
    }

    fun popEvents(): List<DomainEvent> = events.also {
        events = mutableListOf()
    }
}