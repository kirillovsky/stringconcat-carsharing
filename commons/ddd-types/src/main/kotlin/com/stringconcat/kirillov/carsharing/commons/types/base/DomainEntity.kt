package com.stringconcat.kirillov.carsharing.commons.types.base

abstract class DomainEntity<T : Any> protected constructor(val id: T) {
    private var events: MutableList<DomainEvent> = mutableListOf()

    protected fun addEvents(events: List<DomainEvent>) {
        this.events.addAll(events)
    }

    fun popEvents(): List<DomainEvent> = events.also {
        events = mutableListOf()
    }
}