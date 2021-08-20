package com.stringconcat.kirillov.carsharing.application.listeners

import com.stringconcat.kirillov.carsharing.commons.types.base.DomainEvent
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

interface DomainEventListener<T : DomainEvent> {
    fun eventType(): KClass<*> {
        val parametrizedListenerInterface = javaClass.genericInterfaces.first() as ParameterizedType
        val eventClass = parametrizedListenerInterface.actualTypeArguments.first() as Class<*>

        return eventClass.kotlin
    }

    fun handle(event: T)
}